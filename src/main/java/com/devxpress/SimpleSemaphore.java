package com.devxpress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

// Use enum to implement Singleton pattern
enum Downloader {
    INSTANCE;
    
    // Only allow a maximum of 3 threads to download in parallel by specifying 3 permits
    //  - the 'true' parameter indicates use of fairness policy..longest runing thread gets priority
    private Semaphore semaphore = new Semaphore(3, true);
    
    public void downloadData() {
        
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " : semaphore acquired *** <<<--- ***");
            download();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " : semaphore released *** --->>> ***");
        }
    }
    
    private void download() {
        System.out.println(Thread.currentThread().getName() + " : simulating data download...");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


public class SimpleSemaphore {
    
    public static void main(String[] args) {
        
        System.out.println("*** Starting... ***");

        List<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> Downloader.INSTANCE.downloadData()));
            threads.get(i).start();
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("*** Finishing... ***");
        
    }
    
}