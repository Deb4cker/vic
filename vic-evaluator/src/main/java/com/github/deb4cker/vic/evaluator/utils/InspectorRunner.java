package com.github.deb4cker.vic.evaluator.utils;

import com.github.deb4cker.vic.evaluator.commons.interfaces.Inspector;
import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;

import java.util.ArrayList;
import java.util.List;

public class InspectorRunner extends Thread {

    private final Inspector inspector;
    private final List<ImplementationFlag> flags = new ArrayList<>();

    public InspectorRunner(Inspector inspector) {
        this.inspector = inspector;
    }

    @Override
    public void run() {
        try {
            flags.addAll(inspector.inspect());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        flags.sort(ImplementationFlag::compareTo);
    }

    public List<ImplementationFlag> getFlags() {
        return flags;
    }
}
