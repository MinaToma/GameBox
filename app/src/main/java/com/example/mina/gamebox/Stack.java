package com.example.mina.gamebox;

public class Stack {

    private Node head;

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getHead() {

        return head;
    }

    public Stack() {
        head = null;
    }

    public void push(int  value) {
        Node newNode = new Node(value);
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
    }

}
