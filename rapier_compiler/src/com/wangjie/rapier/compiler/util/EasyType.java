package com.wangjie.rapier.compiler.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/8/16.
 */
public class EasyType {
    /**
     * Reference -> https://github.com/JakeWharton/butterknife/blob/master/butterknife-compiler/src/main/java/butterknife/compiler/BindingClass.java
     * @param type
     * @return
     */
    public static TypeName bestGuess(String type) {
        switch (type) {
            case "void": return TypeName.VOID;
            case "boolean": return TypeName.BOOLEAN;
            case "byte": return TypeName.BYTE;
            case "char": return TypeName.CHAR;
            case "double": return TypeName.DOUBLE;
            case "float": return TypeName.FLOAT;
            case "int": return TypeName.INT;
            case "long": return TypeName.LONG;
            case "short": return TypeName.SHORT;
            default:
                int left = type.indexOf('<');
                if (left != -1) {
                    ClassName typeClassName = ClassName.bestGuess(type.substring(0, left));
                    List<TypeName> typeArguments = new ArrayList<>();
                    do {
                        typeArguments.add(WildcardTypeName.subtypeOf(Object.class));
                        left = type.indexOf('<', left + 1);
                    } while (left != -1);
                    return ParameterizedTypeName.get(typeClassName,
                            typeArguments.toArray(new TypeName[typeArguments.size()]));
                }
                return ClassName.bestGuess(type);
        }
    }
}
