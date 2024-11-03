package com.qyling.example_provider;

import com.qyling.example_common.service.UserService;
import com.qyling.example_provider.service.UserServiceImpl;
import com.qyling.qRPC_simple.init.RpcProviderInit;
import com.qyling.qRPC_simple.register.LocalRegistry;

/**
 * @author qyling
 * @date 2024/11/2 8:57
 */
public class provider {
    public static void main(String[] args) {
        LocalRegistry.register(UserService.class, UserServiceImpl.class);
        RpcProviderInit.init();
    }
}
