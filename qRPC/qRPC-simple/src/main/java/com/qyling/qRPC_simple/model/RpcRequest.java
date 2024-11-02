package com.qyling.qRPC_simple.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author qyling
 * @date 2024/10/30 17:50
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private String clazzName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
}
