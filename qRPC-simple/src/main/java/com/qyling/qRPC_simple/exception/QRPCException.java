package com.qyling.qRPC_simple.exception;

/**
 * qRPC框架异常
 * @author qyling
 * @date 2024/11/10 11:05
 */
public class QRPCException extends RuntimeException{
    public QRPCException() {
        super();
    }

    public QRPCException(String message) {
        super(message);
    }
}
