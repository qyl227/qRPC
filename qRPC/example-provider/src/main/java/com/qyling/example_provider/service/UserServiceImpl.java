package com.qyling.example_provider.service;

import com.qyling.example_common.model.User;
import com.qyling.example_common.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qyling
 * @date 2024/10/30 17:47
 */
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(User user) {
        if (user != null) log.info("hello from {}", user.getUsername());
        return "hello from qRPC";
    }
}
