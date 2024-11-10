package com.qyling.qRPC_simple.serialize;

import lombok.Getter;

/**
 * 序列化方式枚举
 * @author qyling
 * @date 2024/11/9 14:38
 */
public enum SerializationEnum {
    JDK((byte) 0),
    HESSIAN((byte) 1),
    KRYO((byte) 2),
    JSON((byte) 3),
    OTHER((byte) 4);
    @Getter
    private final byte serializationID;

    SerializationEnum(byte serializationID) {
        this.serializationID = serializationID;
    }

    public static SerializationEnum getSerializationEnumByID(byte serializationID) {
        for (SerializationEnum value : SerializationEnum.values()) {
            if (value.getSerializationID() == serializationID) {
                return value;
            }
        }
        return null;
    }
}
