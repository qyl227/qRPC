package com.qyling.qRPC_simple.proxy;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfig;
import com.qyling.qRPC_simple.model.RpcRequest;
import com.qyling.qRPC_simple.model.RpcResponse;
import com.qyling.qRPC_simple.serialize.RpcSerializer;

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
        // TODO 注册中心获取 ip:port
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
        HttpResponse response = HttpRequest.post(rpcConfig.getUrl())
                .body(RpcSerializer.serialize(rpcRequest))
                .execute();
        RpcResponse rpcResponse = RpcSerializer.deserialize(response.bodyBytes(), RpcResponse.class);
        if (!response.isOk()) {
            throw new HttpException(rpcResponse.getErrMsg()); //
        }
        return rpcResponse.getResult();
    }
}
