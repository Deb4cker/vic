package com.github.deb4cker.vic.uxfreader.commons.interfaces;

import com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext;

public interface IdentifiedClassLoader {
    ClassLoaderContext getContext();
    Class<?> loadClassFromBytes(byte[] classData, String className);
}
