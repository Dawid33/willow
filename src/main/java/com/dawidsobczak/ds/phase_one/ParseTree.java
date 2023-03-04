package com.dawidsobczak.ds.phase_one;

import java.util.*;

public class ParseTree<T> {
    Node<T> root;
    public ParseTree(Node<T> root) {
        this.root = root;
    }

    public String toString() {
        Deque<Node<T>> nodeStack = new ArrayDeque<>(List.of(root));
        LinkedList<Integer> childCountStack = new LinkedList<>(List.of(root.children.size() - 1));
        StringBuilder b = new StringBuilder();
//        ArrayList<Node<T>> temp = new ArrayList<>();
        b.append(root).append("\n");
        while(!nodeStack.isEmpty()) {
            var current = nodeStack.pop();
            int current_child = childCountStack.pop();

            while (current.children.size() > 0 && current_child >= 0) {
                for (int i = childCountStack.size() - 1; i >= 0; i--) {
                    if (childCountStack.get(i) >= 0) {
                        b.append("| ");
                    } else {
                        b.append("  ");
                    }
                }
                if (current_child != 0) {
                    b.append("├─").append(current.children.get(current_child)).append("\n");
                } else {
                    b.append("└─").append(current.children.get(current_child)).append("\n");
                }
                if (!current.children.get(current_child).children.isEmpty()) {
                    nodeStack.push(current);
                    var child = current.children.get(current_child);
                    current_child -= 1;
                    nodeStack.push(child);
                    childCountStack.push(current_child);
                    childCountStack.push(child.children.size() - 1);

                    break;
                }
                current_child -= 1;
            }
            System.out.println();
        }
        return b.toString();
    }
}
