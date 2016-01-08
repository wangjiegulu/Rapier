package com.wangjie.rapier.compiler.di;

import com.squareup.javapoet.*;
import com.wangjie.rapier.api.util.func.FuncR;

import javax.lang.model.element.Modifier;

import static com.wangjie.rapier.compiler.util.EasyType.bestGuess;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class DILazyField extends AbstractDIField {
    public static DILazyField create() {
        return new DILazyField(DIFieldMode.LAZY);
    }

    /**
     * The real field in RLazy.
     * <code>
     *
     * @Inject RLazy&lt;Hello&gt;
     * </code>
     */
    private String realFieldClassName;

    public DILazyField setRealFieldClassName(String realFieldClassName) {
        this.realFieldClassName = realFieldClassName;
        return this;
    }

    public DILazyField(DIFieldMode fieldMode) {
        super(fieldMode);
    }

    public DILazyField(DIFieldMode fieldMode, String realFieldClassName) {
        super(fieldMode);
        this.realFieldClassName = realFieldClassName;
    }

    @Override
    public void brewJavaStatementInject(MethodSpec.Builder injectMethodBuilder, String belongObjectParam) {
        // "module." + getInjectMethodEle().toString()
        TypeName funcRGenericTypeName = bestGuess(realFieldClassName);
        TypeSpec funcR = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(FuncR.class), funcRGenericTypeName))
                .addMethod(
                        MethodSpec.methodBuilder("call")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(funcRGenericTypeName)
                                .addStatement("return module." + getInjectMethodEle().toString()).build()
                )
                .build();

        injectMethodBuilder.addStatement(belongObjectParam + "." + getFieldEle().toString() + ".setCreation($L)", funcR);
    }

    @Override
    public String getRealFieldClassNameForInject() {
        return realFieldClassName;
    }

    @Override
    public String toString() {
        return super.toString() + "]]DILazyField{" +
                "realFieldClassName='" + realFieldClassName + '\'' +
                '}';
    }
}
