package ru.leosam.reflex;

public class FreeMemory implements Runnable {
    private long interval;
    private Utils handler;

    public FreeMemory(Utils han, long interval) {
        if(han == null)
            throw new IllegalArgumentException("Creation of GC with no cache");
        handler = han;
        this.interval = interval;
        (new Thread(this)).start();
    }

    public void stop() {
        interval = 0L;
    }

    @Override
    public void run() {
        while (interval > 0L) {
            handler.clearHistory("(from GC)");
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
