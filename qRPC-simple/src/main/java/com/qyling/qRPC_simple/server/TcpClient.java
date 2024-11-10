package com.qyling.qRPC_simple.server;

import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfig;
import com.qyling.qRPC_simple.config.RpcConstant;
import com.qyling.qRPC_simple.model.RpcRequest;
import com.qyling.qRPC_simple.model.RpcResponse;
import com.qyling.qRPC_simple.protocol.Message;
import com.qyling.qRPC_simple.protocol.MessageHandleStatusEnum;
import com.qyling.qRPC_simple.protocol.MessageTypeEnum;
import com.qyling.qRPC_simple.serialize.RpcSerializer;
import com.qyling.qRPC_simple.serialize.SerializerUtils;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TCP客户端
 * @author qyling
 * @date 2024/11/9 12:41
 */
@Slf4j
public class TcpClient {
    /**
     * 发起请求
     * @param rpcRequest
     * @return
     * @throws Exception
     */
    public static Message sendRequest(RpcRequest rpcRequest) throws Exception {
        // 创建一个 CompletableFuture 来等待响应
        CompletableFuture<Message> future = new CompletableFuture<>();

        // 创建一个 Vert.x 实例
        Vertx vertx = Vertx.vertx();
        RpcConfig rpcConfig = ConfigUtils.getConfig();

        // 创建一个 TCP 客户端
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
                        .version(RpcConstant.version)
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
        return future.get();
    }
}
