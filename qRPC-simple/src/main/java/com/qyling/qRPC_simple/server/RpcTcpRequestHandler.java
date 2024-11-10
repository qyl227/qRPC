package com.qyling.qRPC_simple.server;

import com.qyling.qRPC_simple.config.RpcConstant;
import com.qyling.qRPC_simple.exception.CheckFailedException;
import com.qyling.qRPC_simple.model.RpcRequest;
import com.qyling.qRPC_simple.model.RpcResponse;
import com.qyling.qRPC_simple.protocol.Message;
import com.qyling.qRPC_simple.protocol.MessageHandleStatusEnum;
import com.qyling.qRPC_simple.protocol.MessageTypeEnum;
import com.qyling.qRPC_simple.register.LocalRegistry;
import com.qyling.qRPC_simple.serialize.RpcSerializer;
import com.qyling.qRPC_simple.serialize.SerializerUtils;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 自定义协议请求处理器
 * @author qyling
 * @date 2024/11/9 14:08
 */
@Slf4j
public class RpcTcpRequestHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket socket) {
        log.info("新连接到达: " + socket.remoteAddress());

        // 读取客户端数据
        socket.handler(buffer -> {
            // 构造消息对象
            Message message = Message.builder()
                    .magic(RpcConstant.MAGIC)
                    .version(RpcConstant.version)
                    .type(MessageTypeEnum.RESPONSE.getType())
                    .status(MessageHandleStatusEnum.RUNNING.getStatus())
                    .build();
            RpcResponse rpcResponse = new RpcResponse();
            byte serializationID = RpcConstant.DEFAULT_SERIALIZATION;
            long requestID = 0L;
            try {
                // 通过buffer获取封装的请求消息对象
                Message requestMessage = Message.bufferToMessage(buffer);

                requestID = requestMessage.getRequestID();
                serializationID = requestMessage.getSerializationID();
                byte magic = requestMessage.getMagic();
                if (magic != RpcConstant.MAGIC) {
                    throw new CheckFailedException("消息非qRPC传输协议，请重新检查");
                }
                byte version = requestMessage.getVersion();
                if (version != RpcConstant.version) {
                    throw new CheckFailedException("qRPC客户端版本：" + version + " 与服务端版本：" + RpcConstant.version + " 不匹配");
                }
                byte type = requestMessage.getType();
                if (type != MessageTypeEnum.REQUEST.getType()) {
                    throw new CheckFailedException("非qRPC请求");
                }
                byte status = requestMessage.getStatus();
                int length = requestMessage.getLength();
                byte[] body = requestMessage.getBody();

                RpcSerializer.setSerializer(SerializerUtils.getSerializer(serializationID));
                RpcRequest rpcRequest = RpcSerializer.deserialize(body, RpcRequest.class);
                handleRpcRequest(rpcRequest, rpcResponse, message);
                message.setStatus(MessageHandleStatusEnum.SUCCEED.getStatus());
            } catch (Exception e) {
                message.setStatus(MessageHandleStatusEnum.FAILED.getStatus());
                rpcResponse.setErrMsg(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                message.setRequestID(requestID);
                message.setSerializationID(serializationID);
                RpcSerializer.setSerializer(SerializerUtils.getSerializer(serializationID));
                byte[] bytes = RpcSerializer.serialize(rpcResponse);
                message.setLength(bytes.length);
                message.setBody(bytes);
                // 发送响应给客户端
                socket.write(Message.messageToBuffer(message));
            }
        });

//        // 处理连接关闭
//        socket.closeHandler(v -> {
//            log.info("连接已关闭: " + socket.remoteAddress());
//        });
    }

    /**
     * 处理RpcRequest
     * @param rpcRequest
     * @param rpcResponse
     * @param message
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void handleRpcRequest(RpcRequest rpcRequest, RpcResponse rpcResponse, Message message) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String clazzName = rpcRequest.getClazzName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        Class<?> interfaceClazz = Class.forName(clazzName);
        // 获取实现类
        Class<?> clazz = LocalRegistry.get(interfaceClazz);
        // 反射调用
        Method method = clazz.getMethod(methodName, parameterTypes);
        Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), args);
        rpcResponse.setResult(result);
        rpcResponse.setResultType(result.getClass());
    }
}
