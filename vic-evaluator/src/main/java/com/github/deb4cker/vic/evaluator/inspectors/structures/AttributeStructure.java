package com.github.deb4cker.vic.evaluator.inspectors.structures;

import com.github.deb4cker.vic.evaluator.interfaces.ElementStructure;

import java.util.Objects;

public record AttributeStructure(String modifier, String type, String name) implements ElementStructure {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttributeStructure that = (AttributeStructure) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name) && Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifier, type, name);
    }
}
