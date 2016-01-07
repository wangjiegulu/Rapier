package com.wangjie.rapier.compiler.di;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.wangjie.rapier.api.di.annotation.Module;
import com.wangjie.rapier.compiler.base.BaseAbstractProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.*;

/**
 * Error: Attempt to access Class object for TypeMirror...
 * http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
 * <p/>
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/7/16.
 */
@AutoService(Processor.class)
public class DIProcessor extends BaseAbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        logger("[init]processingEnv: " + processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypesSet = new HashSet<>();

        supportedTypesSet.add(Module.class.getCanonicalName());
        supportedTypesSet.add(Inject.class.getCanonicalName());
        // add more annotations...

        return supportedTypesSet;
    }

    private final HashMap<String, DIClazz> clazzProcessMapper = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

//        try{

        // 获得被该注解声明的元素
        for (Element ele : roundEnv.getElementsAnnotatedWith(Module.class)) {

            DIClazz diClazz = getDIClazzSafe(ele);

            TypeElement typeElement = MoreElements.asType(ele);
            String packageName = typeElement.getEnclosingElement().toString();

            diClazz.setTargetPackage(packageName);
            diClazz.setSourceClassSimpleName(typeElement.getSimpleName().toString());

            TypeMirror moduleTypeMirror = getModuleTypeMirror(ele.getAnnotation(Module.class));
            Element moduleElement = typeUtils.asElement(moduleTypeMirror);

            for (Element ee : moduleElement.getEnclosedElements()) {
                logger("-> ee: " + ee);

            }

//            logger("moduleClazzName: " + moduleTypeMirror.toString());
            diClazz.setModuleElement(moduleElement);
        }


        for (Element ele : roundEnv.getElementsAnnotatedWith(Inject.class)) {
            DIClazz diClazz = getDIClazzSafe(ele);
//            logger("diClazz.getModuleElement(): " + diClazz.getModuleElement());
            List<Element> methodElementResults = searchMethodElement(diClazz.getModuleElement(), ele.asType());
            if (0 == methodElementResults.size()) {
                throw new IllegalArgumentException(typeUtils.erasure(ele.asType()).toString() + " method 0");
            } else if (methodElementResults.size() > 1) {
                throw new IllegalArgumentException(typeUtils.erasure(ele.asType()).toString() + " method more than 1");
            } else {
                diClazz.getInjectFieldList().add(new DIField(methodElementResults.get(0), ele.toString()));
            }
        }

        logger("clazzProcessMapper: \n" + clazzProcessMapper);
//        }catch(Throwable throwable){
//            logger(throwable.getMessage());
//        }


        for (Map.Entry<String, DIClazz> entry : clazzProcessMapper.entrySet()) {
            try {
                entry.getValue().brewJava().writeTo(filer);
            } catch (IOException e) {
                logger(e.getMessage());
            }
        }
        return true;
    }

    private DIClazz getDIClazzSafe(Element ele) {
        String clazzName = getElementOwnerClassName(ele);

        DIClazz diClazz = clazzProcessMapper.get(clazzName);
        if (null == diClazz) {
            diClazz = new DIClazz();
            logger("---> getDIClazzSafe create key...: " + clazzName);
            clazzProcessMapper.put(clazzName, diClazz);
        }
        return diClazz;
    }

    /**
     * Key of the clazzProcessMapper items.
     *
     * @param element
     * @return
     */
    private String getElementOwnerClassName(Element element) {
        String clazzName;

        // todo: Get the Class which element's owner.
        if (element.getKind() == ElementKind.FIELD) {
            clazzName = MoreElements.asVariable(element).getEnclosingElement().toString();
            logger("[getDIClazzSafe]MoreElements.asVariable().getEnclosingElement(): " + MoreElements.asVariable(element).getEnclosingElement());
        } else {
            clazzName = typeUtils.erasure(element.asType()).toString();
        }
        return clazzName;
    }

    /**
     * Search all methods that with special return type.
     *
     * @param clazzElement
     * @param expectReturnType
     * @return
     */
    private List<Element> searchMethodElement(Element clazzElement, TypeMirror expectReturnType) {
        List<Element> resultMethodElementList = new ArrayList<>();
        if (clazzElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("ElementKind of " + clazzElement + " is not ElementKind.CLASS");
        }
        for (Element ee : MoreElements.asType(clazzElement).getEnclosedElements()) {
            if (ElementKind.METHOD != ee.getKind()) {
                continue;
            }
            // Same return type.
            // todo: add alias to support multi same type
            if (MoreElements.asExecutable(ee).getReturnType().toString()
                    .equals(expectReturnType.toString())) {
                resultMethodElementList.add(ee);
            }
        }
        return resultMethodElementList;
    }

    /**
     * Get the TypeMirror of Class in @Module annotation.
     * Error: Attempt to access Class object for TypeMirror...
     * http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
     *
     * @param annotation
     * @return
     */
    private static TypeMirror getModuleTypeMirror(Module annotation) {
        try {
            annotation.moduleClazz(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

}
