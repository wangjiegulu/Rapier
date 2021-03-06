package com.wangjie.rapier.api.di.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RNamed {
    String value();
}
