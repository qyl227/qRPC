package com.qyling.qRPC_simple.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 指数等待策略：时间呈指数增长
 * @author qyling
 * @date 2024/11/11 16:02
 */
@Slf4j
public class ExponentialWaitStrategy implements RetryStrategy{
    @Override
    public <T> T doTask(Supplier<T> task) {
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfException()  // 如果出现异常就重试
                // nextWaitTime = multiplier ^ (retryAttempt - 1)
                .withWaitStrategy(WaitStrategies.exponentialWait(2000, 1, TimeUnit.MINUTES))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();

        return RetryUtils.runTask(task, retryer);
    }
}
