package com.qyling.qRPC_simple.retry;

import com.github.rholder.retry.*;
import com.qyling.qRPC_simple.retry.RetryStrategy;
import com.qyling.qRPC_simple.retry.RetryUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 渐增策略：时间线性增长
 * @author qyling
 * @date 2024/11/11 15:56
 */
@Slf4j
public class IncrementalWaitStrategy implements RetryStrategy {
    @Override
    public <T> T doTask(Supplier<T> task) {
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfException()  // 如果出现异常就重试
                .withWaitStrategy(WaitStrategies.incrementingWait(1
                        , TimeUnit.SECONDS, 1, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最多尝试3次
                .build();

        return RetryUtils.runTask(task, retryer);
    }
}
