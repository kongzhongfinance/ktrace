package com.kongzhong.finance.ktrace.core;


import com.kongzhong.finance.ktrace.core.config.AppConfiguration;
import com.kongzhong.finance.ktrace.core.config.RequestIdFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IFT8 on 17/4/2.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Trace {
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();

    private static TraceContext getContext() {
        TraceContext context = CONTEXT.get();
        if (context == null) {
            CONTEXT.set(new TraceContext());
        }
        return CONTEXT.get();
    }

    /**
     * 获取AppId
     */
    public static String getAppId() {
        return AppConfiguration.getAppId();
    }

    /**
     * 请求ID
     * e.g.: -5794346442721028506|1.1
     */
    private static String formatRequestId() {
        return getContext().getTraceId() + "|" + getContext().getSpanId();
    }

    /**
     * 获取当前RequestId
     */
    public static String getCurrentRequestId() {
        //没有就生成一个
        if (getContext().getTraceId() == null || getContext().getTraceId().length() == 0) {
            getContext().setTraceId(RequestIdFactory.generateRequestId());
        }
        return formatRequestId();
    }

    /**
     * 继续跟踪（跨服务传递requestId）
     */
    public static void continueTrace(String requestId, String parentSpanId) {
        if (requestId == null) {
            CONTEXT.remove();
            return;
        }
        getContext().setTraceId(parseTraceId(requestId));
        getContext().setRecvTime(System.currentTimeMillis());
        getContext().setParentSpanId(parentSpanId);
        if (parentSpanId != null) {
            getContext().setSpanId(parentSpanId + ".1");
        }
    }

    public static void continueTrace(String requestId) {
        continueTrace(requestId, null);
    }

    public static String getSpanId() {
        return getContext().getSpanId();
    }

    public static String parseParentSpanId(String requestId) {
        if (requestId == null) {
            return null;
        }
        String[] split = requestId.split("\\|");
        if (split.length < 1) {
            return getSpanId();
        }
        return split[1];
    }

    public static String parseTraceId(String requestId) {
        if (requestId == null) {
            return null;
        }
        String[] split = requestId.split("\\|");
        if (split.length < 1) {
            return null;
        }
        return split[0];
    }
}
