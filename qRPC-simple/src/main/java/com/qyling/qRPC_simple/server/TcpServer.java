package com.qyling.qRPC_simple.server;

import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfig;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

/**
 * TCP服务器
 * @author qyling
 * @date 2024/11/9 10:52
 */
@Slf4j
public class TcpServer implements Server {
    public void doStart() {
        RpcConfig rpcConfig = ConfigUtils.getConfig();
        NetServer server = Vertx.vertx().createNetServer();

        // 设置连接处理器
        server.connectHandler(new RpcTcpRequestHandler());

        // 启动服务器并监听端口
        server.listen(rpcConfig.getPort(), result -> {
            if (result.succeeded()) {
                log.info("Server is now listening on port {}", rpcConfig.getPort());
            } else {
                log.error("Failed to bind server: " + result.cause());
            }
        });
    }
}
