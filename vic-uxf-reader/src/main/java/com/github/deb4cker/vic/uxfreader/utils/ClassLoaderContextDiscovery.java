package com.github.deb4cker.vic.uxfreader.utils;

import com.github.deb4cker.vic.uxfreader.commons.interfaces.IdentifiedClassLoader;
import com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext;

public class ClassLoaderContextDiscovery {

    public static ClassLoaderContext findOf(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader instanceof IdentifiedClassLoader identifiedLoader) {
            return identifiedLoader.getContext();
        }
        return ClassLoaderContext.SYSTEM;
    }
}
