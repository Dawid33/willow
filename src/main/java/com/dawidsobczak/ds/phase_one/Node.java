package com.dawidsobczak.ds.phase_one;

import java.util.ArrayList;

public class Node<T> {
    T symbol;
    ArrayList<Node<T>> children = new ArrayList<>();
    Node parent = null;

    public Node(T symbol) {
        this.symbol = symbol;
    }

    public void appendChild(Node<T> n) {
        n.parent = this;
        children.add(n);
    }

    public String toString() {
        return symbol.toString();
    }
}