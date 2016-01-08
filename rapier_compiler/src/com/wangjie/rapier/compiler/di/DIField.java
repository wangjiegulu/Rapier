package com.wangjie.rapier.compiler.di;

import javax.lang.model.element.Element;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
public class DIField {
    private Element injectMethodEle;
    private Element fieldEle;

    public DIField(Element injectMethodEle, Element fieldEle) {
        this.injectMethodEle = injectMethodEle;
        this.fieldEle = fieldEle;
    }

    public Element getInjectMethodEle() {
        return injectMethodEle;
    }

    public void setInjectMethodEle(Element injectMethodEle) {
        this.injectMethodEle = injectMethodEle;
    }

    public Element getFieldEle() {
        return fieldEle;
    }

    public void setFieldEle(Element fieldEle) {
        this.fieldEle = fieldEle;
    }
}
