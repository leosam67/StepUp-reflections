package ru.leosam.reflex;

public interface Proxyable {
    @Cache
    double doubleValue(String msg);
    @Mutator
    public void setNum(int num);
    @Mutator
    public void setDenum(int denum);
}
