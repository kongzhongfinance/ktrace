package com.kongzhong.finance.ktrace.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2017/8/30.
 */
public class TraceTest {

    ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    public void getCurrentRequestId() throws InterruptedException, ExecutionException {

        List<FutureTask<String>> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            FutureTask<String> futureTask = new FutureTask<>(Trace::getCurrentRequestId);
            executorService.submit(futureTask);
            list.add(futureTask);
        }

        executorService.shutdown();
        //坐等线程执行结束
        while (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS));

        list.forEach(item -> {
            try {
                System.out.println(item.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
