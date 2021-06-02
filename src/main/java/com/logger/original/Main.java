package com.logger.original;

import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.initLogger(100, new ConsoleStreamHandle(), Executors.newFixedThreadPool(10));
        logger.addLog(LogLevel.INFO,"test" );

    }
}
