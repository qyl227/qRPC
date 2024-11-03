package com.qyling.qRPC_simple.init;

import com.qyling.qRPC_simple.config.ConfigUtils;
import com.qyling.qRPC_simple.config.RpcConfigConstant;
import com.qyling.qRPC_simple.server.HttpServer;
import com.qyling.qRPC_simple.server.VertxWebServer;

/**
 * RPC提供者初始化类
 * @author qyling
 * @date 2024/11/3 13:22
 */
public class RpcConsumerInit {
    /**
     * RPC初始化方法
     */
    public static void init() {
        init(RpcConfigConstant.DEFAULT_CONFIG_PATH);
    }

    public static void init(String path) {
        init(path, RpcConfigConstant.DEFAULT_CONFIG_PREFIX);
    }

    /**
     *
     * @param path 配置文件路径
     * @param prefix 读取的配置前缀
     */
    public static void init(String path, String prefix) {
        ConfigUtils.loadConfig(path, prefix);
    }
}