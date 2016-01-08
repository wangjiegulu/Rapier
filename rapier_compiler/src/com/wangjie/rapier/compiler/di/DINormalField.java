package com.wangjie.rapier.compiler.di;

import com.squareup.javapoet.MethodSpec;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
public class DINormalField extends AbstractDIField {
    public static DINormalField create() {
        return new DINormalField(DIFieldMode.NORMAL);
    }

    public DINormalField(DIFieldMode fieldMode) {
        super(fieldMode);
    }

    @Override
    public void brewJavaStatementInject(MethodSpec.Builder injectMethodBuilder, String belongObjectParam) {
        injectMethodBuilder.addStatement(belongObjectParam + "." + getFieldEle().toString() + " = module." + getInjectMethodEle().toString());
    }

    @Override
    public String getRealFieldClassNameForInject() {
        return getFieldEle().asType().toString();
    }

}
