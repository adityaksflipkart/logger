package com.logger.original;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Logger {
    private BlockingQueue<LogEvent> logEvents1;
    private BlockingQueue<LogEvent> logEvents2;
    private OutPutLogHelper outPutLogHelper;
    private int maxQueueSize;
    private InUseQueue inUseQueue;
    private Logger() {
        logEvents1 = new LinkedBlockingQueue<LogEvent>();
        logEvents2=new LinkedBlockingQueue<LogEvent> ();
    }

    public static Logger initLogger(int maxQueueSize, OutPutStreamHandle outPutStreamHandle, ExecutorService executorService){
        Logger logger = LoggerInit.getLogger();
        logger.maxQueueSize=maxQueueSize;
        logger.outPutLogHelper=new OutPutLogHelper(executorService,outPutStreamHandle);
        logger.inUseQueue=InUseQueue.Q_ONE;
        return logger;
    }

    public static Logger getInstance(){
        return LoggerInit.getLogger();
    }

      private static class LoggerInit{
        private static  Logger logger=new Logger();
        private static Logger getLogger(){
            return logger;
        }
    }

    public  void shutdown(){
        outPutLogHelper.shutDown();
    }

    public  synchronized void addLog(LogLevel logLevel, String message){
        BlockingQueue<LogEvent> inUseQueue = getInUseQueue();
        inUseQueue.add(new LogEvent(message,logLevel));
        checkAndOutPutLog(inUseQueue);
    }

    private synchronized void checkAndOutPutLog(BlockingQueue<LogEvent> blockingQueue) {
        if(blockingQueue.size()>maxQueueSize){
            inUseQueue= inUseQueue.equals(InUseQueue.Q_ONE)?InUseQueue.Q_TWO:InUseQueue.Q_ONE;
            List<String> messages = blockingQueue.stream()
                    .map(x -> x.getMessage() + " " + x.getLogLevel())
                    .collect(Collectors.toList());
            outPutLogHelper.outputLog(messages);
            blockingQueue.clear();
        }
    }

    private BlockingQueue<LogEvent> getInUseQueue() {
        if (inUseQueue.equals(InUseQueue.Q_ONE))
            return logEvents1;
        return logEvents2;
    }

    enum InUseQueue{
        Q_ONE,Q_TWO;
    }
}
