package ru.leosam.reflex;

import java.lang.reflect.Method;

public class ObjectCall {
    private final long finishTime;
    private final String objectState;
    private final Method method;
    private final Object result;

    public ObjectCall(String objectState, Method method, Object result, long timeout) {
        this.finishTime = System.currentTimeMillis() + timeout;
        this.objectState = objectState;
        this.method = method;
        this.result = result;
    }
    public long getFinishTime() {
        return finishTime;
    }

    public String getObjectState() {
        return objectState;
    }

    public Method getMethod() {
        return method;
    }

    public Object getResult() {
        return result;
    }
}
