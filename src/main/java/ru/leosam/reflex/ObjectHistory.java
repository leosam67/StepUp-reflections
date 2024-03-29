package ru.leosam.reflex;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class ObjectHistory {
    private final Map<Long, ObjectCall> history = new HashMap<>();

    public void put(ObjectCall call) {
        history.put(call.getFinishTime(), call);
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

    public void clearHistory() {
        final long time = System.currentTimeMillis();
        final int elemCountBefore = history.size();
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
            Utils.timedPrintln("Removed from cache " + history.get((Long) key).toString());
            history.remove((Long) key);
        }
        int elemCountAfter = history.size();
        if(elemCountBefore == elemCountAfter)
            Utils.timedPrintln("* No memory freed");
        else
            Utils.timedPrintln("* Freed " + (elemCountBefore - elemCountAfter) + " cache items");
    }
}
