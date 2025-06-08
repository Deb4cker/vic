package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.correctImplementation.CorrectlyImplementedAttribute;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.impl.AttributeFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.AttributeStructure;
import com.github.deb4cker.vic.commons.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class AttributeInspector extends AbstractInspector<Field, AttributeStructure> {
    private final String className;

    public AttributeInspector(Field[] modeledAttributes, Field[] submittedAttributes, String className) {
        super(new AttributeFlagFactory(), modeledAttributes, submittedAttributes, className);
        this.className = className;
    }

    @Override
    public List<ImplementationFlag> getInspectionResult() {

        List<ImplementationFlag> flags = new ArrayList<>();
        for (AttributeStructure modelAttribute : modeledElements) {

            boolean isImplemented = true;
            boolean correctModifiers = true;
            boolean correctType = true;

            for (AttributeStructure submittedAttribute : submittedElements) {
                boolean totallyCorrect = modelAttribute.equals(submittedAttribute);
                if (totallyCorrect){
                    isImplemented = true;
                    flags.add(new CorrectlyImplementedAttribute(submittedAttribute.name()));
                    break;
                }

                isImplemented = modelAttribute.name().equals(submittedAttribute.name());

                if (isImplemented) {
                    correctModifiers = modelAttribute.modifier().equals(submittedAttribute.modifier());
                    correctType      = modelAttribute.type().equals(submittedAttribute.type());
                    break;
                }
            }

            if (!isImplemented) {
                flags.add(flagFactory.createMissingInScopeFlag(modelAttribute.name(), className));
            }

            if (!correctModifiers) {
                flags.add(flagFactory.createIncorrectModifierFlag(modelAttribute.name(), modelAttribute.modifier()));
            }

            if (!correctType) {
                flags.add(flagFactory.createIncorrectTypeFlag(modelAttribute.name(), modelAttribute.type()));
            }
        }

        return flags;
    }

    @Override
    protected AttributeStructure toStructure(Field field){
        String modifier = Modifier.toString(field.getModifiers());
        String type = StringUtils.nameWithoutExtension(field.getType().getName());
        String name = field.getName();

        return new AttributeStructure(modifier, type, name);
    }
}
