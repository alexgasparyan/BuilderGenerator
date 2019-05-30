package com.armdroid.bulder.utils;

import com.armdroid.builder.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

public class Utils {

    public static String getSetterMethodName(String variableName) {
        return  "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
    }

    public static String getGeneratedClassName(Element element) {
        Builder annotation = element.getAnnotation(Builder.class);
        return annotation.className().isEmpty() ? element.getSimpleName().toString() + "Builder" : annotation.className();
    }
}
