package ru.leosam.reflex;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class AppTest
{
    @Test
    public void testProxy() {
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
    }
}
