package com.github.deb4cker.vic.evaluator.inspectors.structures;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ElementStructure;
import com.github.deb4cker.vic.evaluator.commons.interfaces.ParametrizedStructure;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

public record ConstructorStructure(String modifier, String name, Parameter[] parameters) implements ElementStructure, ParametrizedStructure, Comparable<ConstructorStructure> {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorStructure that = (ConstructorStructure) o;
        return Objects.equals(modifier, that.modifier) && Objects.equals(name, that.name) && Objects.deepEquals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifier, name, Arrays.hashCode(parameters));
    }

    @Override
    public int compareTo(ConstructorStructure other) {
        int parameterCountComparison = Integer.compare(other.parameters.length, this.parameters.length);
        if (parameterCountComparison != 0) {
            return parameterCountComparison;
        }

        for (int i = 0; i < this.parameters.length; i++) {
            String thisType = this.parameters[i].getType().getTypeName();
            String otherType = other.parameters[i].getType().getTypeName();
            int typeComparison = thisType.compareTo(otherType);
            if (typeComparison != 0) {
                return typeComparison;
            }
        }

        return 0;
    }
}
