package com.qyling.qRPC.serialize;

import com.qyling.qRPC.config.ConfigUtils;

/**
 * 序列化器工具类
 * @author qyling
 * @date 2024/11/9 14:10
 */
public class SerializerUtils {
    public static Serializer getSerializer(byte serializationID) {
        return getSerializer(SerializationEnum.getSerializationEnumByID(serializationID));
    }

    public static Serializer getSerializer(SerializationEnum serializationEnum) {
        if (serializationEnum == SerializationEnum.JDK) {
            return new JDKSerializer();
        } else if (serializationEnum == SerializationEnum.HESSIAN) {
            return new HessianSerializer();
        } else if (serializationEnum == SerializationEnum.KRYO) {
            return new KryoSerializer();
        } else if (serializationEnum == SerializationEnum.JSON) {
            return new JSONSerializer();
        } else if (serializationEnum == SerializationEnum.OTHER) {
            return ConfigUtils.getConfig().getSerializerObj();
        }
        return null;
    }
}
