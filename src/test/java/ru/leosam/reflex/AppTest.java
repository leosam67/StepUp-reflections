package ru.leosam.reflex;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Proxy;

public class AppTest
{
    private static final String LOG_FILE_NAME = "C:/Temp/test.log";
    private static final PrintStream ORIGINAL_SYSTEM_OUT = System.out;
    @BeforeAll
    public static void clearLog() throws FileNotFoundException {
        File file = new File(LOG_FILE_NAME);
        if(file.exists()) file.delete();
        System.setOut(new PrintStream(file));
    }
    public void checkLogFile(int cntMustBeInvoked, int cntMustBeSkipped) throws IOException {
        final int TIMESTAMP_LENGTH = 24;
        int invokedCnt = 0;
        int skippedCnt = 0;
        System.out.close();
        System.setOut(ORIGINAL_SYSTEM_OUT);
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE_NAME))) {
            String line = br.readLine();;
            while (line != null) {
                if (line.startsWith(Utils.MSG_INVOKED, TIMESTAMP_LENGTH)) invokedCnt++;
                if (line.startsWith(Utils.MSG_INVOKE_SKIPPED, TIMESTAMP_LENGTH)) skippedCnt++;
                line = br.readLine();
            }
        }
        System.out.println("Invoked: " + invokedCnt);
        Assertions.assertEquals(cntMustBeInvoked, invokedCnt);
        System.out.println("Invoke skipped: " + skippedCnt);
        Assertions.assertEquals(cntMustBeSkipped, skippedCnt);
    }
    @Test
    @SneakyThrows
    public void testCachedProxy() {
        Fraction fr = new Fraction(2, 3);
        Utils handler = new Utils(fr);
        Proxyable proxy = (Proxyable) Proxy.newProxyInstance(Proxyable.class.getClassLoader(),
                new Class[] {Proxyable.class},
                handler);
        proxy.doubleValue("A");
        proxy.doubleValue("B");
        proxy.doubleValue("C");
        proxy.setNum(5);
        proxy.doubleValue("D");
        proxy.doubleValue("E");

        handler.clearHistory("(from test application)");
        proxy.doubleValue("F");

        checkLogFile(4, 3);
    }
    @Test
    @SneakyThrows
    public void testTimedCacheProxy() {
        Fraction fr = new Fraction(1, 5);
        Utils handler = new Utils(fr);
        handler.startGarbageCollector(500L);
        Proxyable proxy = (Proxyable) Proxy.newProxyInstance(Proxyable.class.getClassLoader(),
                new Class[] {Proxyable.class},
                handler);
        proxy.doubleValue("A"); Thread.sleep(300L); // Invoked 1
        proxy.doubleValue("B"); Thread.sleep(300L); // Skipped 1
        proxy.doubleValue("C"); Thread.sleep(300L); // Skipped 2
        proxy.setNum(5);    // Invoked 2
        proxy.doubleValue("D"); Thread.sleep(300L); // Invoked 3 due to garbage collector
        proxy.setNum(1);    // Invoked 4
        proxy.doubleValue("E"); Thread.sleep(600L); // Skipped 3
        proxy.setNum(5);    // Invoked 5
        Thread.sleep(1500L);
        proxy.doubleValue("F"); // Invoked 6 due to garbage collector

        handler.stopGarbageCollector();
        checkLogFile(6, 3);
    }
}
