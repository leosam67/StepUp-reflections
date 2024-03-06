package ru.leosam.reflex;

public class Fraction implements Proxyable {
    private int num;
    private int denum;
    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }
    public Fraction() {}

    @Override
    public String toString() {
        return "Fraction{" + num + ", " + denum + '}';
    }

    @Override
    //@Cache
    public double doubleValue(String msg) {
        System.out.println("* Invoked doubleValue(" + msg + ")");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;

        Fraction fraction = (Fraction) o;

        if (num != fraction.num) return false;
        return denum == fraction.denum;
    }

    @Override
    public int hashCode() {
        int result = num;
        result = 31 * result + denum;
        return result;
    }
}