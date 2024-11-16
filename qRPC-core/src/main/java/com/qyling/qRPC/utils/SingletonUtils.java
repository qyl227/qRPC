package com.qyling.qRPC.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @author qyling
 * @date 2024/11/10 15:58
 */
@Slf4j
public class SingletonUtils {
    /**
     * 单例获取配置的对象
     * @param t 要获取的对象
     * @param objStr 对象的全限定类名
     * @param interfaceClazz 对象必须实现的接口
     * @return
     * @param <T>
     */
    public static <T> T getSingleton(T t, String objStr, Class<?> interfaceClazz) {
        if (t == null) {
            synchronized (interfaceClazz) {
                if (t == null) {
                    try {
                        Class<?> clazz = Class.forName(objStr);
                        Class<?>[] interfaces = clazz.getInterfaces();
                        // 是否实现 Serializable
                        boolean result = false;
                        for (Class<?> anInterface : interfaces) {
                            if (anInterface.getName().equals(interfaceClazz.getName())) {
                                result = true;
                            }
                        }
                        if (!result) {
                            log.error("指定的类未实现接口：{}", interfaceClazz.getName());
                            throw new RuntimeException();
                        }
                        // 反射构造实例
                        t = (T) clazz.getDeclaredConstructor().newInstance();
                    } catch (ClassNotFoundException e) {
                        log.error("未找到类：{}", t);
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                             InstantiationException e) {
                        log.error("对象初始化失败");
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return t;
    }
}
