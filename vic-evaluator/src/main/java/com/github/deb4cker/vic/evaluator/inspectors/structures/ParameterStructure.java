package com.github.deb4cker.vic.evaluator.inspectors.structures;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ElementStructure;

import java.util.Objects;

public record ParameterStructure (String type, String name) implements ElementStructure {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParameterStructure that = (ParameterStructure) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
