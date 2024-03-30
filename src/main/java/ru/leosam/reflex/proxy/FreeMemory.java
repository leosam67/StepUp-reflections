package ru.leosam.reflex.proxy;

import java.util.stream.Stream;

public class FreeMemory implements Runnable {
    private long cleanInterval;
    private final Utils handler;
    private final double fullnessRate;

    public FreeMemory(Utils handler, long cleanInterval, double fullnessRate) {
        if(handler == null)
            throw new IllegalArgumentException("Creation of GC with no cache");
        this.handler = handler;
        this.cleanInterval = cleanInterval;
        this.fullnessRate = fullnessRate;
        (new Thread(this)).start();
    }

    public void stop() {
        cleanInterval = 0L;
    }

    @Override
    public void run() {
        while (cleanInterval > 0L) {
            handler.clearHistory(fullnessRate, "(from GC)");
            try {
                Thread.sleep(cleanInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
