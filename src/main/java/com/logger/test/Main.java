package com.logger.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Test test = new Test();
        Collection<Callable<Integer>> callables=new ArrayList<>();
        callables.add(()-> {
            {
                for (int i=0;i<5;i++) {
                    try {
                        {
                            test.print(Thread.currentThread().getName());
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
                return 0;
        }});

        callables.add(() -> {
            for (int i=0;i<5;i++) {

                try {
                    synchronized (test) {
                        test.print(Thread.currentThread().getName());
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 1;
        });

        callables.add(()-> {
                for (int i=0;i<5;i++) {

                    try {
                        synchronized (test) {
                            test.print(Thread.currentThread().getName());
                        }
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } return 2;});
        ExecutorService executorService= Executors.newScheduledThreadPool(10);


        List<Future<Integer>> futures = executorService.invokeAll(callables);

        for (Future<Integer> future : futures) {
            System.out.println(future.get());

        }

        executorService.shutdown();
        if(executorService.awaitTermination(100,TimeUnit.MILLISECONDS)){
            System.out.println("all executor shut ");
        }
    }
}
