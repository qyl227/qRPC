package com.qyling.qRPC_simple.config;

import cn.hutool.setting.dialect.Props;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * RPC配置工具类
 * @author qyling
 * @date 2024/11/3 10:22
 */
public class ConfigUtils {
    private static RpcConfig rpcConfig;

    /**
     * 读取配置信息，默认读取 类路径下的 config.properties 和 rpc. 前缀
     * @return
     */
    public static RpcConfig loadConfig() {
        return loadConfig(RpcConfigConstant.DEFAULT_CONFIG_PATH);
    }

    public static RpcConfig loadConfig(String path) {
        return loadConfig(path, RpcConfigConstant.DEFAULT_CONFIG_PREFIX);
    }

    /**
     *
     * @param path 配置文件路径
     * @param prefix 读取的配置前缀
     * @return
     */
    public static RpcConfig loadConfig(String path, String prefix) {
        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream("config.properties");
        if (inputStream != null) {
            Props props = new Props();
            try {
                props.load(inputStream);
            } catch (IOException e) {
                rpcConfig = new RpcConfig();
                throw new RuntimeException(e);
            }
            rpcConfig = props.toBean(RpcConfig.class, prefix);
        } else {
            rpcConfig = new RpcConfig();
        }
        return rpcConfig;
    }

    public static RpcConfig getConfig() {
        if (rpcConfig == null) {
            throw new IllegalStateException("请先加载配置");
        }
        return rpcConfig;
    }
}
