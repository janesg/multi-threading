package com.devxpress.dining;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {
    
    private int id;
    private Lock lock;
    
    public Chopstick(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }
    
    // Can the specified philosopher pickup their left/right chopstick
    public boolean pickUp(Philosopher philosopher, State state) throws InterruptedException {
        if (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println(philosopher.toString() + " ^ picked up ^ " + state.toString() + " " + this.toString());
            return true;
        }
        
        return false;
    }
    
    public void putDown(Philosopher philosopher, State state) throws InterruptedException {
        lock.unlock();
        System.out.println(philosopher.toString() + " v put down v " + state.toString() + " " + this.toString());
    }
    
    @Override
    public String toString() {
        return "Chopstick " + id;
    }
}