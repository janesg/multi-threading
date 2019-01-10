package com.devxpress;

public class ThreadUsingRunnable {
    
    public static void main(String[] args) {
        
        // Use a lambda for the Runnable interface
        // - which is a FunctionalInterface that has a single abstract method
        //      - run()
        Thread thread1 = new Thread(() -> {
            // Code that will run in a new thread
            String msg = "Hi from the <" + Thread.currentThread().getName() + "> thread : " +
                         " Priority = " + Thread.currentThread().getPriority();
            System.out.println(msg);
        });

        Thread thread2 = new Thread(() -> {
            throw new RuntimeException("Intentional exception...");
        });
        
        thread1.setName("UpTheWorkers");
        thread1.setPriority(Thread.MAX_PRIORITY);
        thread1.start();
        
        // Define an exception handler for uncaught exceptions
        thread2.setUncaughtExceptionHandler(
                (t, e) -> System.out.println(e.getMessage() + " thrown in " + Thread.currentThread().getName()));
        thread2.setName("BehavingBadly");
        thread2.start();
        
        System.out.println("Hi from the <" + Thread.currentThread().getName() + "> thread : BEFORE SLEEP");
        
        // Sleep the main thread for 2 seconds ... worker will have finished
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //
        }
        
        System.out.println("Hi from the <" + Thread.currentThread().getName() + "> thread : AFTER SLEEP");

        System.exit(0);
    }
}