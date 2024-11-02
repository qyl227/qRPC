package com.qyling.qRPC_simple.exception;

/**
 * @author qyling
 * @date 2024/11/2 7:37
 */
public class HttpException extends RuntimeException {
    public HttpException() {
        super();
    }

    public HttpException(String msg) {
        super(msg);
    }
}
