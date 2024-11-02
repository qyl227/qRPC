package com.qyling.qRPC_simple.serialize;


import com.qyling.qRPC_simple.model.RpcRequest;
import com.qyling.qRPC_simple.model.User;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qyling
 * @date 2024/10/31 7:51
 */
public class RpcSeralizerTests {
    public static void main(String[] args) {
        User user = User.builder()
                .username("Lost")
                .execMsg("run")
                .build();

        byte[] bytes = RpcSerializer.serialize(user);
        User u = RpcSerializer.deserialize(bytes);
        System.out.println(u);
    }
}
