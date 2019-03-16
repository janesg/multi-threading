package com.devxpress;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class SimpleCountDownLatch {
    
    public static void main(String[] args) {
        
        System.out.println("*** Started... ***");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        
        CountDownLatch latch = new CountDownLatch(5);
        
        for (int i = 1; i <= 5; i++) {
            executorService.execute(new Worker(i, latch));
        }
        
        try {
            latch.await();
            System.out.println("Latch count is zero so all worker threads have completed...continue main thread processing...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        
        System.out.println("*** ...Finished ***");
    }
    
    static class Worker implements Runnable {
        
        private int id;
        private CountDownLatch latch;

        Worker(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }
        
        @Override
        public void run() {
            doWork();
        }
        
        private void doWork() {
            System.out.println("Latch count = " + latch.getCount() + " : Thread [" + id + "] starting work...");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            latch.countDown();

            System.out.println("Latch count = " + latch.getCount() + " : Thread [" + id + "] ...finished work");
        }
    }
    
}
