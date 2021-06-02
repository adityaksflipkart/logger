package com.logger.original;

public class LogEvent {
    private String message;
    private LogLevel logLevel;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public LogEvent(String message, LogLevel logLevel) {
        this.message = message;
        this.logLevel = logLevel;
    }
}
