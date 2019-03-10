package com.devxpress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CallableFuture {
    
    public static void main(String[] args) throws Exception {
        System.out.println("*** Started... ***");
    
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        List<Future<String>> futureList = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            futureList.add(pool.submit(new Worker(i)));
        }
        
        for (Future<String> f : futureList) {
            System.out.println(f.get());
        }
        
        pool.shutdown();
    
        System.out.println("*** ...Finished ***");
    }
}

class Worker implements Callable<String> {
    
    private int id;
    
    Worker(int id) {
        this.id = id;
    }
    
    @Override
    public String call() throws Exception {
        Thread.sleep(1500);
        return "Id : " + id;
    }
}