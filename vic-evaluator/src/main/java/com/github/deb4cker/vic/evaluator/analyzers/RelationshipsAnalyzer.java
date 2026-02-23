package com.github.deb4cker.vic.evaluator.analyzers;

import com.github.deb4cker.vic.evaluator.models.relations.RelationshipData;
import com.github.deb4cker.vic.evaluator.implementation_flags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementation_flags.correct_implementation.AllRelationshipsCorrectlyImplemented;
import com.github.deb4cker.vic.evaluator.implementation_flags.correct_implementation.CorrectlyImplementedRelationship;
import com.github.deb4cker.vic.evaluator.implementation_flags.inconsistency.relationship.RelationshipNotImplementedInconsistency;

import java.util.ArrayList;
import java.util.List;

public class RelationshipsAnalyzer {

    private final List<RelationshipData> modeledRelationships;
    private final List<RelationshipData> submittedRelationships;
    private final int relationshipCount;

    public RelationshipsAnalyzer(List<RelationshipData> modeledRelationships, List<RelationshipData> submittedRelationships) {
        this.modeledRelationships = modeledRelationships;
        this.submittedRelationships = submittedRelationships;
        this.relationshipCount = modeledRelationships.size();
    }

    public List<ImplementationFlag> analyze() {
        List<ImplementationFlag> flags = new ArrayList<>();

        for (RelationshipData relationshipData : modeledRelationships) {
            boolean isImplemented = false;
            for (RelationshipData submittedRelationship : submittedRelationships) {
                if (relationshipData.equals(submittedRelationship)) {
                    isImplemented = true;
                    break;
                }
            }

            if (!isImplemented){
                flags.add(new RelationshipNotImplementedInconsistency(relationshipData));
                continue;
            }

            flags.add(new CorrectlyImplementedRelationship(relationshipData));
        }

        boolean onlyCorrectFlags = ImplementationFlag.onlyCorrectFlagsIn(flags);
        if (onlyCorrectFlags){
            flags.add(new AllRelationshipsCorrectlyImplemented());
        }

        return flags;
    }

    public int getRelationshipCount() {
        return relationshipCount;
    }
}
