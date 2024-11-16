package com.qyling.qRPC.protocol;

import lombok.Getter;

/**
 * 标明该消息是请求还是响应
 * @author qyling
 * @date 2024/11/9 15:55
 */
public enum MessageTypeEnum {
    REQUEST((byte) 1),
    RESPONSE((byte) 2);

    @Getter
    private final byte type;

    MessageTypeEnum(byte type) {
        this.type = type;
    }
}
