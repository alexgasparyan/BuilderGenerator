package com.armdroid.bulder.validator;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.ElementFilter;
import java.util.List;

import static com.armdroid.bulder.utils.Errors.*;
import static javax.tools.Diagnostic.Kind.ERROR;

public class ClassValidator {

    private Messager messager;

    public ClassValidator(Messager messager) {
        this.messager = messager;
    }

public boolean isClassValid(Element element) {
    if (element.getKind() != ElementKind.CLASS) {
        messager.printMessage(ERROR, ERROR_CLASS_WRONG_TYPE, element);
        return false;
    }
    if (element.getModifiers().contains(Modifier.PRIVATE)) {
        messager.printMessage(ERROR, ERROR_CLASS_PRIVATE, element);
        return false;
    }
    if (element.getModifiers().contains(Modifier.ABSTRACT)) {
        messager.printMessage(ERROR, ERROR_CLASS_ABSTRACT, element);
        return false;
    }
    List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
    if (!constructors.isEmpty()) {
        for (ExecutableElement constructor : constructors) {
            if (constructor.getParameters().isEmpty()) {
                return true;
            }
        }
        messager.printMessage(ERROR, ERROR_CLASS_NO_DEFAULT_CONSTRUCTOR, element);
        return false;
    }
    return true;
}


}
