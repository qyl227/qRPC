package com.qyling.qRPC_simple.init;

import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfigConstant;
import com.qyling.qRPC_simple.serialize.RpcSerializer;
import com.qyling.qRPC_simple.server.HttpServer;
import com.qyling.qRPC_simple.server.VertxWebServer;

/**
 * RPC提供者初始化类
 * @author qyling
 * @date 2024/10/31 8:17
 */
public class RpcProviderInit {

    /**
     * RPC初始化方法
     */
    public static void init() {
        // Web服务器
        HttpServer httpServer = new VertxWebServer();
        httpServer.doStart();
    }
}
