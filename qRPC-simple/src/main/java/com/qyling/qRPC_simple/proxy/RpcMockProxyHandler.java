package com.qyling.qRPC_simple.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author qyling
 * @date 2024/11/3 17:50
 */
public class RpcMockProxyHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        return getDefaultReturn(returnType);
    }

    private Object getDefaultReturn(Class<?> returnType) {
        if (returnType == Byte.class || returnType == Integer.class) {
            return 0;
        } else if (returnType == Character.class) {
            return 'a';
        } else if (returnType == Long.class) {
            return 0L;
        } else if (returnType == Float.class) {
            return 0.0f;
        } else if (returnType == Double.class) {
            return 0.0;
        } else if (returnType == Boolean.class) {
            return false;
        } else if (returnType == Object.class) {
            return null;
        }
        return null;
    }
}
