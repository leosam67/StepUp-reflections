package ru.leosam.reflex;

import java.lang.reflect.Method;

public class ObjectCall {
    private final long time;
    private final String objectState;
    private final Method method;
    private final Object result;

    public ObjectCall(String objectState, Method method, Object result) {
        this.time = System.currentTimeMillis();
        this.objectState = objectState;
        this.method = method;
        this.result = result;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ObjectCall)) return false;

        ObjectCall that = (ObjectCall) object;

        if (time != that.time) return false;
        if (!objectState.equals(that.objectState)) return false;
        if (!method.equals(that.method)) return false;
        return result.equals(that.result);
    }

    @Override
    public int hashCode() {
        int result1 = (int) (time ^ (time >>> 32));
        result1 = 31 * result1 + objectState.hashCode();
        result1 = 31 * result1 + method.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    public long getTime() {
        return time;
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
