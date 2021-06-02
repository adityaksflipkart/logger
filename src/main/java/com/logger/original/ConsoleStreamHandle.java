package com.logger.original;

public class ConsoleStreamHandle implements OutPutStreamHandle {
    public void openStream() {
        //return System.out;
    }

    public void closeStream() {
        System.out.close();
    }

    public void writeEvent(String event) {
        System.out.println(event);
    }
}
