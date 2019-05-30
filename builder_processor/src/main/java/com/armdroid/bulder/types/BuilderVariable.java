package com.armdroid.bulder.types;

import com.armdroid.builder.Required;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

public class BuilderVariable {

    private String name;
    private TypeName type;
    private boolean isPrivate;
    private boolean isRequired;

    public BuilderVariable(VariableElement variable) {
        this.name = variable.getSimpleName().toString();
        this.type = TypeName.get(variable.asType());
        this.isPrivate = variable.getModifiers().contains(Modifier.PRIVATE);
        this.isRequired = variable.getAnnotation(Required.class) != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeName getType() {
        return type;
    }

    public void setType(TypeName type) {
        this.type = type;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}
