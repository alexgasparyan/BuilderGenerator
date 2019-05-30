package com.armdroid.bulder;

import com.armdroid.builder.Builder;
import com.armdroid.builder.Transient;
import com.armdroid.bulder.types.BuilderClass;
import com.armdroid.bulder.types.BuilderVariable;
import com.armdroid.bulder.validator.ClassValidator;
import com.armdroid.bulder.validator.VariableValidator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

public class Processor extends AbstractProcessor {


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(Builder.class.getCanonicalName()));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationSet, RoundEnvironment roundEnvironment) {
        for (TypeElement annotation : annotationSet) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            ClassValidator classValidator = new ClassValidator(processingEnv.getMessager());
            List<BuilderClass> builderClassList = new ArrayList<>();
            for (Element element: elements) {
                if (classValidator.isClassValid(element)) {
                    BuilderClass builderClass = new BuilderClass(element, processingEnv.getElementUtils());
                    VariableValidator variableValidator = new VariableValidator(element,
                            processingEnv.getMessager(), processingEnv.getTypeUtils());
                    for (VariableElement variable : getEligibleVariables(element)) {
                        if (variableValidator.isVariableValid(variable)) {
                            builderClass.addVariable(new BuilderVariable(variable));
                        } else {
                            return false;
                        }
                    }
                    builderClassList.add(builderClass);
                } else {
                    return false;
                }
            }


            Generator.generateClasses(processingEnv.getFiler(), builderClassList);


        }
        return true;
    }

    //static, final and fields with @Transient annotation are ignored
    private List<VariableElement> getEligibleVariables(Element element) {
        return ElementFilter.fieldsIn(element.getEnclosedElements())
                .stream()
                .filter(variable ->
                        !variable.getModifiers().contains(Modifier.STATIC) &&
                        !variable.getModifiers().contains(Modifier.FINAL) &&
                        variable.getAnnotation(Transient.class) == null)
                .collect(Collectors.toList());
    }
}
