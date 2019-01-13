package com.devxpress;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    private final int numberOfWorkers;
    private Semaphore semaphore = new Semaphore(0);
    private int counter = 0;
    private Lock lock = new ReentrantLock();

    public Barrier(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public void barrier() {
        lock.lock();
        boolean isLastWorker = false;
        try {
            counter++;

            if (counter == numberOfWorkers) {
                isLastWorker = true;
            }
        } finally {
            lock.unlock();
        }

        if (isLastWorker) {
            System.out.println(Thread.currentThread().getName() + " [last worker] about to release (" +
                    (numberOfWorkers - 1) + ") 'permits' to semaphore...");
            // Release a 'permit for each of the other threads
            semaphore.release(numberOfWorkers - 1);
        } else {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting to acquire 'permit' from semaphore...");
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " acquired 'permit' from semaphore");
            } catch (InterruptedException e) {
            }
        }
    }
}
