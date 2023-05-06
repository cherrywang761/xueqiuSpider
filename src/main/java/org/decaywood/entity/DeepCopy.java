package org.decaywood.entity;

import java.io.Serializable;

/**
 * @Description 对于你定义了在模块间传递的对象，请实现DeepCopy接口
 * @author: decaywood
 * @date: 2015/11/24 19:52
 */
public interface DeepCopy <R> extends Serializable {

    R copy();

}
