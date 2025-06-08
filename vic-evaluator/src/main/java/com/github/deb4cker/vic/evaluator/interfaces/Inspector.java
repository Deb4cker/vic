package com.github.deb4cker.vic.evaluator.interfaces;


import com.github.deb4cker.vic.evaluator.implementationFlags.ImplementationFlag;

import java.util.List;

public interface Inspector {
    List<ImplementationFlag> inspect() throws InterruptedException;
}
