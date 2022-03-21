package com.www.common.config.feign;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.www.common.config.filter.TraceIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>@Description hystrix策略，解决RequestContextHolder.getRequestAttributes()=null问题 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/21 21:34 </p>
 */
@Slf4j
@Primary
@Component
@ConditionalOnClass(HystrixConcurrencyStrategy.class)
public class FeignHystrixStrategy extends HystrixConcurrencyStrategy {
    /** Hystrix当前策略 **/
    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;
    /**
     * <p>@Description 构造方法，配置Hystrix策略 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     * @return
     */
    public FeignHystrixStrategy() {
        try {
            this.hystrixConcurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
            if (this.hystrixConcurrencyStrategy instanceof FeignHystrixStrategy) {
                return;
            }
            HystrixCommandExecutionHook commandExecutionHook =
                    HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy =
                    HystrixPlugins.getInstance().getPropertiesStrategy();
            HystrixPlugins.reset();
            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
            log.info("注册Hystrix并发策略");
        } catch (Exception e) {
            log.error("无法注册Hystrix并发策略", e);
        }
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return new WrappedCallable<>(callable, requestAttributes);
    }
    /**
     * <p>@Description 获取线程池 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     * @param threadPoolKey
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @return java.util.concurrent.ThreadPoolExecutor
     */
    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return this.hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue);
    }
    /**
     * <p>@Description 获取线程池 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     * @param threadPoolKey
     * @param threadPoolProperties
     * @return java.util.concurrent.ThreadPoolExecutor
     */
    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return this.hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties);
    }
    /**
     * <p>@Description 获取线程池队列 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     * @param maxQueueSize
     * @return java.util.concurrent.BlockingQueue<java.lang.Runnable>
     */
    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return this.hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize);
    }
    /**
     * <p>@Description 获取可用请求 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     * @param rv
     * @return com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable<T>
     */
    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return this.hystrixConcurrencyStrategy.getRequestVariable(rv);
    }
    /**
     * <p>@Description 线程 </p>
     * <p>@Version 1.0 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 21:51 </p>
     */
    static class WrappedCallable<T> implements Callable<T> {
        private final Callable<T> target;
        private final RequestAttributes requestAttributes;
        private String traceId;

        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes) {
            this.target = target;
            this.traceId = MDC.get(TraceIdFilter.TRACE_ID);//获取当前线程的日志跟踪ID
            this.requestAttributes = requestAttributes;
        }
        /**
         * <p>@Description 线程执行 </p>
         * <p>@Author www </p>
         * <p>@Date 2022/1/21 21:54 </p>
         * @return T
         */
        @Override
        public T call() throws Exception {
            try {
                MDC.put(TraceIdFilter.TRACE_ID,traceId);//添加日志跟踪ID，必须有，否则feign服务提供方会缺少日志跟踪ID
                RequestContextHolder.setRequestAttributes(requestAttributes);
                return target.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
