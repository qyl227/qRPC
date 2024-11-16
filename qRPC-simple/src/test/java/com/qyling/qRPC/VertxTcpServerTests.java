package com.qyling.qRPC;

import com.qyling.qRPC.server.TcpServer;

/**
 * @author qyling
 * @date 2024/10/31 7:34
 */
public class VertxTcpServerTests {
    public static void main(String[] args) {
        TcpServer tcpServer = new TcpServer();
        tcpServer.doStart();
    }
}
