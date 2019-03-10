package com.devxpress;

import java.util.ArrayList;
import java.util.List;

public class ProducerConsumer {
    
    public static void main(String[] args) {
        
        System.out.println("*** Starting... ***");

        ProdCon pc = new ProdCon(20, 5);
        
        Thread producer = new Thread(() -> pc.fillList(), "Producer");
        Thread consumer = new Thread(() -> pc.emptyList(), "Consumer");

        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("*** Finishing... ***");

    }

    static class ProdCon {
        
        private List<Integer> list = new ArrayList<>();
        private Object lock = new Object();
        private int totalItemsToProduce;
        private int listSize;
        private int loopCount = 1;
        
        public ProdCon(int totalItemsToProduce, int listSize) {
            this.totalItemsToProduce = totalItemsToProduce;
            this.listSize = listSize;
        }
        
        void fillList() {
            synchronized(lock) {
                while(loopCount <= totalItemsToProduce || list.size() > 0) {
                    if (list.size() < listSize) {
                        int value = list.size();
                        list.add(value);
                        System.out.println(Thread.currentThread().getName() + " : added item " + loopCount++ + " with value " + value + " to list");
                    } else {
                        lock.notify();
                        System.out.println(Thread.currentThread().getName() + " : waiting for list items to be consumed...");
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                lock.notify();
            }
        }
        
        void emptyList() {
            synchronized(lock) {
                while(loopCount <= totalItemsToProduce || list.size() > 0) {
                    if (list.size() > 0) {
                        int value = list.remove(list.size() - 1);
                        System.out.println(Thread.currentThread().getName() + " : removed item with value " + value + " from list");
                    } else {
                        lock.notify();
                        System.out.println(Thread.currentThread().getName() + " : waiting for list items to be produced...");
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                lock.notify();
            }
        }
    }    
}