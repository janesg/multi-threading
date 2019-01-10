package com.devxpress;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factorial {

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(10000000L, 0L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);

        List<FactorialThread> threads = new ArrayList<>();

        for (Long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }

        for (Thread thread : threads) {
            thread.start();
            // Allow 2 seconds for each thread to finish
            thread.join(2000);
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            if (threads.get(i).isFinished()) {
                System.out.println("Thread [" + i + "] has finished : Factorial of " +
                                    inputNumbers.get(i) + " = " + threads.get(i).getResult());
            } else {
                System.out.println("Thread [" + i + "] still running : Factorial of " +
                        inputNumbers.get(i) + " is not yet available");
            }
        }
    }

    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(this.inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i >= 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }

            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
