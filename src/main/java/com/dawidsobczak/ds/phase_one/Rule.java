package com.dawidsobczak.ds.phase_one;

public class Rule<T> {
    public Rule(T left, T[] right, int priority) {
        this.left = left;
        this.right = right;
        this.priority = priority;
    }
    public T left;
    public T[] right;
    int priority = 0;
}
