package com.devxpress;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleReentrantLock {
    
    private static int lockedCount = 0;
    private static int unlockedCount = 0;
    private static final Lock lock = new ReentrantLock();
    
    private static void lockedIncrement() {
        
        lock.lock();
        
        try {
            lockedCount++;
        } finally {
            lock.unlock();
        }
    }
    
    private static void unlockedIncrement() {
        unlockedCount++;
    }
    
    public static void main(String[] args) {
        
        System.out.println("*** Starting... ***");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lockedIncrement();
                unlockedIncrement();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lockedIncrement();
                unlockedIncrement();
            }
        });

        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("*** Finishing with locked / unlocked counts of " + 
                           lockedCount + " / " + unlockedCount + " ***");

    }
    
}