package org.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassInfoPrinter {

    private ClassInfoPrinter() {}
    public static void printClassInfo(Class<?> inspectedClass) {
        System.out.println("Class info:");
        System.out.println("name " + inspectedClass.getSimpleName());
        System.out.println("package " + inspectedClass.getPackageName());
        System.out.println("modifiers " + Modifier.toString(inspectedClass.getModifiers()));
        System.out.println("superclass " + inspectedClass.getSuperclass().getSimpleName());
        System.out.println("implemented interfaces:");
        for (Class<?> interfaceClass : inspectedClass.getInterfaces()) {
            System.out.println(interfaceClass.getName());
        }
        System.out.println("fields:");
        for (Field field : inspectedClass.getFields()) {
            System.out.println(field.getName());
        }
        System.out.println("number of constructors = " + inspectedClass.getConstructors().length);
        System.out.println("methods:");
        for (Method method : inspectedClass.getMethods()) {
            System.out.println(method.getName());
        }
        System.out.println("annotations:");
        for (Annotation annotation : inspectedClass.getAnnotations()) {
            System.out.println(annotation.toString());
        }
    }
}
