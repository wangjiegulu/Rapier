package com.wangjie.rapier.compiler.di;

import javax.lang.model.element.Element;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
public class DIField {
    private Element injectMethod;
    private String fieldName;

    public DIField(Element injectMethod, String fieldName) {
        this.injectMethod = injectMethod;
        this.fieldName = fieldName;
    }

    public Element getInjectMethod() {
        return injectMethod;
    }

    public void setInjectMethod(Element injectMethod) {
        this.injectMethod = injectMethod;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "DIField{" +
                "injectMethod=" + injectMethod +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
