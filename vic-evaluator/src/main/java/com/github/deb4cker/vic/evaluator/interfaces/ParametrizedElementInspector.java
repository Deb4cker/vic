package com.github.deb4cker.vic.evaluator.interfaces;

import com.github.deb4cker.vic.evaluator.inspectors.structures.ConstructorStructure;
import com.github.deb4cker.vic.evaluator.inspectors.structures.MethodStructure;

import java.util.*;

public interface ParametrizedElementInspector {
   default boolean haveSameParameters(ParametrizedStructure structure1, ParametrizedStructure structure2){
       if (structure1.parameters().length != structure2.parameters().length) return false;

       List<String> c1Types = Arrays.stream(structure1.parameters())
               .map(param -> param.getType().getTypeName())
               .toList();

       List<String> c2Types = Arrays.stream(structure2.parameters())
               .map(param -> param.getType().getTypeName())
               .toList();

       return c1Types.equals(c2Types);
   }

   default <T extends ParametrizedStructure> List<List<T>> trimOverloads(List<T> structures) {
       Map<String, List<T>> groups = new HashMap<>();

       for (T method : structures)
           groups.computeIfAbsent(method.name(), name -> new ArrayList<>()).add(method);

       List<T> noOverloads = new ArrayList<>();
       List<List<T>> overloadGroups = new ArrayList<>();

       for (List<T> group : groups.values()) {
           if (group.size() == 1) {
               noOverloads.add(group.get(0));
               continue;
           }
           overloadGroups.add(group);
       }

       return List.of(noOverloads, overloadGroups.stream().flatMap(List::stream).toList());
   }

   default boolean methodHasSameSignature(MethodStructure model, MethodStructure submitted){
        return model.name().equals(submitted.name())
                && Arrays.equals(model.getParametersType(model.parameters()), submitted.getParametersType(submitted.parameters()));
   }

    default boolean constructorHasSameSignature(ConstructorStructure model, ConstructorStructure submitted){
        return model.name().equals(submitted.name())
                && Arrays.equals(model.getParametersType(model.parameters()), submitted.getParametersType(submitted.parameters()));
    }

}
