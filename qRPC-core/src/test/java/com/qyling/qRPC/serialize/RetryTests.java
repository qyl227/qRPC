package com.qyling.qRPC.serialize;

import com.qyling.qRPC.model.RpcRequest;
import com.qyling.qRPC.protocol.Message;
import com.qyling.qRPC.fault.retry.ExponentialWaitStrategy;
import com.qyling.qRPC.fault.retry.RetryStrategy;
import com.qyling.qRPC.server.TcpClient;

/**
 * @author qyling
 * @date 2024/11/10 16:42
 */
public class RetryTests {
    public static void main(String[] args) {
        RetryStrategy retryStrategy = new ExponentialWaitStrategy();
        RpcRequest rpcRequest = new RpcRequest();
        Message message = retryStrategy.doTask(() -> {
            try {
                return TcpClient.sendRequest(rpcRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("message = " + message);
    }
}
