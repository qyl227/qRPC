package com.qyling.qRPC_simple.protocol;

import lombok.Getter;

/**
 * 消息处理状态枚举
 * @author qyling
 * @date 2024/11/9 16:00
 */
public enum MessageHandleStatusEnum {
    RUNNING((byte) 0),
    SUCCEED((byte) 1),
    FAILED((byte) 2);

    @Getter
    private final byte status;

    MessageHandleStatusEnum(byte status) {
        this.status = status;
    }
}
