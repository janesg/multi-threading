package com.devxpress;

public class DataRace {

    public static void main(String[] args) {
        SharedClass sc = new SharedClass();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sc.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sc.checkForDataRace();
            }
        });

        t1.start();
        t2.start();
    }

    static class SharedClass {
        // Adding the volatile keyword to each variable means that
        // code instructions listed before/after access to the variable
        // can not be reordered by compiler / CPU
        //  - this is enough to prevent the data race
//        private int x = 0;
//        private int y = 0;
        private volatile int x = 0;
        private volatile int y = 0;

        public void increment() {
            // Note: Compiler or CPU can decide to order these instructions
            //       in any order as long as the logic is maintained.
            //       Since neither statement relies on the other, order is random
            x++;
            y++;
        }

        public void checkForDataRace() {
            // If instructions were maintained in same order as specified in
            // the increment method it should not be possible for Y to be
            // greater than X
            if (y > x) {
                System.out.println("Data race detected... Y > X");
            }
        }

//        public void checkForDataRace() {
//            int x2 = x;
//            int y2 = y;
//
//            if (x2 != y2) {
//                if (x2 > y2) {
//                    System.out.println("Data race detected... X = " + x2 + " > Y = " + y2);
//                } else {
//                    System.out.println("Data race detected... Y = " + y2 + " > X = " + x2);
//                }
//            }
//        }
    }

}
