package com.armdroid.bulder.validator;

import com.armdroid.bulder.utils.Utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;

import static com.armdroid.bulder.utils.Errors.ERROR_VARIABLE_ACCESS;
import static javax.tools.Diagnostic.Kind.ERROR;

public class VariableValidator {

    private Element enclosingElement;
    private Messager messager;
    private Types typeUtils;

    public VariableValidator(Element enclosingElement, Messager messager, Types typeUtils) {
        this.enclosingElement = enclosingElement;
        this.messager = messager;
        this.typeUtils = typeUtils;
    }

    public boolean isVariableValid(VariableElement variable) {
        if (!variable.getModifiers().contains(Modifier.PRIVATE)) {
            return true;
        }
        String setterName = Utils.getSetterMethodName(variable.getSimpleName().toString());
        for (ExecutableElement method : ElementFilter.methodsIn(enclosingElement.getEnclosedElements())) {
            boolean isVoid = method.getReturnType().getKind() == TypeKind.VOID;
            boolean isNotStatic = !method.getModifiers().contains(Modifier.STATIC);
            boolean hasValidName = method.getSimpleName().toString().equals(setterName);
            boolean hasValidParameter = method.getParameters().size() == 1
                    && typeUtils.isAssignable(variable.asType(), method.getParameters().get(0).asType());
            if (isVoid && isNotStatic && hasValidName && hasValidParameter) {
                return true;
            }
        }
        messager.printMessage(ERROR, ERROR_VARIABLE_ACCESS, variable);
        return false;
    }
}
