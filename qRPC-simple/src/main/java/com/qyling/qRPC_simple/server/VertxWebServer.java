package com.qyling.qRPC_simple.server;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertx http服务器
 * @author qyling
 * @date 2024/10/31 8:05
 */
@Slf4j
public class VertxWebServer implements HttpServer {
    @Override
    public void doStart() {
        // 创建vertx http服务器
        Vertx.vertx().createHttpServer()
                .requestHandler(new RpcHttpRequestHandler())
                .listen(8200, result -> {
                    if (result.succeeded()) {
                        log.info("Server is now listening on port 8200");
                    } else {
                        log.error("Failed to bind server: " + result.cause());
                    }
                });
    }
}
