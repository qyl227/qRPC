package com.qyling.qRPC.fault.retry;

import java.util.function.Supplier;

/**
 * 重试策略顶级接口
 * @author qyling
 * @date 2024/11/10 16:16
 */
public interface RetryStrategy {
    <T> T doTask(Supplier<T> task);
}
