package com.qyling.qRPC.fault.retry;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 固定时间重试
 * @author qyling
 * @date 2024/11/10 18:15
 */
@Slf4j
public class FixedWaitStrategy implements RetryStrategy{
    @Override
    public <T> T doTask(Supplier<T> task) {
        return doTask(task, 1, TimeUnit.SECONDS, 3);
    }

    public <T> T doTask(Supplier<T> task, long sleepTime, TimeUnit timeUnit, int attemptNumber) {
        // 创建重试策略，最多重试2次，每次间隔1秒
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfException()  // 如果出现异常就重试
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTime, timeUnit)) // 等待1秒
                .withStopStrategy(StopStrategies.stopAfterAttempt(attemptNumber)) // 最多尝试3次
                .build();

        return RetryUtils.runTask(task, retryer);
    }
}
