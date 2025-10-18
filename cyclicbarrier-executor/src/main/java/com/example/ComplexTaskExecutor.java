package com.example;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

class ComplexTaskExecutor {
    private final int defaultTasks;

    public ComplexTaskExecutor(int defaultTasks) {
        this.defaultTasks = defaultTasks;
    }

    public void executeTasks(int numberOfTasks) {
        final int tasks = numberOfTasks > 0 ? numberOfTasks : defaultTasks;

        AtomicIntegerArray results = new AtomicIntegerArray(tasks);

        CyclicBarrier barrier = new CyclicBarrier(tasks, () -> {
            int sum = 0;
            for (int i = 0; i < tasks; i++) sum += results.get(i);
            System.out.println(Thread.currentThread().getName()
                    + " barrierAction -> combined result = " + sum);
        });

        ExecutorService pool = Executors.newFixedThreadPool(tasks);

        for (int i = 0; i < tasks; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    ComplexTask task = new ComplexTask(idx);
                    int r = task.execute();
                    results.set(idx, r);       // записали свой результат
                    barrier.await();           // ждём остальных
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    // Если кто-то упал/прервался — барьер сломан, просто выходим
                }
            });
        }
        pool.shutdown();
        try {
            pool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
