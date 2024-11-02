package com.qyling.example_common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qyling
 * @date 2024/11/2 18:21
 */
@AllArgsConstructor
@Data
public class User implements Serializable {
    private String username;
}
