package com.qyling.qRPC_simple.config;

import lombok.Data;

/**
 * @author qyling
 * @date 2024/11/3 10:25
 */
@Data
public class RpcConfig {
    private String host = "localhost";
    private Integer port = 8200;
    private Boolean mock = false;

    public String getUrl() {
        return host + ":" + port;
    }
}
