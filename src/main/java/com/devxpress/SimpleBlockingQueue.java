package com.devxpress;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleBlockingQueue {
    
    private static final int FINAL_VALUE = -999;
    
    public static void main(String[] args) {
        
        System.out.println("*** Started... ***");

        ExecutorService executorService = Executors.newCachedThreadPool();
        
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        AtomicBoolean finished = new AtomicBoolean(false);

        // Create and start the consumers
        for (int i = 1; i <= 3; i++) {
            executorService.execute(new Consumer(i, queue, finished));
        }
        
        // Create and start the producer
        executorService.execute(new Producer(queue));
        
        while (!finished.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        System.out.println("Producer and at least one consumer have completed...interrupt any consumers waiting to take from queue...");
        
        // Use shutdownNow to interrupt any waiting threads
        executorService.shutdownNow();

        System.out.println("*** ...Finished ***");
    }
    
    static class Producer implements Runnable {
        
        private BlockingQueue<Integer> queue;

        Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }
        
        @Override
        public void run() {
            System.out.println("Producer Thread : starting...");
            
            try {
                for (int i = 1; i <= 25; i++) {
                    queue.put(new Integer(i));
                    System.out.println("===> " + i);
            
                    // Sleep much less than consumers so that we fill the queue up to capacity (i.e. 5)
                    Thread.sleep(10);
                }
                
                // Signal consumers to finish
                queue.put(new Integer(FINAL_VALUE));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            System.out.println("Producer Thread : ...finished");
        }
    }
    
    static class Consumer implements Runnable {
        
        private int id;
        private BlockingQueue<Integer> queue;
        private AtomicBoolean finished;

        Consumer(int id, BlockingQueue<Integer> queue, AtomicBoolean finished) {
            this.id = id;
            this.queue = queue;
            this.finished = finished;
        }
        
        @Override
        public void run() {
            System.out.println("Consumer Thread [" + id + "] : starting...");
            
            while (!finished.get()) {
                try {
                    System.out.println("Consumer Thread [" + id + "] : waiting to take from queue...");
                    Integer value = queue.take();
                    System.out.println("<=== " + value + " : Consumer Thread [" + id + "]");

                    if (value == FINAL_VALUE) {
                        finished.set(true);
                    }
                    
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    if (!finished.get()) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Consumer Thread [" + id + "] : ...finished");
        }
    }
    
}
