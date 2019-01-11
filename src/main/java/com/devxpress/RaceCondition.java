package com.devxpress;

public class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();

        IncrementingThread incThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decThread = new DecrementingThread(inventoryCounter);

        // Run sequentially ... final count = 0
//        incThread.start();
//        incThread.join();
//
//        decThread.start();
//        decThread.join();

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
        private int itemCount = 0;

        // A single thread entering just one synchronized method blocks all other
        // threads from entering ANY of the objects synchronized methods
        //  - this is because the monitor (lock object) is the class instance itself
        //  - equivalent to     - synchronized(this) { ... }
//        synchronized void increment() {
//            // Non-atomic operation
//            itemCount++;
//        }
//
//        synchronized void decrement() {
//            // Non-atomic operation
//            itemCount--;
//        }

        // Use an explicitly created lock (monitor) object and use it with
        // synchronized block.
        // Only a single thread can enter one or more blocks synchronized on same monitor object
        Object lock = new Object();

        void increment() {
            synchronized(lock) {
                // Non-atomic operation
                itemCount++;
            }
        }

        void decrement() {
            synchronized(lock) {
                // Non-atomic operation
                itemCount--;
            }
        }

        // Unsynchronized non-atomic mutating operations leads to unpredictable results
//        void increment() {
//            // Non-atomic operation
//            itemCount++;
//        }
//
//        void decrement() {
//            // Non-atomic operation
//            itemCount--;
//        }

        int getItemCount() {
            return itemCount;
        }
    }
}
