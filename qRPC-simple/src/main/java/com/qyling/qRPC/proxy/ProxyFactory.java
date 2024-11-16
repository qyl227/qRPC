package com.qyling.qRPC.proxy;

import com.qyling.qRPC.config.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * 代理工厂，获取代理对象
 * @author qyling
 * @date 2024/10/30 17:59
 */
@Slf4j
public class ProxyFactory {

    /**
     *
     * @param interfaceClass 接口的Class对象
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> interfaceClass) {
        if (ConfigUtils.getConfig().getMock()) {
            return getMockProxy(interfaceClass);
        }

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

    /**
     * Mock模式
     * @param interfaceClass
     * @return
     * @param <T>
     */
    private static <T> T getMockProxy(Class<T> interfaceClass) {
        log.info("现在处于Mock模式，将返回默认数据");
        try {
            // 创建并返回代理对象
            return (T) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    new RpcMockProxyHandler()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
