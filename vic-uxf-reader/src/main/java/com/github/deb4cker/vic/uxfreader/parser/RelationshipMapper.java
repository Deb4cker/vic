package com.github.deb4cker.vic.uxfreader.parser;

import com.github.deb4cker.vic.evaluator.models.relations.*;
import com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext;
import com.github.deb4cker.vic.uxfreader.utils.ClassLoaderContextDiscovery;
import com.github.deb4cker.vic.commons.enums.RelationType;
import com.github.deb4cker.vic.evaluator.models.ClassData;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext.SYSTEM;

public class RelationshipMapper {

    public static List<RelationshipData> map(List<ClassData> classDataList) {

        List<RelationshipData> relationshipDataList = new ArrayList<>();

        for (ClassData classData : classDataList) {

            Class<?> superClass = classData.getSuperClass();
            ClassLoaderContext superClassContext = ClassLoaderContextDiscovery.findOf(superClass);

            boolean probablyInheritance = superClassContext != SYSTEM && superClass != Object.class;
            if (probablyInheritance) {
                RelationshipData relationshipData = new RelationshipData(new SubClass(classData), new SuperClass(new ClassData(superClass)), RelationType.INHERITANCE);
                relationshipDataList.add(relationshipData);
            }

            Field[] fields = classData.getFields();

            for (Field field : fields) {
                Class<?> clazz = field.getType();
                if (!isObjectType(clazz)) continue;

                boolean isMany = false;
                if(isCollectionOrArrayOrMap(clazz)) {
                    clazz = getTypeFromCollection(field);
                    isMany = true;
                }

                if(clazz == null) continue;

                ClassLoaderContext context = ClassLoaderContextDiscovery.findOf(clazz);
                if (context == SYSTEM) continue;

                relationshipDataList.add(getCardinality(classData, clazz, isMany));
            }
        }

        return relationshipDataList;
    }

    private static RelationshipData getCardinality(ClassData classData, Class<?> clazz, boolean isMany) {
        RelationshipSide thisSide = new One(classData);
        return isMany
                ? new RelationshipData(thisSide, new Many(new ClassData(clazz)), RelationType.TO_MANY_RELATION)
                : new RelationshipData(thisSide, new One (new ClassData(clazz)), RelationType.TO_ONE_RELATION );
    }

    private static boolean isObjectType(Class<?> type) {
        return !type.isPrimitive() && !type.isEnum() && !type.equals(String.class);
    }

    private static boolean isCollectionOrArrayOrMap(Class<?> clazz) {
        return clazz.isArray() || isCollection(clazz) || isMap(clazz);
    }

    public static Class<?> getTypeFromCollection(Field field) {
        Class<?> rawType = field.getType();

        if (rawType.isArray()) return rawType.getComponentType();

        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType pt) {
            Type[] typeArguments = pt.getActualTypeArguments();

            if (isCollection(rawType)) {
                if (typeArguments.length == 1 && typeArguments[0] instanceof Class<?>) {
                    return (Class<?>) typeArguments[0];
                }
            }

            if (isMap(rawType)) {
                if (typeArguments.length == 2 && typeArguments[1] instanceof Class<?>) {
                    return (Class<?>) typeArguments[1];
                }
            }
        }

        return null;
    }

    private static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
