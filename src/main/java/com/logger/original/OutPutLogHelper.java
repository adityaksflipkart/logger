package com.logger.original;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OutPutLogHelper {
    private ExecutorService executorService;
    private ExecutorCompletionService<String> completionService;
    private OutPutStreamHandle outPutStreamHandle;
    private CountDownLatch countDownLatch;

    public  OutPutLogHelper(ExecutorService executorService,OutPutStreamHandle outPutStreamHandle){
        this.executorService= executorService;
        this.completionService=new ExecutorCompletionService<>(executorService);
        this.outPutStreamHandle=outPutStreamHandle;
        countDownLatch=new CountDownLatch(3);
        outPutStreamHandle.openStream();
    }

    public void outputLog(List<String> messages){
        List<Callable<String>> callables=new ArrayList<>();
        messages.forEach(x-> {
            callables.add(()-> {
                        outPutStreamHandle.writeEvent(x);
                        countDownLatch.countDown();
                        return "Written " +" Task " + x;
                    }
            );
        });
        callables.forEach(completionService::submit);

        messages.forEach(x->{
            try {
                System.out.println(completionService.take().get());
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutDown(){
        if(!executorService.isShutdown()) {
            executorService.shutdown();
        }
        outPutStreamHandle.closeStream();
    }

}
