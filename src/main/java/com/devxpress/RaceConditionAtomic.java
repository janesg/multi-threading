package com.devxpress;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceConditionAtomic {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();

        IncrementingThread incThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decThread = new DecrementingThread(inventoryCounter);

        // Run concurrently ... final count = ??? ...only sometimes is it zero
        incThread.start();
        decThread.start();

        incThread.join();
        decThread.join();

        System.out.println("Final inventory count = " + inventoryCounter.getItemCount());
    }

    static class IncrementingThread extends Thread {
        InventoryCounter invCount;

        public IncrementingThread(InventoryCounter invCount) {
            this.invCount = invCount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                invCount.increment();
            }
        }
    }

    static class DecrementingThread extends Thread {
        InventoryCounter invCount;

        public DecrementingThread(InventoryCounter invCount) {
            this.invCount = invCount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                invCount.decrement();
            }
        }
    }

    static class InventoryCounter {
        // Using AtomicInteger we don't need a lock
        private AtomicInteger itemCount = new AtomicInteger(0);

        void increment() {
            itemCount.incrementAndGet();
        }

        void decrement() {
            itemCount.decrementAndGet();
        }

        int getItemCount() {
            return itemCount.get();
        }
    }
}
