package ru.leosam.reflex;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Utils implements InvocationHandler {
    public static final String MSG_INVOKE_SKIPPED = "REAL INVOKE IS SKIPPED ";
    public final static String MSG_INVOKED = "INVOKED METHOD ";
    public final static String MSG_HISTORY_CLEARED = "HISTORY CLEARED ";
    private final static ObjectHistory cache = new ObjectHistory();
    private final Proxyable source;

    public Utils(Proxyable proxyable) {
        source = proxyable;
        System.out.println("Proxy installed for " + proxyable.getClass().getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder allArgs = new StringBuilder();
        if (args != null) {
            for (int k = 0; k < args.length; k++) {
                if (k > 0) allArgs.append(", ");
                allArgs.append(args[k]);
            }
        }
        System.out.print("Proxy invoked "
                + method.getDeclaringClass().getName() + "."
                + method.getName()
                + "(" + allArgs // args[0]
                + ") annotated by ");
        for (Annotation annotation : method.getAnnotations()) {
            System.out.print(annotation.toString() + " ");
        }
        System.out.println();
        if (method.isAnnotationPresent(Mutator.class)) {
            System.out.println("Method is a mutator. Do not clear the cache");
        }
        String state = ObjectState.calcState(source);
        Object result = cache.get(state, method);
        if (result != null) {
            System.out.println(MSG_INVOKE_SKIPPED + method.getName() + '(' + allArgs + ") with state " + state);
        } else {
            result = method.invoke(source, args);
            state = ObjectState.calcState(source);
            System.out.println(MSG_INVOKED + method.getName() + '(' + allArgs + ") for state " + state);
            if (method.isAnnotationPresent(Cache.class)) {
                System.out.println("Cached after " + method.getName());
                cache.put(new ObjectCall(ObjectState.calcState(source), method, result));
            }
        }
        System.out.println();
        return result;
    }
    public void clearHistory(Long timeout) {
        System.out.println(MSG_HISTORY_CLEARED);
        System.out.println();
        cache.clearHistory(timeout);
    }
}
