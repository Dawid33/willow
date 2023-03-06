package com.dawidsobczak.ds.phase_one;

import java.util.ArrayList;

public class Node<T> {
    public T symbol;
    public String content;
    public ArrayList<Node<T>> children = new ArrayList<>();
    public Node<T> parent = null;

    public Node(T symbol, String content) {
        this.symbol = symbol;
        this.content = content;
    }

    public void appendChild(Node<T> n) {
        n.parent = this;
        children.add(n);
    }

    public String toString() {
        return symbol.toString();
    }
}