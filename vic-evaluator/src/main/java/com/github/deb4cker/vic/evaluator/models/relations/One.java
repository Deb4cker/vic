package com.github.deb4cker.vic.evaluator.models.relations;

import com.github.deb4cker.vic.evaluator.models.ClassData;

public class One extends RelationshipSide{

    public One(ClassData classData) {
        super(classData);
    }

    @Override
    public String toString(){
        return "Um(a) " + classData.getClassName();
    }
}
