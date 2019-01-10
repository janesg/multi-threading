package com.devxpress;

import java.math.BigInteger;

public class ThreadInterrupt {
    
    public static void main(String[] args) {
        Thread blockingT = new Thread(new BlockingTask());
        
        blockingT.start();
        
        // Because the blocking thread will be running a method (sleep) that
        // explicitly responds to interrupt signal by throwing InterruptedException, 
        // we can wake it up and exit the program
        blockingT.interrupt();
        
        Thread nonDaemonComputingT = 
            new Thread(new LongComputationTask(new BigInteger("1011"), new BigInteger("8634")));
            
        nonDaemonComputingT.start();
        
        // Because the computing thread will be running a method that regularly 
        // checks whether its been interrupted, we can wake it up and exit the program
        nonDaemonComputingT.interrupt();
        
        // Daemon thread will automatically be stopped when main thread exits
        // .... no interrupt needed
        Thread daemonComputingT = 
            new Thread(new LongComputationTask(new BigInteger("1011"), new BigInteger("8634")));
        
        daemonComputingT.setDaemon(true);
        daemonComputingT.start();
        
        System.out.println("Exiting the main thread");
    }
    
    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread...");
            }
        }
    }
    
    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
        
        @Override
        public void run() {
            System.out.println(base + " ^ " + power + " = " + pow(base, power));
        }
        
        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i.add(BigInteger.ONE)) {
                // Allow computation loop to be ended by checking if the
                // thread has been interrupted
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Computation prematurely interrupted !");
                    return BigInteger.ONE;
                }
                
                result = result.multiply(base);
            }
            
            return result;
        }
    }
}