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
final public class DIClazz {
    private String sourceClassSimpleName;
    private String targetPackage;
    private Element moduleElement;

    private final List<DIField> injectFieldList = new ArrayList<>();

    public Element getModuleElement() {
        return moduleElement;
    }

    public void setModuleElement(Element moduleElement) {
        this.moduleElement = moduleElement;
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
                ", moduleElement=" + moduleElement +
                ", injectFieldList=" + injectFieldList +
                '}';
    }

    public JavaFile brewJava() {
        String targetClassSimpleName = sourceClassSimpleName + "_Rapier";
        TypeSpec.Builder result = TypeSpec.classBuilder(targetClassSimpleName)
                .addModifiers(Modifier.PUBLIC);


        ClassName selfClassName = ClassName.get(targetPackage, targetClassSimpleName);

        MethodSpec createMethod = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(selfClassName)
                .addStatement("return new $T()", selfClassName)
                .build();
        result.addMethod(createMethod);

        ClassName moduleClassName = ClassName.get(MoreElements.asType(moduleElement).getEnclosingElement().toString(), moduleElement.getSimpleName().toString());
        ClassName sourceClassName = ClassName.get(targetPackage, sourceClassSimpleName);

        String sourceArg0 = sourceClassSimpleName.substring(0, 1).toLowerCase() + sourceClassSimpleName.substring(1);
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(moduleClassName, "module")
                .addParameter(sourceClassName, sourceArg0)
                .beginControlFlow("if(null == module)")
                .addStatement("throw new $T(\"Module of \" + " + sourceArg0 + " + \" can not be null!\")", NullPointerException.class)
                .endControlFlow();

        if (!injectFieldList.isEmpty()) {
            for (DIField field : injectFieldList) {
                injectMethodBuilder.addStatement(sourceArg0 + "." + field.getFieldName() + " = module." + field.getInjectMethod());
            }
        }

        result.addMethod(injectMethodBuilder.build());
        return JavaFile.builder(targetPackage, result.build())
                .addFileComment("GENERATED CODE BY RAPIER. DO NOT MODIFY!")
                .skipJavaLangImports(true)
                .build();
    }


}
