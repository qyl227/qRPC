package com.qyling.qRPC_simple.proxy;

import java.lang.reflect.Proxy;

/**
 * 获取代理对象
 * @author qyling
 * @date 2024/10/30 17:59
 */
public class ProxyFactory {

    /**
     *
     * @param interfaceClass 接口的Class对象
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> interfaceClass) {
        try {
            // 创建并返回代理对象
            return (T) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new RpcProxyHandler()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
