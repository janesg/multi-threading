package com.devxpress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class LockFreeDataStructure {

    public static void main(String[] args) throws InterruptedException {

        // StandardStack comes in at around 200 million operations in 10 secs
//        StandardStack<Integer> stack = new StandardStack<>();
        // LockFreeStack comes in at around 525 million operations in 10 secs
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Random random = new Random();

        // Initialise the stack
        for (int i = 0; i < 100000; i++) {
            stack.push(random.nextInt());
        }

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            t.setDaemon(true);
            threads.add(t);
        }

        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            t.setDaemon(true);
            threads.add(t);
        }

        threads.forEach(t -> t.start());

        // Give the pushing and popping threads 10 seconds to do their thing
        // then find out how many operations they managed
        Thread.sleep(10000);

        System.out.println(String.format("%,d operations performed in 10 seconds", stack.getOperationCount()));
    }

    static class LockFreeStack<T> {
        private AtomicReference<Node<T>> head = new AtomicReference<>();
        private AtomicInteger operationCount = new AtomicInteger(0);

        public void push(T value) {
            Node<T> newHeadNode = new Node<>(value);

            // Repeatedly try to get and set the new head node
            while (true) {
                // Read the current head node
                Node<T> currentHeadNode = head.get();

                // Processing based on head node read
                newHeadNode.setNext(currentHeadNode);

                // Expectation that head node has not changed since we read it above
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    // Current head was still same as we read and so we were
                    // able to update the head to the new value
                    //  - we're done so break out of while loop
                    break;
                } else {
                    // Current head has changed since we read it so
                    // try again but wait for 1 nano-second first
                    LockSupport.parkNanos(1);
                }
            }

            operationCount.incrementAndGet();
        }

        public T pop() {
            Node<T> currentHeadNode = head.get();
            Node<T> newHeadNode;

            while (currentHeadNode != null) {
                newHeadNode = currentHeadNode.getNext();

                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentHeadNode = head.get();
                }
            }

            operationCount.incrementAndGet();

            return currentHeadNode != null ? currentHeadNode.getValue() : null;
        }

        public int getOperationCount() {
            return operationCount.get();
        }
    }

    // Have to make push and pop synchronized methods to
    // prevent concurrent changes to head and operationCount variables
    static class StandardStack<T> {
        private Node<T> head;
        private int operationCount;

        public synchronized void push(T value) {
            Node<T> node = new Node<>(value);
            node.setNext(head);
            head = node;
            operationCount++;
        }

        public synchronized T pop() {
            if (head == null) {
                operationCount++;
                return null;
            }

            T value = head.getValue();
            head = head.getNext();
            operationCount++;

            return value;
        }

        public int getOperationCount() {
            return operationCount;
        }
    }

    static class Node<T> {
        private T value;
        private Node<T> next = null;

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }
}
