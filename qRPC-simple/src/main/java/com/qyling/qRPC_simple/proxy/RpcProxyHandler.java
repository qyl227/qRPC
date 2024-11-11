package com.qyling.qRPC_simple.proxy;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfig;
import com.qyling.qRPC_simple.exception.QRPCException;
import com.qyling.qRPC_simple.model.RpcRequest;
import com.qyling.qRPC_simple.model.RpcResponse;
import com.qyling.qRPC_simple.protocol.Message;
import com.qyling.qRPC_simple.protocol.MessageTypeEnum;
import com.qyling.qRPC_simple.retry.RetryStrategy;
import com.qyling.qRPC_simple.serialize.RpcSerializer;
import com.qyling.qRPC_simple.serialize.SerializerUtils;
import com.qyling.qRPC_simple.server.TcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
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
            RetryStrategy retryStrategy = rpcConfig.getRetryStrategyObj();
            message = retryStrategy.doTask(() -> TcpClient.sendRequest(rpcRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (message == null) {
            throw new RuntimeException("消息为空");
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
