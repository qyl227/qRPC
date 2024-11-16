package com.qyling.qRPC.server;

/**
 * @author qyling
 * @date 2024/10/31 8:18
 */
public interface HttpServer extends Server {
    /**
     * 启动HTTP服务器
     */
    void doStart();
}
