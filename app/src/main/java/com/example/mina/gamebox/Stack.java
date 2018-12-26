package com.example.mina.gamebox;

import java.util.ArrayList;

public class Stack {

    private Node head;
    ArrayList<Node> nodes;

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getHead() {

        return head;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public Stack() {
        head = null;
        nodes = new ArrayList<>();
    }

    public void push(int  value) {
        Node newNode = new Node(value);
        nodes.add(newNode);

        if (head == null) {
            head = newNode;
            head.setVOrder(1);
        } else {
            newNode.left = head;
            newNode.setVOrder(head.VOrder + 1);
            head = newNode;
        }
    }

    public void pop() {
        if (head == null) return;
        head = head.left;
        nodes.remove(nodes.size() - 1);
    }
}
