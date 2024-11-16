package com.qyling.qRPC.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    private String clazzName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
}
