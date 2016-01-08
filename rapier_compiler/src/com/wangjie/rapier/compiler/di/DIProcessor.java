package com.wangjie.rapier.compiler.di;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.wangjie.rapier.api.di.annotation.RInject;
import com.wangjie.rapier.api.di.annotation.RModule;
import com.wangjie.rapier.api.di.annotation.RNamed;
import com.wangjie.rapier.compiler.base.BaseAbstractProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
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

        supportedTypesSet.add(RModule.class.getCanonicalName());
        supportedTypesSet.add(RInject.class.getCanonicalName());
//        supportedTypesSet.add(RNamed.class.getCanonicalName());
        // add more annotations...

        return supportedTypesSet;
    }

    private final HashMap<String, DIClass> clazzProcessMapper = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

//        try{

        // Get all elements that annotated with special annotation.
        for (Element ele : roundEnv.getElementsAnnotatedWith(RModule.class)) {

            DIClass diClass = getDIClazzSafe(ele);

            TypeElement typeElement = MoreElements.asType(ele);
            String packageName = typeElement.getEnclosingElement().toString();

            diClass.setTargetPackage(packageName);
            diClass.setSourceClassSimpleName(typeElement.getSimpleName().toString());

            TypeMirror moduleTypeMirror = getModuleTypeMirror(ele.getAnnotation(RModule.class));
            Element moduleElement = typeUtils.asElement(moduleTypeMirror);

            diClass.setModuleEle(moduleElement);
        }


        for (Element ele : roundEnv.getElementsAnnotatedWith(RInject.class)) {
            DIClass diClass = getDIClazzSafe(ele);
            List<Element> methodElementResults = searchMethodElementsByInjectElement(diClass.getModuleEle(), ele);
            if (0 == methodElementResults.size()) {
                throw new IllegalArgumentException(getElementOwnerClassName(ele) + "." + ele.getSimpleName() + "[" + typeUtils.erasure(ele.asType()).toString() + "] method in module 0");
            } else if (methodElementResults.size() > 1) {
                throw new IllegalArgumentException(getElementOwnerClassName(ele) + "." + ele.getSimpleName() + "[" + typeUtils.erasure(ele.asType()).toString() + "] methods in module more than 1");
            } else {
                diClass.getInjectFieldList().add(new DIField(methodElementResults.get(0), ele));
            }
        }

        logger("clazzProcessMapper: \n" + clazzProcessMapper);
//        }catch(Throwable throwable){
//            logger(throwable.getMessage());
//        }


        for (Map.Entry<String, DIClass> entry : clazzProcessMapper.entrySet()) {
            try {
                entry.getValue().brewJava().writeTo(filer);
            } catch (IOException e) {
                logger(e.getMessage());
            }
        }
        return true;
    }

    private DIClass getDIClazzSafe(Element ele) {
        String clazzName = getElementOwnerClassName(ele);

        DIClass diClass = clazzProcessMapper.get(clazzName);
        if (null == diClass) {
            diClass = new DIClass();
            logger("---> getDIClazzSafe create key...: " + clazzName);
            clazzProcessMapper.put(clazzName, diClass);
        }
        return diClass;
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
     * Search all methods that with special return type and @RNamed.
     *
     * @param moduleClassElement
     * @param injectElement
     * @return
     */
    private List<Element> searchMethodElementsByInjectElement(Element moduleClassElement, Element injectElement) {
        List<Element> resultMethodElementList = new ArrayList<>();
        if (moduleClassElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("ElementKind of " + moduleClassElement + " is not ElementKind.CLASS");
        }

        String exceptInjectType = injectElement.asType().toString();
        RNamed injectEleNamedAnno = injectElement.getAnnotation(RNamed.class);

        // set exceptInjectType to injectElement's injectElementQualifierName if @named annotation had not set.
        String injectEleQualifierName = null == injectEleNamedAnno ? exceptInjectType : injectEleNamedAnno.value();

        for (Element methodEle : MoreElements.asType(moduleClassElement).getEnclosedElements()) {
            if (ElementKind.METHOD != methodEle.getKind()) {
                continue;
            }

            // Match the type of method in module as same as the type of injectElement.
            String methodReturnType = MoreElements.asExecutable(methodEle).getReturnType().toString();
            RNamed methodEleNamedAnno = methodEle.getAnnotation(RNamed.class);
            String methodEleQualifierName = null == methodEleNamedAnno ? methodReturnType : methodEleNamedAnno.value();

            if (methodReturnType.equals(exceptInjectType) // Same type
                    &&
                    methodEleQualifierName.equals(injectEleQualifierName) // Same named
                    ) {
                resultMethodElementList.add(methodEle);
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
    private static TypeMirror getModuleTypeMirror(RModule annotation) {
        try {
            annotation.moduleClazz(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

}
