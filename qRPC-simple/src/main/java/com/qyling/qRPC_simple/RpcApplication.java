package com.qyling.qRPC_simple;

import com.qyling.qRPC_simple.server.HttpServer;
import com.qyling.qRPC_simple.server.VertxWebServer;

/**
 * @author qyling
 * @date 2024/10/31 8:17
 */
public class RpcApplication {
    public static void main(String[] args) {
        HttpServer httpServer = new VertxWebServer();
        httpServer.doStart();
    }
}
