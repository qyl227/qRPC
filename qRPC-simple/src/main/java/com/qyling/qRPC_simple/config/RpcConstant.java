package com.qyling.qRPC_simple.config;

import com.qyling.qRPC_simple.serialize.SerializationEnum;

/**
 * @author qyling
 * @date 2024/11/3 10:49
 */
public interface RpcConstant {
    String DEFAULT_CONFIG_PATH = "config.properties";
    String DEFAULT_CONFIG_PREFIX = "rpc";
    String DEFAULT_HOST = "localhost";
    Integer DEFAULT_PORT = 8200;
    /**
     * qRPC魔数
     */
    Byte MAGIC = (byte) 0x5A;
    /**
     * qRPC服务端当前版本
     */
    Byte VERSION = 1;
    /**
     * qRPC默认序列化器
     */
    Byte DEFAULT_SERIALIZATION = SerializationEnum.JDK.getSerializationID();

}
