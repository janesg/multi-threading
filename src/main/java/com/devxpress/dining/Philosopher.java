package com.devxpress.dining;

import java.util.Random;

public class Philosopher implements Runnable {
    
    private int id;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private Random random;
    private int eatCount;
    private volatile boolean full = false;

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            // Note : boolean controlling while loop is defined as volatile 
            //        to avoid thread using cached version and never exiting loop
            while (!full) {
                think();
                
                if (leftChopstick.pickUp(this, State.LEFT)) {
                    if (rightChopstick.pickUp(this, State.RIGHT)) {
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);
                    }
                    
                    leftChopstick.putDown(this, State.LEFT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void think() throws InterruptedException {
        System.out.println(this + " is thinking...");
        Thread.sleep(random.nextInt(1000));
    }
    
    private void eat() throws InterruptedException {
        System.out.println(this + " is eating...");
        eatCount++;
        Thread.sleep(random.nextInt(1000));
    }
    
    public int getEatCount() {
        return eatCount;
    }
    
    public void setFull(boolean full) {
        this.full = full;
    }
    
    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}