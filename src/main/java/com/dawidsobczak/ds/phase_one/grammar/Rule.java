package com.dawidsobczak.ds.phase_one.grammar;

public class Rule<T> {
    public Rule(T left, T[] right) {
        this.left = left;
        this.right = right;
    }
    public T left;
    public T[] right;
}
