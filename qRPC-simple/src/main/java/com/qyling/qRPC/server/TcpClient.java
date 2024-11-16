package com.qyling.qRPC.server;

import com.qyling.qRPC.config.ConfigUtils;
import com.qyling.qRPC.config.RpcConfig;
import com.qyling.qRPC.config.RpcConstant;
import com.qyling.qRPC.model.RpcRequest;
import com.qyling.qRPC.protocol.Message;
import com.qyling.qRPC.protocol.MessageHandleStatusEnum;
import com.qyling.qRPC.protocol.MessageTypeEnum;
import com.qyling.qRPC.serialize.RpcSerializer;
import com.qyling.qRPC.serialize.SerializerUtils;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TCP客户端
 * @author qyling
 * @date 2024/11/9 12:41
 */
@Slf4j
public class TcpClient {
    private static AtomicInteger i = new AtomicInteger(1);
    /**
     * 发起请求
     * @param rpcRequest
     * @return
     * @throws Exception
     */
    public static Message sendRequest(RpcRequest rpcRequest){
        if (i.getAndIncrement() <= 2) {
            throw new RuntimeException();
        }
        // 创建一个 CompletableFuture 来等待响应
        CompletableFuture<Message> future = new CompletableFuture<>();

        // Vert.x 实例
        Vertx vertx = Vertx.vertx();
        RpcConfig rpcConfig = ConfigUtils.getConfig();

        // TCP 客户端
        NetClient client = vertx.createNetClient();

        // 连接到服务器
        client.connect(rpcConfig.getPort(), rpcConfig.getHost(), result -> {
            if (result.succeeded()) {
                NetSocket socket = result.result();
                log.info("成功连接到服务器: {}:{}", rpcConfig.getHost(), rpcConfig.getPort());

                RpcSerializer.setSerializer(SerializerUtils.getSerializer(rpcConfig.getSerializationID()));
                byte[] body = RpcSerializer.serialize(rpcRequest);
                // 构造消息对象
                Message message = Message.builder()
                        .requestID(UUID.randomUUID().getLeastSignificantBits())
                        .magic(RpcConstant.MAGIC)
//                        .magic((byte) 1)
                        .version(RpcConstant.VERSION)
//                        .version((byte) 2)
                        .type(MessageTypeEnum.REQUEST.getType())
                        .serializationID(rpcConfig.getSerializationID())
                        .status(MessageHandleStatusEnum.RUNNING.getStatus())
                        .length(body.length)
                        .body(body)
                        .build();
                socket.write(Message.messageToBuffer(message));
                log.info("已发送消息: " + message);

                // 处理服务器返回的响应
                socket.handler(buffer -> {
                    Message src = Message.bufferToMessage(buffer);
                    if (src == null) {
                        future.completeExceptionally(new RuntimeException("消息为空"));
                    } else {
                        future.complete(src);
                    }
                    // 关闭连接
                    socket.close();
                });
            } else {
                log.error("连接失败: " + result.cause());
                future.completeExceptionally(result.cause());
            }
        });

        // 阻塞直到收到响应
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
