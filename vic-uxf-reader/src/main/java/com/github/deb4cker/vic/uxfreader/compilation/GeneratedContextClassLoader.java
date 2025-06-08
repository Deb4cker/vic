package com.github.deb4cker.vic.uxfreader.compilation;

import com.github.deb4cker.vic.uxfreader.commons.interfaces.IdentifiedClassLoader;
import com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GeneratedContextClassLoader extends ClassLoader implements IdentifiedClassLoader {

    private final ClassLoaderContext context;
    private final Map<String, byte[]> byteCodeMap = new ConcurrentHashMap<>();

    public GeneratedContextClassLoader(ClassLoaderContext contextName) {
        super(GeneratedContextClassLoader.class.getClassLoader());
        this.context = contextName;
    }

    @Override
    public ClassLoaderContext getContext() {
        return context;
    }

    public String getContextName() {
        return context.name();
    }

    public void addClassBytes(String className, byte[] classData) {
        byteCodeMap.put(className, classData);
    }

    public Class<?> loadClassFromBytes(byte[] classData, String className) {
        addClassBytes(className, classData);
        return defineClass(className, classData, 0, classData.length);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (byteCodeMap.containsKey(name)) {
            byte[] bytes = byteCodeMap.get(name);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }
}