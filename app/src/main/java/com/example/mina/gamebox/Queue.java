package com.example.mina.gamebox;

import java.util.ArrayList;

public class Queue {

    Node front, back;
    ArrayList<Node> nodes;

    public Queue() {
        front = back = null;
        nodes = new ArrayList<>();
    }

    void enqueue(int value)
    {

        Node temp = new Node(value);

        temp.setVOrder(1);
        nodes.add(temp);
        if (back == null)
        {
            front = back = temp;
            return;
        }

        temp.setVOrder(back.getVOrder() + 1);
        back.left = temp;
        back = temp;
    }

    public void dequeue()
    {
        if (front == null) return;

        this.front = front.left;

        nodes.remove(0);

        if (this.front == null) this.back = null;
    }
}
