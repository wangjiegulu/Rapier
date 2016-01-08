package com.wangjie.rapier.api.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
//@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface RView {
    int value() default -1; // 控件resId
}
