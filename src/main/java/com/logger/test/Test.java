package com.logger.test;

public class Test {
    public void print(String name) throws InterruptedException {
        System.out.println("{");
        Thread.sleep(10);
        System.out.println(name);
        System.out.println("}");
    }
}
