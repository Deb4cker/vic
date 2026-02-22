package com.github.deb4cker.vic.evaluator.models;

import com.github.deb4cker.vic.evaluator.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassData {
    private final String className;
    private final Field[] fields;
    private final Method[] methods;
    private final Constructor<?>[] constructors;
    private final Class<?> superClass;
    private final String classSimpleName;

    public ClassData(Class<?> clazz) {
        className = clazz.getName();
        fields = clazz.getDeclaredFields();
        methods = clazz.getDeclaredMethods();
        constructors = ReflectionUtils.getDeclaredConstructorsExcludingDefault(clazz);
        superClass = clazz.getSuperclass();
        classSimpleName = clazz.getSimpleName();
    }

    public String getClassName() {
        return className;
    }
    public String getClassSimpleName() {
        return classSimpleName;
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
    }

    public Constructor<?>[] getConstructors() {
        return constructors;
    }

    public int getNumberOfSections() {
        return fields.length + methods.length + constructors.length;
    }

    public Class<?> getSuperClass() {
        return superClass;
    }

    @Override
    public String toString() {
        return className;
    }
}
