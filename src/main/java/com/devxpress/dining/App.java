package com.devxpress.dining;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    
    public static void main(String[] args) {
        
        System.out.println("*** Started... ***");

        ExecutorService executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);
        Philosopher[] philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
        Chopstick[] chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];
        
        for (int i = 0; i < Constants.NUMBER_OF_CHOPSTICKS; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        
        try {
            for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % Constants.NUMBER_OF_CHOPSTICKS]);
                executorService.execute(philosophers[i]);
            }
            
            Thread.sleep(Constants.SIMULATION_DURATION);
            
            for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
                philosophers[i].setFull(true);
                System.out.println(philosophers[i] + " is now officially full");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            System.out.println("Shutting down...");
        
            // Now wait for all threads to terminate
            while(!executorService.isTerminated()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        System.out.println("All philosophers are now full and have finished eating...");
        
        for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
            System.out.println(philosophers[i] + " was able to eat " + 
                               philosophers[i].getEatCount() + " times");
        }
        
        System.out.println("*** ...Finished ***");
    }
    
}