package com.example.mina.gamebox;

import android.content.Context;
import android.widget.Toast;

public class AVLTree{

    Node root;
    Context context;

    public Node getRoot() {
        return root;
    }

    public AVLTree(Context context)
    {
        this.context = context;
        root = null;
    }

    private int getHeight(Node node){
        if(node == null) return 0;
        return node.height;
    }

    public void insert(int value)
    {
        if(find(value) != null) {
            Toast.makeText(context.getApplicationContext() , "Node Already Exists" , Toast.LENGTH_SHORT).show();
        }

        root = insertKey(root , value);
    }

    private int getBalance(Node node){
        if(node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private Node leftRotate(Node node){
        Node right = node.right;
        Node lChild = right.left;

        right.left = node;
        node.right = lChild;

        node.height = 1 + Math.max(getHeight(node.right) , getHeight(node.left));
        right.height = 1 + Math.max(getHeight(right.right) , getHeight(right.left));

        return right;
    }

    private Node rightRotate(Node node){
        Node left = node.left;
        Node rChild = left.right;

        left.right = node;
        node.left = rChild;

        node.height = 1 + Math.max(getHeight(node.right) , getHeight(node.left));
        left.height = 1 + Math.max(getHeight(left.right) , getHeight(left.left));

        return left;
    }

    private Node insertKey(Node start, int key)
    {
        if(start == null)
        {
            start = new Node(key);
            return start;
        }

        if(key < start.value)
        {
            start.left = insertKey(start.left, key);
        }
        else if(key > start.value)
        {
            start.right = insertKey(start.right, key);
        }
        else return  start;

        start.height = 1 + Math.max(getHeight(start.left) , getHeight(start.right));

        int balance = getBalance(start);

        // Left Left Case
        if (balance > 1 && key < start.left.value)
            return rightRotate(start);

        // Right Right Case
        if (balance < -1 && key > start.right.value)
            return leftRotate(start);

        // Left Right Case
        if (balance > 1 && key > start.left.value)
        {
            start.left =  leftRotate(start.left);
            return rightRotate(start);
        }

        // Right Left Case
        if (balance < -1 && key < start.right.value)
        {
            start.right = rightRotate(start.right);
            return leftRotate(start);
        }

        return start;
    }

    public void delete(int key){
        root = deleteIn(root , key);
    }

    private Node deleteIn(Node root , int key)
    {
        if(root == null) return root; //empty tree

        if(key < root.value){
            root.left = deleteIn(root.left , key);
        }
        else if(key > root.value){
            root.right = deleteIn(root.right , key);
        }
        else{

            // node with only one child or no child
            if( (root.left == null) || (root.right == null) )
            {
                Node temp = root.left != null ? root.left : root.right;

                // No child case
                if (temp == null)
                    root = null;
                else // One child case
                    root = temp; // Copy the contents of
            }
            else
            {
                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                Node temp = findMin(root.right);

                // Copy the inorder successor's data to this node
                root.value = temp.value;

                // Delete the inorder successor
                root.right = deleteIn(root.right, temp.value);
            }
        }

        if (root == null)
            return root;

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0)
        {
            root.left =  leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0)
        {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node findMin(Node node)
    {
        while(node.left != null)
        {
            node = node.left;
        }
        return node;
    }

    private Node find(int value)
    {
        Node node = root;
        while(node != null && node.value != value)
        {
            if (value < node.value)
                node = node.left;
            else
                node = node.right;
        }
        return node;
    }

    private int setNodePosition(boolean isRight , int level , Node currNode , int prevHPos , int pivot){
        if(currNode == null) return 0;

        if(isRight){

            prevHPos = setNodePosition(isRight , level + 1 ,  currNode.left , prevHPos - 1 , pivot) + 1;

            if(prevHPos <= pivot){
                prevHPos = pivot + 1;
            }

            currNode.setHOrder(prevHPos);
            currNode.setVOrder(level);

            prevHPos = Math.max(setNodePosition(isRight , level + 1 , currNode.right , prevHPos + 1 , prevHPos) , prevHPos);

            return prevHPos;
        }
        else{

            prevHPos = setNodePosition(isRight , level + 1 , currNode.right , prevHPos + 1 , pivot) - 1;

            if(prevHPos >= pivot){
                prevHPos = pivot - 1;
            }

            currNode.setVOrder(level);
            currNode.setHOrder(prevHPos);

            prevHPos = Math.min(setNodePosition(isRight , level + 1 ,  currNode.left , prevHPos - 1 , prevHPos) , prevHPos);

            return prevHPos;
        }
    }

    public void setTree(){
        if(root != null){
            root.setVOrder(0);
            root.setHOrder(0);
            setNodePosition(true , 1 , root.right , 1 , 0);
            setNodePosition(false , 1 , root.left , -1 , 0);
        }
    }
}