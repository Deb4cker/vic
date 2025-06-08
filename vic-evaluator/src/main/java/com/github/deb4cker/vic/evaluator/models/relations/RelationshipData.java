package com.github.deb4cker.vic.evaluator.models.relations;

import com.github.deb4cker.vic.commons.enums.RelationType;

public class RelationshipData {
    private final RelationshipSide aClassSide;
    private final RelationshipSide bClassSide;
    private final RelationType type;

    public RelationshipData(RelationshipSide aClassSide, RelationshipSide bClassSide, RelationType type) {
        this.aClassSide = aClassSide;
        this.bClassSide = bClassSide;
        this.type = type;
    }

    @Override
    public String toString() {
        if(type == RelationType.INHERITANCE){
            return String.format("%s herda de %s", aClassSide, bClassSide);
        }

        return String.format("%s para %s", aClassSide, bClassSide);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RelationshipData other = (RelationshipData) obj;
        return this.toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
