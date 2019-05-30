package com.armdroid.bulder.utils;

public class Errors {
    public static final String ERROR_CLASS_WRONG_TYPE = "Element with @Builder annotation must be of type class (i.e. not interface)";
    public static final String ERROR_CLASS_PRIVATE = "Class with @Builder annotation cannot be private";
    public static final String ERROR_CLASS_ABSTRACT = "Class with @Builder annotation cannot be abstract";
    public static final String ERROR_CLASS_NO_DEFAULT_CONSTRUCTOR = "Class with @Builder annotation must have empty constructor";
    public static final String ERROR_VARIABLE_ACCESS = "Variable must be either non-private or must have non-static setter";
}
