package com.devxpress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SafeCrack {
    
    private static final int MAX_PIN = 6666;
    
    public static void main(String[] args) {
        Random random = new Random();
        
        Vault vault = new Vault(random.nextInt(MAX_PIN));
        
        List<Thread> threads = new ArrayList<>();
        
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());
        
        for (Thread thread : threads) {
            thread.start();
        }
    }
    
    
    public static class Vault {
        
        private int pin;
        
        Vault(int pin) {
            this.pin = pin;    
        }
        
        boolean isCorrectPin(int guess) {
            // Slow down the hacker's attempt to guess
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
            
            return this.pin == guess;
        }
    }
    
    private static abstract class HackerThread extends Thread {
        protected Vault vault;
        
        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }
        
        @Override
        public void start() {
            System.out.println("Starting thread : " + this.getName());
            super.start();
        }
    }
    
    private static class AscendingHackerThread extends HackerThread {
        public AscendingHackerThread(Vault vault) {
            super(vault);
        }
        
        @Override
        public void run() {
            for (int guess = 0; guess <= MAX_PIN; guess++) {
                if (vault.isCorrectPin(guess)) {
                    System.out.println(this.getName() + " guessed the PIN of " + guess);
                    // Stop the program
                    System.exit(0);
                }
            }
        }
    }
    
    private static class DescendingHackerThread extends HackerThread {
        public DescendingHackerThread(Vault vault) {
            super(vault);
        }
        
        @Override
        public void run() {
            for (int guess = MAX_PIN; guess >= 0; guess--) {
                if (vault.isCorrectPin(guess)) {
                    System.out.println(this.getName() + " guessed the PIN of " + guess);
                    // Stop the program
                    System.exit(0);
                }
            }
        }
    }
    
    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i >= 0; i--) {
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            
            System.out.println("Caught those pesky hackers !!");
            System.exit(0);
        }
    }
    
}
