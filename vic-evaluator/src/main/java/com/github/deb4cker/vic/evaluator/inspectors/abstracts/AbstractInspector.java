package com.github.deb4cker.vic.evaluator.inspectors.abstracts;

import com.github.deb4cker.vic.evaluator.commons.interfaces.ElementStructure;
import com.github.deb4cker.vic.evaluator.commons.interfaces.Inspector;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;
import com.github.deb4cker.vic.evaluator.implementationFlags.factory.interfaces.InconsistencyFlagFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractInspector<T, E extends ElementStructure> implements Inspector {

    protected final InconsistencyFlagFactory flagFactory;
    protected List<E> modeledElements;
    protected List<E> submittedElements;
    protected final String scopeName;

    public AbstractInspector(InconsistencyFlagFactory flagFactory, T[] modeledElements, T[] subModeledElements, String scopeName) {
        this.flagFactory = flagFactory;
        this.modeledElements = allToStructure(modeledElements);
        this.submittedElements = allToStructure(subModeledElements);
        this.scopeName = scopeName;
    }

    @Override
    public List<ImplementationFlag> inspect() throws InterruptedException {
        return getInspectionResult();
    }

    public ArrayList<E> allToStructure(T[] elements) {
        return new ArrayList<>(Arrays.stream(elements)
                .map(this::toStructure)
                .toList());
    }

    public void setModeledElements(List<E> modeledElements) {
        this.modeledElements = modeledElements;
    }

    public void setSubmittedElements(List<E> submittedElements) {
        this.submittedElements = submittedElements;
    }

    protected abstract List<ImplementationFlag> getInspectionResult() throws InterruptedException;

    protected abstract E toStructure(T element);

    protected List<ImplementationFlag> checkOverloads(List<E> modeledElements, List<E> submittedElements) throws InterruptedException {
        return List.of();
    }
}
