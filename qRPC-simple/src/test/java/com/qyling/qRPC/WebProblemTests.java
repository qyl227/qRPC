package com.qyling.qRPC;

import com.qyling.qRPC.config.RpcConstant;
import com.qyling.qRPC.protocol.Message;
import com.qyling.qRPC.protocol.MessageHandleStatusEnum;
import com.qyling.qRPC.protocol.MessageTypeEnum;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

/**
 * 半包、粘包问题测试类
 *
 * @author qyling
 * @date 2024/11/13 20:32
 */
@Slf4j
public class WebProblemTests {
    public static void main(String[] args) {
        doListen();
        doSend();
    }

    private static void doListen() {
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(socket -> {
            String testMessage = "Hello, server!Hello, server!Hello, server!Hello, server!";
            int messageLength = testMessage.getBytes().length;
            // 设置RecordParser来解析数据
            RecordParser parser = RecordParser.newDelimited(RpcConstant.END_SIGN, socket);

            parser.handler(buffer -> {
                // 处理解析到的完整消息
                Message message = Message.bufferToMessage(buffer);
                byte[] data = message.getBody();
                if (data.length < messageLength) {
                    System.out.println("半包, length = " + data.length);
                    return;
                }
                if (data.length > messageLength) {
                    System.out.println("粘包, length = " + data.length);
                    return;
                }
                String str = new String(data);
                System.out.println(str);
                if (testMessage.equals(str)) {
                    System.out.println("good");
                }
            });

            parser.exceptionHandler(ex -> {
                // 处理异常
                System.out.println("Error parsing message: " + ex.getMessage());
            });

            // 直接传给RecordParser进行解析
            socket.handler(parser::handle);
            socket.write("这是响应数据");
        });
        // 启动 TCP 服务器并监听指定端口
        server.listen(8888, result -> {
            if (result.succeeded()) {
                log.info("TCP server started on port " + 8888);
            } else {
                log.info("Failed to start TCP server: " + result.cause());
            }
        });
    }

    private static void doSend() {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
            String data = "Hello, server!Hello, server!Hello, server!Hello, server!";
            byte[] bytes = data.getBytes();
            Message message = Message.builder()
                    .magic(RpcConstant.MAGIC)
                    .version(RpcConstant.VERSION)
                    .serializationID((byte) 1)
                    .type(MessageTypeEnum.RESPONSE.getType())
                    .status(MessageHandleStatusEnum.RUNNING.getStatus())
                    .requestID(2L)
                    .body(bytes)
                    .length(bytes.length)
                    .build();
            if (result.succeeded()) {
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();
                for (int i = 0; i < 1000; i++) {
                    // 发送数据

                    socket.write(Message.messageToBuffer(message).appendString(RpcConstant.END_SIGN));
                }
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                    socket.close();
                });
            } else {
                System.err.println("Failed to connect to TCP server");
            }
        });
    }
}




