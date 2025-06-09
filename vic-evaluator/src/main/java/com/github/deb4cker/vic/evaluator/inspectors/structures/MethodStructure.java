package com.github.deb4cker.vic.evaluator.inspectors.structures;


import com.github.deb4cker.vic.evaluator.commons.interfaces.ElementStructure;
import com.github.deb4cker.vic.evaluator.commons.interfaces.ParametrizedStructure;

import java.lang.reflect.Parameter;
import java.util.Objects;

public record MethodStructure(String modifier, String name, String returnType, Parameter[] parameters) implements ElementStructure, ParametrizedStructure {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MethodStructure that = (MethodStructure) o;
        return Objects.equals(name, that.name) && Objects.equals(modifier, that.modifier) && Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifier, name, returnType);
    }
}
