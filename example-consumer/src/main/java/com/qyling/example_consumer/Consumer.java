package com.qyling.example_consumer;

import com.qyling.example_common.model.User;
import com.qyling.example_common.service.UserService;
import com.qyling.qRPC_simple.proxy.ProxyFactory;

/**
 * @author qyling
 * @date 2024/11/2 8:51
 */
public class Consumer {
    public static void main(String[] args) {
        UserService userService = ProxyFactory.getProxy(UserService.class);
        User user = userService.getUser(2L);
        System.out.println("user = " + user);
//        System.out.println(userService.sayHello(new User(2L, "Lost")));
    }
}
