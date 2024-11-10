package com.qyling.qRPC_simple.server;

/**
 * 服务器顶级接口
 * @author qyling
 * @date 2024/11/9 17:35
 */
public interface Server {
    /**
     * 启动服务器，监听对应端口
     */
    void doStart();
}
