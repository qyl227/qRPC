package com.qyling.qRPC_simple.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qyling
 * @date 2024/10/30 16:25
 */
@Data
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private String username;
    private String execMsg;
}
