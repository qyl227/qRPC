package com.qyling.qRPC_simple.exception;

/**
 * qRPC协议校验异常
 * @author qyling
 * @date 2024/11/9 13:15
 */
public class CheckFailedException extends RuntimeException{
    public CheckFailedException() {
        super();
    }

    public CheckFailedException(String msg) {
        super(msg);
    }
}
