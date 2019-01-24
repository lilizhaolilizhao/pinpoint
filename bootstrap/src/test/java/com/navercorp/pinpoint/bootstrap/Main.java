package com.navercorp.pinpoint.bootstrap;

public class Main {
    public static void main(String[] args) {
        System.out.println("this is a test!");

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
