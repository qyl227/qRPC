package com.qyling.qRPC_simple.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author qyling
 * @date 2024/10/30 17:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private Object result;
    private Class<?> resultType;
    private String errMsg;
}
