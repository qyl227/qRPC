package com.qyling.qRPC.protocol;

import com.qyling.qRPC.config.RpcConstant;
import io.vertx.core.buffer.Buffer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义协议：消息传输对象
 *
 * @author qyling
 * @date 2024/11/9 11:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 魔数
     */
    private byte magic;
    /**
     * 版本号
     */
    private byte version;
    /**
     * 请求体的序列化方式
     */
    private byte serializationID;
    /**
     * 1为请求、2为响应
     */
    private byte type;
    /**
     * 标记处理结果，类似http的响应状态码
     * 1为未成功，2为失败
     */
    private byte status;
    /**
     * 请求ID
     */
    private long requestID;
    /**
     * 请求体长度
     */
    private int length;
    /**
     * 请求体（实际传输的数据的序列化流）
     */
    private byte[] body;

    /**
     * 将message转为buffer
     * @param message
     * @return
     */
    public static Buffer messageToBuffer(Message message) {
        Buffer buffer = Buffer.buffer()
                .appendByte(message.getMagic())
                .appendByte(message.getVersion())
                .appendByte(message.getSerializationID())
                .appendByte(message.getType())
                .appendByte(message.getStatus())
                .appendLong(message.getRequestID())
                .appendInt(message.getLength());
        if (message.getBody() != null) {
            buffer.appendBytes(message.getBody());
        }
        buffer.appendString(RpcConstant.END_SIGN); // 结束标识
        return buffer;
    }

    /**
     * 将buffer转为message
     * @param buffer
     * @return
     */
    public static Message bufferToMessage(Buffer buffer) {
        // 按照协议顺序读取信息，并装载到对应的类型中
        byte magic = buffer.getByte(0);
        byte version = buffer.getByte(1);
        byte serializationID = buffer.getByte(2);
        byte type = buffer.getByte(3);
        byte status = buffer.getByte(4);
        long requestID = buffer.getLong(5);
        int length = buffer.getInt(13);
        // 解决半包、粘包问题
        byte[] body = buffer.getBytes(17, 17 + length);
        // 构造消息对象
        Message message = Message.builder()
                .magic(magic)
                .version(version)
                .serializationID(serializationID)
                .type(type)
                .status(status)
                .requestID(requestID)
                .length(length)
                .body(body)
                .build();
        return message;
    }

    /**
     * 请求处理是否成功
     * @return
     */
    public boolean isOk() {
        return status == MessageHandleStatusEnum.SUCCEED.getStatus();
    }

    /**
     * 拷贝message的属性到另一个message中
     * @param src
     * @param dst
     */
    public static void copyProperties(Message src, Message dst) {
        byte magic = src.getMagic();
        byte version = src.getVersion();
        byte serializationID = src.getSerializationID();
        byte type = src.getType();
        byte status = src.getStatus();
        long requestID = src.getRequestID();
        int length = src.getLength();
        byte[] body = src.getBody();

        dst.setMagic(magic);
        dst.setVersion(version);
        dst.setSerializationID(serializationID);
        dst.setType(type);
        dst.setStatus(status);
        dst.setRequestID(requestID);
        dst.setLength(length);
        dst.setBody(body);
    }
}
