package com.devxpress;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SimpleCyclicBarrier {
    
    private static final int NO_OF_PARTIES = 6;
    
    public static void main(String[] args) {
        
        System.out.println("*** Main thread started... ***");

        ExecutorService executorService = Executors.newFixedThreadPool(NO_OF_PARTIES - 1);
        CyclicBarrier barrier = new CyclicBarrier(NO_OF_PARTIES, () -> {
            System.out.println("Action which runs when all parties are waiting on the barrier causing it to trip...");
        });
        
        for (int i = 1; i <= NO_OF_PARTIES - 1; i++) {
            executorService.execute(new Worker(i, barrier));
        }
        
        try {
            System.out.println("1st time : Main thread waiting on barrier...");
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        
        System.out.println("Main thread resetting barrier...");
        barrier.reset();
        
        for (int i = 1; i <= NO_OF_PARTIES - 1; i++) {
            executorService.execute(new Worker(i, barrier));
        }
        
        try {
            System.out.println("2nd time : Main thread waiting on barrier...");
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        
        executorService.shutdown();
        
        System.out.println("*** ...Main thread finished ***");
    }
    
    static class Worker implements Runnable {
        
        private int id;
        private CyclicBarrier barrier;
        private Random random = new Random();

        Worker(int id, CyclicBarrier barrier) {
            this.id = id;
            this.barrier = barrier;
        }
        
        @Override
        public void run() {
            doWork();
        }
        
        private void doWork() {
            System.out.println("Waiting count = " + barrier.getNumberWaiting() + " : Thread [" + id + "] starting work...");
            
            try {
                Thread.sleep(random.nextInt(3000));
                System.out.println("Thread [" + id + "] ...finished work...now waiting on barrier");
                barrier.await();
                System.out.println("Barrier tripped : Thread [" + id + "] continuing work...");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
    
}
