package ru.leosam.reflex;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Utils implements InvocationHandler {
    private final static HashMap<Object, Object> cache = new HashMap();
    private final Proxyable source;

    public Utils(Proxyable proxyable) {
        source = proxyable;
        System.out.println("Proxy installed for " + proxyable.getClass().getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print("Proxy invoked "
                + method.getDeclaringClass().getName() + "."
                + method.getName()
                + "(" + args[0]
                + ") annotated by ");
        for(Annotation annotation : method.getAnnotations()) {
            System.out.print(annotation.toString() + " ");
        }
        System.out.println();
        if(method.isAnnotationPresent(Mutator.class)) {
            System.out.println("Method is a mutator");
            System.out.println("Clearing cache before " + method.getName());
            cache.remove(source);
        }
        Object result;
        if(cache.containsKey(source)) {
            System.out.println("REAL INVOKE IS SKIPPED");
            result = cache.get(source);
        } else {
            result = method.invoke(source, args);
        }
        if(method.isAnnotationPresent(Cache.class)) {
            System.out.println("Cached after " + method.getName());
            cache.put(source, result);
        }
        System.out.println();
        return result;
    }
}
