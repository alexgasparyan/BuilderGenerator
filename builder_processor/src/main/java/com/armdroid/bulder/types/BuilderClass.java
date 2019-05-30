package com.armdroid.bulder.types;

import com.armdroid.bulder.utils.Utils;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

public class BuilderClass {

    private String packageName;
    private String className;
    private String generatedClassName;
    private TypeName type;
    private List<BuilderVariable> variableList;

    public BuilderClass(Element element, Elements elementUtils) {
        this.packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        this.className = element.getSimpleName().toString();
        this.generatedClassName = Utils.getGeneratedClassName(element);
        this.type = TypeName.get(element.asType());
        this.variableList = new ArrayList<>();
    }

    public void addVariable(BuilderVariable variable) {
        this.variableList.add(variable);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGeneratedClassName() {
        return generatedClassName;
    }

    public void setGeneratedClassName(String generatedClassName) {
        this.generatedClassName = generatedClassName;
    }

    public TypeName getType() {
        return type;
    }

    public void setType(TypeName type) {
        this.type = type;
    }

    public List<BuilderVariable> getVariableList() {
        return variableList;
    }

    public void setVariableList(List<BuilderVariable> variableList) {
        this.variableList = variableList;
    }
}
