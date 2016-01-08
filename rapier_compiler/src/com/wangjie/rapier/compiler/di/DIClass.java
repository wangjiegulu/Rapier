package com.wangjie.rapier.compiler.di;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
final public class DIClass {
    private String sourceClassSimpleName;
    private String targetPackage;
    private Element moduleEle;

    private final List<DIField> injectFieldList = new ArrayList<>();

    public Element getModuleEle() {
        return moduleEle;
    }

    public void setModuleEle(Element moduleEle) {
        this.moduleEle = moduleEle;
    }

    public List<DIField> getInjectFieldList() {
        return injectFieldList;
    }


    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getSourceClassSimpleName() {
        return sourceClassSimpleName;
    }

    public void setSourceClassSimpleName(String sourceClassSimpleName) {
        this.sourceClassSimpleName = sourceClassSimpleName;
    }

    @Override
    public String toString() {
        return "DIClazz{" +
                "targetPackage='" + targetPackage + '\'' +
                ", moduleEle=" + moduleEle +
                ", injectFieldList=" + injectFieldList +
                '}';
    }

    public static final String METHOD_NAME_RAPIER_STAFF = "_Rapier";
    public static final String METHOD_NAME_CREATE = "create";
    public static final String METHOD_NAME_INJECT = "inject";
    public static final String PARAM_NAME_MODULE = "module";

    public JavaFile brewJava() {
        String targetClassSimpleName = sourceClassSimpleName + METHOD_NAME_RAPIER_STAFF;
        TypeSpec.Builder result = TypeSpec.classBuilder(targetClassSimpleName)
                .addModifiers(Modifier.PUBLIC);


        ClassName selfClassName = ClassName.get(targetPackage, targetClassSimpleName);

        MethodSpec createMethod = MethodSpec.methodBuilder(METHOD_NAME_CREATE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(selfClassName)
                .addStatement("return new $T()", selfClassName)
                .build();
        result.addMethod(createMethod);

        ClassName moduleClassName = ClassName.get(MoreElements.asType(moduleEle).getEnclosingElement().toString(), moduleEle.getSimpleName().toString());
        ClassName sourceClassName = ClassName.get(targetPackage, sourceClassSimpleName);

        String sourceArg0 = sourceClassSimpleName.substring(0, 1).toLowerCase() + sourceClassSimpleName.substring(1);
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(METHOD_NAME_INJECT)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(moduleClassName, PARAM_NAME_MODULE)
                .addParameter(sourceClassName, sourceArg0)
                .beginControlFlow("if(null == module)")
                .addStatement("throw new $T(\"Module of \" + " + sourceArg0 + " + \" can not be null!\")", NullPointerException.class)
                .endControlFlow();

        if (!injectFieldList.isEmpty()) {
            for (DIField field : injectFieldList) {
                injectMethodBuilder.addStatement(sourceArg0 + "." + field.getFieldEle().toString() + " = module." + field.getInjectMethodEle());
            }
        }

        result.addMethod(injectMethodBuilder.build());
        return JavaFile.builder(targetPackage, result.build())
                .addFileComment("GENERATED CODE BY RAPIER. DO NOT MODIFY!")
                .skipJavaLangImports(true)
                .build();
    }


}
