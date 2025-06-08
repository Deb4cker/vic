package com.github.deb4cker.vic.evaluator.models.relations;

import com.github.deb4cker.vic.evaluator.models.ClassData;

public abstract class RelationshipSide {
    protected final ClassData classData;

    public RelationshipSide(ClassData classData) {
        this.classData = classData;
    }

    @Override
    public String toString(){
        return classData.toString();
    }
}
