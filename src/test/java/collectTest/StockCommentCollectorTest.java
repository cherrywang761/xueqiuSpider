package collectTest;

import org.decaywood.collector.StockCommentCollector;
import org.decaywood.entity.PostInfo;
import org.junit.Test;

import java.util.List;

/**
 * @Description 雪球网股票评论收集器测试
 * @author decaywood
 * @date 2020/10/7 22:05
 */
public class StockCommentCollectorTest {

    @Test
    public void test() {
        List<PostInfo> infos = new StockCommentCollector("SH605577", StockCommentCollector.SortType.time, 1, 10).get();
        for (PostInfo info : infos) {
            System.out.println(info.getDescription());
            System.out.println("===============");
            System.out.println("===============");
        }
//        Assert.assertEquals(1, infos.size());
    }
}
