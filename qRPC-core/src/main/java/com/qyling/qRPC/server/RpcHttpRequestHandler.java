package com.qyling.qRPC.server;

import com.qyling.qRPC.model.RpcRequest;
import com.qyling.qRPC.model.RpcResponse;
import com.qyling.qRPC.register.LocalRegistry;
import com.qyling.qRPC.serialize.RpcSerializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射处理RPC请求
 * @author qyling
 * @date 2024/10/31 10:34
 */
public class RpcHttpRequestHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        RpcResponse rpcResponse = new RpcResponse();
        // 异步调用
        request.bodyHandler(body -> {
            RpcRequest rpcRequest = RpcSerializer.deserialize(body.getBytes(), RpcRequest.class);
            String clazzName = rpcRequest.getClazzName();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] args = rpcRequest.getArgs();

            try {
                Class<?> interfaceClazz = Class.forName(clazzName);
                // 获取实现类
                Class<?> clazz = LocalRegistry.get(interfaceClazz);
                // 反射调用
                Method method = clazz.getMethod(methodName, parameterTypes);
                Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), args);
                rpcResponse.setResult(result);
                rpcResponse.setResultType(result.getClass());
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | ClassNotFoundException e) {
                rpcResponse.setErrMsg(Arrays.toString(e.getStackTrace()));
                request.response()
                        .setStatusCode(500)
                        .end(Buffer.buffer(RpcSerializer.serialize(rpcResponse)));
                throw new RuntimeException(e);
            }
            request.response()
                    .setStatusCode(200)
                    .end(Buffer.buffer(RpcSerializer.serialize(rpcResponse)));
        });

    }
}