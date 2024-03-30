package ru.leosam.reflex.calc;

import ru.leosam.reflex.proxy.interfaces.Proxyable;

public class Fraction implements Proxyable {
    private int num;
    private int denum;
    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }
    @Override
    public String toString() {
        return "Fraction{" + num + ", " + denum + '}';
    }

    @Override
    //@Cache
    public double doubleValue(String msg) {
        return (double) num / denum;
    }

    @Override
    //@Mutator
    public void setNum(int num) {
        this.num = num;
    }
    @Override
    //@Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }
}
