package com.github.deb4cker.vic.evaluator.models.relations;

import com.github.deb4cker.vic.evaluator.models.ClassData;

public class InheritanceData {
    public ClassData superClass;
    public ClassData subClass;

    public InheritanceData(ClassData superClass, ClassData subClass) {
        this.superClass = superClass;
        this.subClass = subClass;
    }

    public ClassData getSuperClass() {
        return superClass;
    }

    public ClassData getSubClass() {
        return subClass;
    }
}
