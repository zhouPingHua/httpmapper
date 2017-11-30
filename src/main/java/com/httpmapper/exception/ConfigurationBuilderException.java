package com.httpmapper.exception;

/**
 * @author zph  on 2017/11/29
 */
public class ConfigurationBuilderException extends RuntimeException {

    public ConfigurationBuilderException() {
    }

    public ConfigurationBuilderException(String message) {
        super(message);
    }

    public ConfigurationBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationBuilderException(Throwable cause) {
        super(cause);
    }

    public ConfigurationBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
