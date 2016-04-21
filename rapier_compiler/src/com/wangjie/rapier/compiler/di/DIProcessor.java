package com.wangjie.rapier.compiler.di;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import com.wangjie.rapier.api.di.annotation.RInject;
import com.wangjie.rapier.api.di.annotation.RNamed;
import com.wangjie.rapier.api.di.annotation.RModule;
import com.wangjie.rapier.api.di.core.RLazy;
import com.wangjie.rapier.compiler.base.BaseAbstractProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        long start = System.currentTimeMillis();
        logger("[process]annotations: " + Arrays.toString(annotations.toArray()) + ", roundEnv: " + roundEnv);
        try {

            HashMap<String, DIClass> clazzProcessMapper = new HashMap<>();

            // Get all elements that annotated with special annotation.
            for (Element ele : roundEnv.getElementsAnnotatedWith(RModule.class)) {

                DIClass diClass = getDIClazzSafe(ele, clazzProcessMapper);

                TypeElement typeElement = MoreElements.asType(ele);
                String packageName = typeElement.getEnclosingElement().toString();

                diClass.setTargetPackage(packageName);
                diClass.setSourceClassSimpleName(typeElement.getSimpleName().toString());

                TypeMirror moduleTypeMirror = getModuleTypeMirror(ele.getAnnotation(RModule.class));
                Element moduleElement = typeUtils.asElement(moduleTypeMirror);

                diClass.setModuleEle(moduleElement);
            }


            for (Element ele : roundEnv.getElementsAnnotatedWith(RInject.class)) {
                DIClass diClass = getDIClazzSafe(ele, clazzProcessMapper);
                AbstractDIField diField = parseDIFieldElement(ele);
                logger("diField: " + diField);

                List<Element> methodElementResults = searchMethodElementsByInjectElement(diClass.getModuleEle(), diField, ele.getAnnotation(RNamed.class));
                if (0 == methodElementResults.size()) {
                    throw new IllegalArgumentException(/*getElementOwnerClassName(ele) + "." + ele.getSimpleName() + */"[" + typeUtils.erasure(ele.asType()).toString() + "] method in module 0");
                } else if (methodElementResults.size() > 1) {
                    throw new IllegalArgumentException(/*getElementOwnerClassName(ele) + "." + ele.getSimpleName() + */"[" + typeUtils.erasure(ele.asType()).toString() + "] methods in module more than 1");
                } else {
                    diField.setInjectMethodEle(methodElementResults.get(0));
                    diClass.getInjectFieldList().add(diField);
                }
            }


            logger("clazzProcessMapper: \n" + clazzProcessMapper);
            for (Map.Entry<String, DIClass> entry : clazzProcessMapper.entrySet()) {
                try {
                    entry.getValue().brewJava().writeTo(filer);
                } catch (IOException e) {
                    logger(e.getMessage());
                }
            }
        } catch (Throwable throwable) {
            loggerE(throwable);
            throw throwable;
        } finally {
            logger("[process] tasks: " + (System.currentTimeMillis() - start) + "ms");
        }

        return true;
    }

    /**
     * Parse DIField
     * {@link DINormalField} or {@link DILazyField}
     */
    private AbstractDIField parseDIFieldElement(Element element) {
        String fieldModeName = element.asType().toString();

        AbstractDIField field;
        // Generic type, maybe RLazy
        if (fieldModeName.contains("<")) {
            String fieldModeNameWithoutGeneric = fieldModeName.substring(0, fieldModeName.indexOf("<"));
            // is RLazy
            if (fieldModeNameWithoutGeneric.equals(RLazy.class.getCanonicalName())) {
                String realFieldClassName = fieldModeName.substring(fieldModeName.indexOf("<") + 1, fieldModeName.indexOf(">"));
                field = DILazyField.create().setRealFieldClassName(realFieldClassName);
//                logger(">>>>>>>>realFieldClassName: " + realFieldClassName);
            } else { // not RLazy
                field = DINormalField.create();
            }
        } else {
            field = DINormalField.create();
        }

        field.setFieldEle(element);
        return field;
    }


    private DIClass getDIClazzSafe(Element ele, HashMap<String, DIClass> clazzProcessMapper) {
        String clazzName = getElementOwnerClassName(ele);

        DIClass diClass = clazzProcessMapper.get(clazzName);
        if (null == diClass) {
            diClass = new DIClass();
            clazzProcessMapper.put(clazzName, diClass);
        }
        return diClass;
    }

    /**
     * Key of the clazzProcessMapper items.
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
     */
    private List<Element> searchMethodElementsByInjectElement(Element moduleClassElement, AbstractDIField fieldForInject, RNamed namedOfFieldForInject) {
        List<Element> resultMethodElementList = new ArrayList<>();
        if (moduleClassElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("ElementKind of " + moduleClassElement + " is not ElementKind.CLASS");
        }

        String exceptInjectType = fieldForInject.getRealFieldClassNameForInject();
        logger("[searchMethodElementsByInjectElement]exceptInjectType: " + exceptInjectType);
//        RNamed injectEleNamedAnno = injectElement.getAnnotation(RNamed.class);

        // set exceptInjectType to injectElement's injectElementQualifierName if @named annotation had not set.
        String injectEleQualifierName = null == namedOfFieldForInject ? exceptInjectType : namedOfFieldForInject.value();

        Element superElement = MoreElements.asType(moduleClassElement);
        while (null != superElement && !superElement.toString().equals(Object.class.getCanonicalName())) {

            for (Element methodEle : superElement.getEnclosedElements()) {
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

            TypeMirror superTypeMirror = MoreElements.asType(superElement).getSuperclass();
            if (superTypeMirror.getKind() != TypeKind.DECLARED) {
                break;
            }
            superElement = ((DeclaredType) superTypeMirror).asElement();
        }


        return resultMethodElementList;
    }

    /**
     * Get the TypeMirror of Class in @Module annotation.
     * Error: Attempt to access Class object for TypeMirror...
     * http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
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
