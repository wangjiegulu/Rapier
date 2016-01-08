package com.wangjie.rapier.compiler.view;

import com.google.auto.service.AutoService;
import com.wangjie.rapier.api.view.annotation.RView;
import com.wangjie.rapier.compiler.base.BaseAbstractProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/6/16.
 */
//@SupportedAnnotationTypes("com.wangjie.rapier.api.view.annotation.AIView")
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class AIViewProcessor extends BaseAbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        logger("[init]processingEnv: " + processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RView.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获得被该注解声明的元素
        for (Element ele : roundEnv.getElementsAnnotatedWith(RView.class)) {
            logger(new StringBuilder()
                    .append("----------------------------")
                    .append("\nele = ").append(ele)
                    .append("\nele.getSimpleName(): ").append(ele.getSimpleName())
                    .append("\ntypeUtils.erasure(ele.asType()): ").append(typeUtils.erasure(ele.asType()).toString())
                    .append("\nelementUtils.getPackageOf(ele).getQualifiedName(): ").append(elementUtils.getPackageOf(ele).getQualifiedName().toString())
                    .append("\nele.getEnclosingElement(): ").append(ele.getEnclosingElement())
                    .append("\n----------------------------")
                    .toString());
            switch (ele.getKind()) {
                case CLASS: // 判断该元素是否为class

                    break;
                case FIELD: // 是否为field

                    break;
            }


        }

        return true;
    }


}
