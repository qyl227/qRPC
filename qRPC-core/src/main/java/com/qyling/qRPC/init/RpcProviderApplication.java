package com.qyling.qRPC.init;

import com.qyling.qRPC.server.Server;
import com.qyling.qRPC.server.TcpServer;

/**
 * RPC提供者初始化类
 * @author qyling
 * @date 2024/10/31 8:17
 */
public class RpcProviderApplication {

    /**
     * RPC初始化方法
     */
    public static void run() {
//        // HTTP服务器
//        Server httpServer = new VertxWebHttpServer();
//        httpServer.doStart();

        // TCP服务器（自定义协议）
        Server server = new TcpServer(); //
        server.doStart();
    }
}
