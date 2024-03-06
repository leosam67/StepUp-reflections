package ru.leosam.reflex;

public interface Fractionable {
    @Cache
    double doubleValue(String msg);
    @Mutator
    void setNum(int num);
    @Mutator
    void setDenum(int denum);
}
