package ru.leosam.reflex.proxy;

import lombok.Getter;
import java.lang.reflect.Method;

@Getter
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
}
