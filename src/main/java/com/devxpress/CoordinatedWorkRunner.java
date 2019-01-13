package com.devxpress;

import java.util.ArrayList;
import java.util.List;

public class CoordinatedWorkRunner implements Runnable {

    private Barrier barrier;

    public static void main(String[] args) {
        int numberOfWorkerThreads = 10;

        List<Thread> threads = new ArrayList<>();

        // This is the shared object used to coordinate threads
        Barrier barrier = new Barrier(numberOfWorkerThreads);

        for (int i = 0; i < numberOfWorkerThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        threads.forEach(t -> t.start());
    }

    public CoordinatedWorkRunner(Barrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            task();
        } catch (InterruptedException e) {
        }
    }

    // The following task is performed by multiple threads concurrently
    private void task() throws InterruptedException {
        System.out.println("*** " + Thread.currentThread().getName() + " part 1 of the work is finished ***");

        // All threads (in any order) must finish part 1 before going onto part 2
        barrier.barrier();

        System.out.println("*** " + Thread.currentThread().getName() + " part 2 of the work is finished ***");
    }
}
