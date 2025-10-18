package com.example;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<BigInteger> {
    // Порог: когда длина отрезка ≤ THRESHOLD — считаем последовательно без новых задач
    private static final int THRESHOLD = 1_000;

    private final int start;
    private final int end;

    public FactorialTask(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        this.start = 1;
        this.end = Math.max(1, n);
    }

    private FactorialTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected BigInteger compute() {
        int len = end - start + 1;
        if (len <= THRESHOLD) {
            BigInteger acc = BigInteger.ONE;
            for (int i = start; i <= end; i++) {
                acc = acc.multiply(BigInteger.valueOf(i));
            }
            return acc;
        }

        int mid = (start + end) >>> 1;
        FactorialTask left  = new FactorialTask(start, mid);
        FactorialTask right = new FactorialTask(mid + 1, end);

        // Work-first: правую часть считаем в текущем потоке, левую — форкаем
        left.fork();
        BigInteger rightRes = right.compute();
        BigInteger leftRes  = left.join();

        return leftRes.multiply(rightRes);
    }
}
