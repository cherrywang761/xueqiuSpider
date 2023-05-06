package org.decaywood.mapper;

import org.decaywood.AbstractRequester;
import org.decaywood.CookieProcessor;
import org.decaywood.entity.DeepCopy;
import org.decaywood.timeWaitingStrategy.TimeWaitingStrategy;
import org.decaywood.utils.URLMapper;

import java.io.IOException;
import java.util.function.Function;

/**
 *
 * @author: decaywood
 * @date: 2015/11/24 16:56
 */

public abstract class AbstractMapper<T, R> extends AbstractRequester implements
        Function<T, R>,
        CookieProcessor {


    protected abstract R mapLogic(T t) throws Exception;


    public AbstractMapper(TimeWaitingStrategy strategy) {
        this(strategy, URLMapper.MAIN_PAGE.toString());
    }

    public AbstractMapper(TimeWaitingStrategy strategy, String webSite) {
        super(strategy, webSite);
    }


    //重写apply(),Function函数式编程apply()是必定执行的
    @Override
    public R apply(T t) {

        if (t != null) {
            System.out.println(getClass().getSimpleName() + " mapping -> " + t.getClass().getSimpleName());
        }

        R res = null;
        //重试次数
        int retryTime = this.strategy.retryTimes();

        try {

            int loopTime = 1;
            boolean needRMI = true;

            //如果对象实现了DeepCopy接口，说明对象需要在模块中进行传递，所以进行深拷贝
            if (t != null) //noinspection unchecked
            {
                t = t instanceof DeepCopy ? ((DeepCopy<T>) t).copy() : t;
            }
            //重试mapLogic()
            while (retryTime > loopTime) {
                try {
                    //映射逻辑生成新响应对象
                    res = mapLogic(t);
                    needRMI = false;
                    break;
                } catch (Exception e) {
                    if (!(e instanceof IOException)) throw e;
                    System.out.println("Mapper: Network busy Retrying -> " + loopTime + " times" + "  " + this.getClass().getSimpleName());
                    updateCookie(webSite);
                    this.strategy.waiting(loopTime++);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }


}
