package com.mdt.common.shared.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ClassUtils {

    public static Set<Class<?>> getAllParents(Class<?> type) {
        Set<Class<?>> result = new HashSet<>();
        getAllParentRecursive(type, result);
        return result;
    }

    private static void getAllParentRecursive(Class<?> cls, Set<Class<?>> out) {
        if (cls == null || cls == Object.class) return;
        out.add(cls);

        getAllParentRecursive(cls.getSuperclass(), out);
        for (var interfaceClass : cls.getInterfaces()) getAllParentRecursive(interfaceClass, out);
    }
}
