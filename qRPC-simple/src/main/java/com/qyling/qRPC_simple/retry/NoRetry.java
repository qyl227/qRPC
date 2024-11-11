package com.qyling.qRPC_simple.retry;

import java.util.function.Supplier;

/**
 * 无任何重试机制
 * @author qyling
 * @date 2024/11/10 16:26
 */
public class NoRetry implements RetryStrategy{

    @Override
    public <T> T doTask(Supplier<T> task) {
        return task.get();
    }
}
