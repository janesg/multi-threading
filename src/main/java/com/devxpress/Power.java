package com.devxpress;

import java.math.BigInteger;

public class Power {

    public static void main(String[] args) {

        Power p = new Power();
        BigInteger result = p.calculatePowerSum(new BigInteger("12345"), new BigInteger("17"),
                                                new BigInteger("98765"), new BigInteger("23"));
        System.out.println("Result of calculation = " + result);
    }

    private BigInteger calculatePowerSum(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {

        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        System.out.println("Starting first thread...");
        thread1.start();
        System.out.println("Starting second thread...");
        thread2.start();

        try {
            // Allow 3 seconds for each thread to complete calculation
            System.out.println("Joining first thread...");
            thread1.join(3000);
            System.out.println("Joining second thread...");
            thread2.join(3000);

            System.out.println("Calculating result...");

            if (thread1.isFinished()) {
                if (thread2.isFinished()) {
                    return thread1.getResult().add(thread2.getResult());
                } else {
                    thread2.interrupt();
                    throw new RuntimeException("Calculation took too long...");
                }
            } else {
                thread1.interrupt();
                throw new RuntimeException("Calculation took too long...");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Calculation took too long...");
        }
    }

    public static class PowerCalculatingThread extends Thread {
        private BigInteger base;
        private BigInteger power;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        PowerCalculatingThread(BigInteger base, BigInteger power) {
            if (base.compareTo(BigInteger.ZERO) < 0 || power.compareTo(BigInteger.ZERO) < 0) {
                throw new IllegalArgumentException("Both base and power must be greater than or equal to 0");
            }

            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {

            if (base.equals(BigInteger.ZERO) && power.equals(BigInteger.ZERO)) {
                this.result = BigInteger.ONE;
            } else if (power.equals(BigInteger.ZERO)) {
                this.result = BigInteger.ONE;
            } else if (base.equals(BigInteger.ZERO)) {
                this.result = BigInteger.ZERO;
            } else {
                BigInteger tempResult = base;
                for (BigInteger i = BigInteger.ONE; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new RuntimeException("Calculation has been stopped before completion...");
                    }

                    tempResult = tempResult.multiply(base);
                }

                this.result = tempResult;
            }

            this.isFinished = true;
        }

        BigInteger getResult() {
            return this.result;
        }

        boolean isFinished() {
            return this.isFinished;
        }
    }
}
