package com.logger.original;

public interface OutPutStreamHandle {

    public void openStream();
    public void closeStream();
    public void writeEvent(String event);

}
