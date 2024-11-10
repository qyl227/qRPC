package com.qyling.qRPC_simple.server;

import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfig;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertx http服务器
 * @author qyling
 * @date 2024/10/31 8:05
 */
@Slf4j
public class VertxWebHttpServer implements HttpServer {
    @Override
    public void doStart() {
        RpcConfig rpcConfig = ConfigUtils.getConfig();
        // 创建vertx http服务器
        Vertx.vertx().createHttpServer()
                .requestHandler(new RpcHttpRequestHandler())
                .listen(rpcConfig.getPort(), result -> {
                    if (result.succeeded()) {
                        log.info("Server is now listening on port {}", rpcConfig.getPort());
                    } else {
                        log.error("Failed to bind server: " + result.cause());
                    }
                });
    }
}
