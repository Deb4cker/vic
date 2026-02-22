package com.github.deb4cker.vic.evaluator.inspectors;

import com.github.deb4cker.vic.commons.EvaluationBypassCode;
import com.github.deb4cker.vic.evaluator.implementationflags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationflags.correctImplementation.CorrectlyImplementedAttribute;
import com.github.deb4cker.vic.evaluator.implementationflags.factory.impl.AttributeFlagFactory;
import com.github.deb4cker.vic.evaluator.inspectors.abstracts.AbstractInspector;
import com.github.deb4cker.vic.evaluator.inspectors.structures.AttributeStructure;
import com.github.deb4cker.vic.evaluator.inspectors.support.ElementMatcher;
import com.github.deb4cker.vic.commons.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttributeInspector extends AbstractInspector<Field, AttributeStructure> {
    private final String className;

    public AttributeInspector(Field[] modeledAttributes, Field[] submittedAttributes, String className) {
        super(new AttributeFlagFactory(), modeledAttributes, submittedAttributes, className);
        this.className = className;
    }

    @Override
    public List<ImplementationFlag> getInspectionResult() {
        List<ImplementationFlag> flags = new ArrayList<>();
        modeledElements.stream()
                .filter(a -> !a.name().contains(EvaluationBypassCode.CODE))
                .forEach(model -> inspectAttribute(model, flags));
        return flags;
    }

    private void inspectAttribute(AttributeStructure model, List<ImplementationFlag> flags) {
        Optional<AttributeStructure> exact = ElementMatcher.findExact(model, submittedElements);
        if (exact.isPresent()) {
            flags.add(new CorrectlyImplementedAttribute(exact.get().name()));
            return;
        }

        Optional<AttributeStructure> byName = ElementMatcher.findByName(model.name(), submittedElements,
                AttributeStructure::name);
        if (byName.isEmpty()) {
            flags.add(flagFactory.createMissingInScopeFlag(model.name(), className));
            return;
        }

        AttributeStructure found = byName.get();
        if (!model.modifier().equals(found.modifier()))
            flags.add(flagFactory.createIncorrectModifierFlag(model.name(), model.modifier()));
        if (!model.type().equals(found.type()))
            flags.add(flagFactory.createIncorrectTypeFlag(model.name(), model.type()));
    }

    @Override
    protected AttributeStructure toStructure(Field field) {
        String modifier = Modifier.toString(field.getModifiers());
        String type = StringUtils.nameWithoutExtension(field.getType().getName());
        String name = field.getName();
        return new AttributeStructure(modifier, type, name);
    }
}
