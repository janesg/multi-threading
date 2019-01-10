package com.devxpress;

public class ExtendingThread {
    
    public static void main(String[] args) {
        
        Thread thread1 = new NewThread();
        thread1.setName("TheWorker");
        thread1.start();
        
        // Remove the exit so that VM is still running when
        // worker thread writes the message out to console
        //System.exit(0);
        
    }
    
    private static class NewThread extends Thread {
        @Override
        public void run() {
            // Code that will run in a new thread
            String msg = "Hi from the <" + this.getName() + "> thread : " +
                         " Priority = " + this.getPriority();
            System.out.println(msg);
        }
    }
}