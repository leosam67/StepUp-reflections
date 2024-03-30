package ru.leosam.reflex;

import ru.leosam.reflex.calc.Fraction;
import ru.leosam.reflex.proxy.ObjectState;
import ru.leosam.reflex.proxy.Utils;
import ru.leosam.reflex.proxy.interfaces.Proxyable;

import java.lang.reflect.Proxy;

public class App
{
    public static void main( String[] args )
    {
        Fraction fr = new Fraction(2, 3);
        Utils handler = new Utils(fr);
        handler.startGarbageCollector(500L, 0.0d);
        Proxyable proxyable = (Proxyable) Proxy.newProxyInstance(Proxyable.class.getClassLoader(),
                new Class[] {Proxyable.class},
                handler);
        proxyable.doubleValue("A");
        System.out.println(ObjectState.calcState(proxyable));
        proxyable.doubleValue("B");
        proxyable.doubleValue("C");
        proxyable.setNum(5);
        System.out.println(ObjectState.calcState(proxyable));
        proxyable.doubleValue("D");
        proxyable.doubleValue("E");
        proxyable.setDenum(1);
        System.out.println(ObjectState.calcState(proxyable));
        proxyable.doubleValue("F");
        proxyable.doubleValue("G");
        handler.stopGarbageCollector();
    }
}
