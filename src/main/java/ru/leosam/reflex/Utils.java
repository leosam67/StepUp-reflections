package ru.leosam.reflex;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils implements InvocationHandler {
    public static final String MSG_INVOKE_SKIPPED = "REAL INVOKE IS SKIPPED ";
    public final static String MSG_INVOKED = "INVOKED METHOD ";
    public final static String MSG_HISTORY_CLEARED = "HISTORY CLEARED ";
    private final static ObjectHistory cache = new ObjectHistory();
    private final Proxyable source;
    private static FreeMemory fm;
    private final static SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");

    public Utils(Proxyable proxyable) {
        source = proxyable;
        timedPrintln("Proxy installed for " + proxyable.getClass().getName());
    }

    public void startGarbageCollector(long interval) {
        if (fm == null) {
            fm = new FreeMemory(this, interval);
            timedPrintln("Garbage collector is started");
        } else timedPrintln("Garbage collector is already started");
    }

    public void stopGarbageCollector() {
        if (fm != null) fm.stop();
        fm = null;
    }

    public static void timedPrint(String msg) {
        System.out.print(utc.format(new Date()) + msg);
    }
    public static void timedPrintln(String msg) {
        timedPrint(msg);
        System.out.println();
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
        timedPrint("Proxy invoked " + method.getDeclaringClass().getName() + "." + method.getName() + "(" + allArgs + ") annotated by ");
        for (Annotation annotation : method.getAnnotations()) {
            System.out.print(annotation.toString() + " ");
        }
        System.out.println();
        if (method.isAnnotationPresent(Mutator.class)) {
            timedPrintln("Method is a mutator. Do not clear the cache");
        }
        String state = ObjectState.calcState(source);
        Object result = cache.get(state, method);
        if (result != null) {
            timedPrintln(MSG_INVOKE_SKIPPED + method.getName() + '(' + allArgs + ") with state " + state);
        } else {
            result = method.invoke(source, args);
            state = ObjectState.calcState(source);
            timedPrintln(MSG_INVOKED + method.getName() + '(' + allArgs + ") for state " + state);
            if (method.isAnnotationPresent(Cache.class)) {
                timedPrintln("Cached after " + method.getName());
                cache.put(new ObjectCall(ObjectState.calcState(source), method, result, method.getAnnotation(Cache.class).timeout()));
            }
        }
        System.out.println();
        return result;
    }

    public void clearHistory(String msg) {
        timedPrintln(MSG_HISTORY_CLEARED + msg);
        cache.clearHistory();
        System.out.println();
    }
}
