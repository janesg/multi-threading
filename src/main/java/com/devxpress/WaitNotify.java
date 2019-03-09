package com.devxpress;

import java.util.ArrayList;
import java.util.List;

public class WaitNotify {
    
    public static void main(String[] args) {
        
        System.out.println("*** Starting... ***");

        Processor p = new Processor();
        
        List<Thread> waiters = new ArrayList<>();
        
        for (int i = 0; i < 5 ; i++) {
            waiters.add(new Thread(() -> p.doWait()));
            waiters.add(new Thread(() -> p.doWaitUsingBlock()));
        }
        
        List<Thread> notifiers = new ArrayList<>();
        
        for (int i = 0; i < 5 ; i++) {
            notifiers.add(new Thread(() -> p.doNotify()));
            notifiers.add(new Thread(() -> p.doNotifyUsingBlock()));
        }
        
        for (Thread t : waiters) {
            t.start();
        }

        // Allow all waiting threads to run before notifiers
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        for (Thread t : notifiers) {
            t.start();
        }

        try {
            for (Thread t : waiters) {
                t.join();
            }

            for (Thread t : notifiers) {
                t.join();
            }
        } catch (Exception e) {
            //
        }
        
        System.out.println("*** Finished ***");
    }
    
    // Class that demonstrates that the following are equivalent:
    //  - synchonized instance method
    //  - instance method whose whole body is bounded by a block protected using 'synchronized(this)'
    static class Processor {
        
        synchronized void doWait() {
            System.out.println(Thread.currentThread().getName() + " : About to wait...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : ...finished waiting");
        }       
    
        synchronized void doNotify() {
            System.out.println(Thread.currentThread().getName() + " : About to sleep...");
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println(Thread.currentThread().getName() + " : About to notify any waiting threads...");
            
            notify();

            System.out.println(Thread.currentThread().getName() + " : ...finished notifying");
        }       

        void doWaitUsingBlock() {
            synchronized(this) {
                System.out.println(Thread.currentThread().getName() + " : About to wait...");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " : ...finished waiting");
            }
        }       
    
        void doNotifyUsingBlock() {
            synchronized(this) {
                System.out.println(Thread.currentThread().getName() + " : About to sleep...");
                
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println(Thread.currentThread().getName() + " : About to notify any waiting threads...");
                
                notify();

                System.out.println(Thread.currentThread().getName() + " : ...finished notifying");
            }
        }       
    }
    
}
