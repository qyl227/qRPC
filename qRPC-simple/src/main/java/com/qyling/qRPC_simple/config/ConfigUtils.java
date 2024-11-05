package com.qyling.qRPC_simple.config;

import cn.hutool.setting.dialect.Props;

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
        return getConfig(RpcConfigConstant.DEFAULT_CONFIG_PATH);
    }

    public static RpcConfig getConfig(String path) {
        return getConfig(path, RpcConfigConstant.DEFAULT_CONFIG_PREFIX);
    }
    public static RpcConfig getConfig(String path, String prefix) {
        if (rpcConfig == null) {
            return loadConfig(path, prefix);
        }
        return rpcConfig;
    }
}
