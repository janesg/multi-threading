package com.devxpress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitNotify {
    
    public static void main(String[] args) {
        
        System.out.println("*** Starting... ***");

        Processor p = new Processor();
        
        List<Thread> waiters = new ArrayList<>();
        
        for (int i = 0; i < 5 ; i++) {
            waiters.add(new Thread(() -> p.doWait()));
            waiters.add(new Thread(() -> p.doWaitUsingBlock()));
            waiters.add(new Thread(() -> p.doAwaitUsingLock()));
        }
        
        List<Thread> notifiers = new ArrayList<>();
        
        for (int i = 0; i < 5 ; i++) {
            notifiers.add(new Thread(() -> p.doNotify()));
            notifiers.add(new Thread(() -> p.doNotifyUsingBlock()));
            notifiers.add(new Thread(() -> p.doSignalUsingLock()));
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
        
        private static final Lock lock = new ReentrantLock();
        private static final Condition condition = lock.newCondition();
        
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
            
            System.out.println(Thread.currentThread().getName() + " : About to notify a single waiting thread...");
            
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
                
                System.out.println(Thread.currentThread().getName() + " : About to notify a single waiting thread...");
                
                notify();

                System.out.println(Thread.currentThread().getName() + " : ...finished notifying");
            }
        }       

        void doAwaitUsingLock() {
            System.out.println(Thread.currentThread().getName() + " : About to await...");
            try {
                lock.lock();
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + " : ...finished awaiting");
        }       
    
        void doSignalUsingLock() {
            System.out.println(Thread.currentThread().getName() + " : About to sleep...");
                
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                
            System.out.println(Thread.currentThread().getName() + " : About to signal the lock...");
                
            try {
                lock.lock();
                condition.signal();
            } finally {
                lock.unlock();
            }

            System.out.println(Thread.currentThread().getName() + " : ...finished signaling the lock");
        }       
    }
    
}
