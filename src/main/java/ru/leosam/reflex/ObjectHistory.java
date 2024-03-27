package ru.leosam.reflex;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class ObjectHistory {
    public static final long DO_NOT_CLEAR_HISTORY = 0L;
    public static final long CLEAR_ALL_HISTORY = -1L;
    private final Map<Long, ObjectCall> history = new HashMap<>();

    public void put(ObjectCall call) {
        history.put(call.getTime(), call);
    }

    public Object get(String state, Method method) {
        Optional<ObjectCall> cachedCall =
                history.values().stream().filter(new Predicate<ObjectCall>() {
                    @Override
                    public boolean test(ObjectCall objectCall) {
                        return state.equals(objectCall.getObjectState())
                                && method.equals(objectCall.getMethod());
                    }
                }).findFirst();
        return cachedCall.map(ObjectCall::getResult).orElse(null);
    }

    public void clearHistory(Long timeout) {
        if(timeout == DO_NOT_CLEAR_HISTORY) return;
        if(timeout == CLEAR_ALL_HISTORY) {
            history.clear();
            return;
        }
        final long time = System.currentTimeMillis() - timeout;
        for (Object key : history.keySet().stream().sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        }).filter(new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) {
                return aLong < time;
            }
        }).toArray()) {
            System.out.println("Removed from cache " + history.get((Long) key).toString());
            history.remove((Long) key);
        }
    }
}