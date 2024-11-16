package com.qyling.qRPC.proxy;

import com.qyling.qRPC.config.ConfigUtils;
import com.qyling.qRPC.config.RpcConfig;
import com.qyling.qRPC.exception.QRPCException;
import com.qyling.qRPC.model.RpcRequest;
import com.qyling.qRPC.model.RpcResponse;
import com.qyling.qRPC.protocol.Message;
import com.qyling.qRPC.protocol.MessageTypeEnum;
import com.qyling.qRPC.fault.retry.RetryStrategy;
import com.qyling.qRPC.serialize.RpcSerializer;
import com.qyling.qRPC.server.TcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * 代理调用handler
 * @author qyling
 * @date 2024/10/30 18:27
 */
public class RpcProxyHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 获取参数类型
        Class<?>[] parameterTypes = Stream.of(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new); // 转换为 Class<?>[]
        // 构造 RpcRequest
        RpcRequest rpcRequest = RpcRequest.builder()
                .clazzName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(parameterTypes)
                .args(args)
                .build();
        RpcConfig rpcConfig = ConfigUtils.getConfig();
        // 发送TCP请求
        Message message = null;
        try {
            // 重试策略
            RetryStrategy retryStrategy = rpcConfig.getRetryStrategyObj();
            message = retryStrategy.doTask(() -> TcpClient.sendRequest(rpcRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (message == null) {
            throw new QRPCException("消息为空");
        }
        if (message.getType() != MessageTypeEnum.RESPONSE.getType()) {
            throw new QRPCException("非qRPC响应");
        }
        RpcResponse rpcResponse = RpcSerializer.deserialize(message.getBody(), RpcResponse.class);
        if (!message.isOk()) {
            throw new QRPCException(rpcResponse.getErrMsg());
        }
        return rpcResponse.getResult();
    }
}
