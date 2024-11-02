package com.qyling.qRPC_simple.register;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心，通过接口获取实现类
 * @author qyling
 * @date 2024/11/2 16:12
 */
public class LocalRegistry {
    private static final ConcurrentHashMap<Class<?>, Class<?>> registry = new ConcurrentHashMap<>();

    /**
     * 通过接口获取实现类
     * @param interfaceClass 接口的Class对象
     * @return
     */
    public static Class<?> get(Class<?> interfaceClass) {
        return registry.get(interfaceClass);
    }

    /**
     * 创建接口和实现类的关系
     * @param interfaceClass 接口的Class对象
     * @param implementionClass 实现类的Class对象
     */
    public static void register(Class<?> interfaceClass, Class<?> implementionClass) {
        registry.put(interfaceClass, implementionClass);
    }
}
