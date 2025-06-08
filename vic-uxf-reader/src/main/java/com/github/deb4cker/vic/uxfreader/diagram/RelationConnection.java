package com.github.deb4cker.vic.uxfreader.diagram;

import com.github.deb4cker.vic.uxfreader.parser.ClassPanel;
import com.github.deb4cker.vic.uxfreader.parser.Relation;
import com.github.deb4cker.vic.uxfreader.parser.RelationPanel;
import com.github.deb4cker.vic.commons.enums.RelationType;

public class RelationConnection {
    private ClassPanel source;
    private ClassPanel target;
    private final RelationPanel relationPanel;
    private final RelationType relationType;

    public RelationConnection(ClassPanel source, ClassPanel target, RelationPanel relationPanel) {
        this.source = source;
        this.target = target;
        this.relationPanel = relationPanel;

        this.relationType = loadRelationType();
    }

    public String getSourceName() {
        return source.getClassName();
    }

    public String getTargetName() {
        return target.getClassName();
    }

    public RelationType getRelationType() {
        return relationType;
    }

    private RelationType loadRelationType(){
        String lt = relationPanel.getLt();

        boolean isInheritance =
                 (lt.startsWith("<<-") || lt.startsWith("->>"))
                                       &&
                !(lt.contains("<<<<-") || lt.contains("->>>>"));

        if (isInheritance){
            if (lt.startsWith("<<-")) swapConnection();
            return RelationType.INHERITANCE;
        }

        String m1 = relationPanel.getM1();
        String m2 = relationPanel.getM2();

        if (m1 != null){
            if(m2 == null) swapConnection();
            if (m1.contains("1")) return RelationType.TO_ONE_RELATION;
            if (m1.contains("*") || m1.contains("n")) return RelationType.TO_MANY_RELATION;
        }

        if (m2 != null) {
            if (m2.contains("1")) return RelationType.TO_ONE_RELATION;
            if (m2.contains("*") || m2.contains("n")) return RelationType.TO_MANY_RELATION;
        }

        return RelationType.TO_MANY_RELATION;
    }

    public void loadRelationInPanels(){
        switch (relationType) {
            case TO_ONE_RELATION ->{
                source.addTarget(new Relation(getTargetName(), RelationType.TO_ONE_RELATION));
                target.addSource(new Relation(getSourceName(), RelationType.TO_ONE_RELATION));
            }
            case TO_MANY_RELATION -> {
                source.addTarget(new Relation(getTargetName(), RelationType.TO_MANY_RELATION));
                target.addSource(new Relation(getSourceName(), RelationType.TO_MANY_RELATION));
            }
            case INHERITANCE -> {
                source.addTarget(new Relation(getTargetName(), RelationType.INHERITANCE));
                target.addSource(new Relation(getSourceName(), RelationType.INHERITANCE));
            }
        }
    }

    private void swapConnection(){
        ClassPanel temp = source;
        source = target;
        target = temp;
    }

    @Override
    public String toString() {
        return "RelationConnection{\n" +
                "source=" + source +
                "\n, target=" + target +
                "\n, relationPanel=" + relationPanel +
                "\n}";
    }
}
