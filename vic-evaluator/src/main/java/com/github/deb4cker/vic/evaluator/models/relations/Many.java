package com.github.deb4cker.vic.evaluator.models.relations;

import com.github.deb4cker.vic.evaluator.models.ClassData;
import com.github.deb4cker.vic.commons.StringUtils;

public class Many extends RelationshipSide {

    public Many(ClassData classData) {
        super(classData);
    }

    @Override
    public String toString() {
        return String.format("muitos(as) %s", StringUtils.pluralize(classData.getClassName()));
    }
}
