package com.armdroid.bulder;

import com.armdroid.bulder.types.BuilderClass;
import com.armdroid.bulder.types.BuilderVariable;
import com.armdroid.bulder.utils.Utils;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class Generator {

    static void generateClasses(Filer filer, List<BuilderClass> builderClassList) {
        for (BuilderClass builderClass : builderClassList) {
            try {
                getJavaFile(builderClass).writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JavaFile getJavaFile(BuilderClass builderClass) {
        ClassName genClassName = ClassName.get(builderClass.getPackageName(), builderClass.getGeneratedClassName());
        String target = builderClass.getClassName().toLowerCase();

        List<BuilderVariable> variables = builderClass.getVariableList();

        List<BuilderVariable> requiredVariables = variables
                .stream()
                .filter(BuilderVariable::isRequired)
                .collect(Collectors.toList());

        List<BuilderVariable> nonRequiredVariables = variables
                .stream()
                .filter(var -> !var.isRequired())
                .collect(Collectors.toList());

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(builderClass.getGeneratedClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (BuilderVariable var : variables) {
            classBuilder.addField(FieldSpec.builder(var.getType(), var.getName(), Modifier.PRIVATE).build());
        }

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        requiredVariables.stream()
                .map(var -> {
                    constructorBuilder.addStatement("this.$L = $L", var.getName(), var.getName());
                    return ParameterSpec.builder(var.getType(), var.getName()).build();
                })
                .forEach(constructorBuilder::addParameter);

        classBuilder.addMethod(constructorBuilder.build());

        for (BuilderVariable variable : nonRequiredVariables) {
            MethodSpec method = MethodSpec.methodBuilder(Utils.getSetterMethodName(variable.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(variable.getType(), variable.getName())
                    .returns(genClassName)
                    .addStatement("this.$L = $L", variable.getName(), variable.getName())
                    .addStatement("return this")
                    .build();
            classBuilder.addMethod(method);
        }

        MethodSpec.Builder buildMethodBuilder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass.getType())
                .addStatement("$T $L = new $T()", builderClass.getType(), target, builderClass.getType());

        for (BuilderVariable var : variables) {
            buildMethodBuilder.addStatement(getSetterStatement(target, var));
        }
        MethodSpec buildMethod = buildMethodBuilder.addStatement("return $L", target).build();
        classBuilder.addMethod(buildMethod);


        return JavaFile.builder(builderClass.getPackageName(), classBuilder.build()).build();


    }

    private static CodeBlock getSetterStatement(String target, BuilderVariable variable) {
        boolean isPrivate = variable.isPrivate();
        String structure = isPrivate ? "$L.$L($L)" : "$L.$L = $L";
        String setter = isPrivate ? Utils.getSetterMethodName(variable.getName()) : variable.getName();
        return CodeBlock.of(structure, target, setter, variable.getName());
    }


}
