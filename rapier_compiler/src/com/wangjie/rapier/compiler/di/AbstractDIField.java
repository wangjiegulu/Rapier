package com.wangjie.rapier.compiler.di;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Element;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public abstract class AbstractDIField {

    public enum DIFieldMode {
        NORMAL,
        /**
         * If element is lazy mode, it will not call <code>injectMethodEle</code> method until <code>RLazy.get()<code/> method been called.
         *
         * see {@link com.wangjie.rapier.api.di.core.RLazy}
         */
        LAZY
    }

    /**
     * Present an inject element.
     */
    private Element fieldEle;
    /**
     * The method that provider object for injection.
     */
    private Element injectMethodEle;
    /**
     * Field type, see {@link DIFieldMode}.
     */
    private DIFieldMode fieldMode;

    public AbstractDIField(DIFieldMode fieldMode) {
        this.fieldMode = fieldMode;
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

    public DIFieldMode getFieldMode() {
        return fieldMode;
    }

    public abstract void brewJavaStatementInject(MethodSpec.Builder injectMethodBuilder, String belongObjectParam);

    /**
     * Real field class name for inject
     *
     * <code>
     *     @Inject
     *     String str;
     * </code>
     *
     * <code>
     *     @Inject
     *     RLazy&lt;String&gt;
     * </code>
     *
     * Both should return "java.lang.String".
     *
     */
    public abstract String getRealFieldClassNameForInject();

    @SuppressWarnings("unchecked")
    public <T extends AbstractDIField> T asSubClass(Class<T> subClass) {
        return (T) this;
    }

    @Override
    public String toString() {
        return "AbstractDIField{" +
                "fieldEle=" + fieldEle +
                ", injectMethodEle=" + injectMethodEle +
                ", fieldMode=" + fieldMode +
                '}';
    }
}
