package com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.relationship;

import com.github.deb4cker.vic.evaluator.models.relations.RelationshipData;
import com.github.deb4cker.vic.evaluator.implementationFlags.inconsistency.ImplementationInconsistency;

import static com.github.deb4cker.vic.evaluator.constants.InconsistencyMessages.RELATIONSHIP_NOT_IMPLEMENTED;

public class RelationshipNotImplementedInconsistency extends ImplementationInconsistency {
    public RelationshipNotImplementedInconsistency(RelationshipData relationshipData) {
        super(RELATIONSHIP_NOT_IMPLEMENTED, relationshipData.toString());
    }
}
