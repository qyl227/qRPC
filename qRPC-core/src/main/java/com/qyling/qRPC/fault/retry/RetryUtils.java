package com.qyling.qRPC.fault.retry;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author qyling
 * @date 2024/11/11 16:14
 */
@Slf4j
public class RetryUtils {
    public static <T> T runTask(Supplier<T> task, Retryer<T> retryer) {
        AtomicInteger i = new AtomicInteger(0);
        T t = null;
        try {
            t = retryer.call(() -> {
                if (i.incrementAndGet() > 1) log.error("运行失败，正在进行第{}次重试", i.get() - 1);
                return task.get();
            });
        } catch (ExecutionException e) { // call() 代码块中抛出的异常会被封装为 ExecutionException
            throw new RuntimeException(e);
        } catch (RetryException e) { // 无法继续重试：达到最大重试次数 或 达到最大等待时间
            log.info("无法继续重试，服务降级");
            throw new RuntimeException(e);
        }
        return t;
    }
}
