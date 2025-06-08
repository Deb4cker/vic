package com.github.deb4cker.vic.evaluator.analyzers;

import com.github.deb4cker.vic.commons.interfaces.Loggable;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectlyImplementedClass;
import com.github.deb4cker.vic.evaluator.inspectors.AttributeInspector;
import com.github.deb4cker.vic.evaluator.inspectors.ConstructorInspector;
import com.github.deb4cker.vic.evaluator.inspectors.MethodInspector;
import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.evaluator.utils.InspectorRunner;

import java.util.ArrayList;
import java.util.List;


public class ClassAnalyser implements Loggable {

    private final ClassData modelClassData;
    private final ClassData submittedClassData;

    public ClassAnalyser(ClassData modelClassData, ClassData submittedClassData) {
        this.modelClassData = modelClassData;
        this.submittedClassData = submittedClassData;
    }

    public List<ImplementationFlag> inspectClass() throws InterruptedException {
        String className = modelClassData.getClassName();

        InspectorRunner attributeInspector   = new InspectorRunner(new AttributeInspector(modelClassData.getFields(), submittedClassData.getFields(), className));
        InspectorRunner constructorInspector = new InspectorRunner(new ConstructorInspector(modelClassData.getConstructors(), submittedClassData.getConstructors(), className));
        InspectorRunner methodInspector      = new InspectorRunner(new MethodInspector(modelClassData.getMethods(), submittedClassData.getMethods(), className));

        attributeInspector.start();
        constructorInspector.start();
        methodInspector.start();

        attributeInspector.join();
        constructorInspector.join();
        methodInspector.join();

        List<ImplementationFlag> flags = new ArrayList<>();
        flags.addAll(attributeInspector.getFlags());
        flags.addAll(constructorInspector.getFlags());
        flags.addAll(methodInspector.getFlags());

        boolean onlyCorrectFlags = ImplementationFlag.onlyCorrectFlagsIn(flags);

        if (onlyCorrectFlags) {
            flags.add(new CorrectlyImplementedClass(modelClassData.getClassName()));
        }

        return flags;
    }

    public String getModelClassDataName() {
        return modelClassData.getClassName();
    }
}
