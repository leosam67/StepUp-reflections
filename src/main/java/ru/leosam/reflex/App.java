package ru.leosam.reflex;

import java.lang.reflect.Proxy;

public class App
{
    public static void main( String[] args )
    {
        Fraction fr = new Fraction(2, 3);
        Utils handler = new Utils(fr);
        Proxyable proxyable = (Proxyable) Proxy.newProxyInstance(Proxyable.class.getClassLoader(),
                new Class[] {Proxyable.class},
                handler);
        proxyable.doubleValue("A");
        proxyable.doubleValue("B");
        proxyable.doubleValue("C");
        proxyable.setNum(5);
        proxyable.doubleValue("D");
        proxyable.doubleValue("E");
        proxyable.setDenum(1);
        proxyable.doubleValue("F");
        proxyable.doubleValue("G");
    }
}
