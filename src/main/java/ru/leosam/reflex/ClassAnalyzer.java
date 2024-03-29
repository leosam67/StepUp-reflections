package ru.leosam.reflex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/*
К делу не относится, класс для "всё пощупать".
 */

public class ClassAnalyzer {
    public static void analyzeClass(Object o) {
        Class clazz = o.getClass();
        System.out.println("Class: " + clazz.getCanonicalName());
        System.out.println("Fields: " + Arrays.toString(clazz.getDeclaredFields()));
        System.out.println("Parent: " + clazz.getSuperclass().getCanonicalName());
        System.out.println("Declared methods: " + Arrays.toString(clazz.getDeclaredMethods()));
        System.out.println("Methods: " + Arrays.toString(clazz.getMethods()));
        System.out.println("Constructors: " + Arrays.toString(clazz.getConstructors()));
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.print(method.getName());
            if (method.isAnnotationPresent(Cache.class))
                System.out.print(" @Cache(" + method.getAnnotation(Cache.class).timeout() + ")");
            if (method.isAnnotationPresent(Mutator.class))
                System.out.print(" @Mutator");
            System.out.println();
        }
    }

    public static void analyzeMethod(Method method) {
        System.out.println("Method: " + method.getName());
        System.out.println("Annotations are:");
        for (Annotation annotation : method.getAnnotations()) {
            System.out.println("+" + annotation.toString());
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Fraction fr = new Fraction(4, 5);
        analyzeClass(fr);
        Class[] params = {String.class};
        analyzeMethod(fr.getClass().getMethod("doubleValue", params));
        Utils handler = new Utils(fr);
        Proxyable proxy = (Proxyable) Proxy.newProxyInstance(Proxyable.class.getClassLoader(),
                new Class[] {Proxyable.class},
                handler);
        analyzeClass(proxy);
        analyzeMethod(proxy.getClass().getMethod("doubleValue", params));
    }
}
