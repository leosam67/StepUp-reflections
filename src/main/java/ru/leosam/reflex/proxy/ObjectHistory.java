package ru.leosam.reflex.proxy;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class ObjectHistory {
    private final Map<Long, ObjectCall> history = new ConcurrentHashMap<>();

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

    public void clearHistory(double fullnessRate) {
        final long time = System.currentTimeMillis();
        final long elemCountBefore = history.size();
        final long oldElements = history.keySet().stream().filter(aLong -> aLong < time).count();
        if(oldElements == 0)
            return;
        if(((double) oldElements / (double) elemCountBefore) <= fullnessRate)
            // Rate of old elements less than rate fullnessRate
            return;
        for (Object key : history.keySet().stream().sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        }).filter(aLong -> aLong < time).toArray()) {
            Utils.timedPrintln("Removed from cache " + history.get((Long) key).toString());
            history.remove((Long) key);
        }
        final int elemCountAfter = history.size();
        if(elemCountBefore == elemCountAfter)
            Utils.timedPrintln("* No memory freed");
        else
            Utils.timedPrintln("* Freed " + (elemCountBefore - elemCountAfter) + " cache items");
    }
}
