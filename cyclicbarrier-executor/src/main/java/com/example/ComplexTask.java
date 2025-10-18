package com.example;

class ComplexTask {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public int execute() {
        int n = 100_000 + taskId * 1_000;
        long acc = 0;
        for (int i = 1; i <= n; i++) {
            acc += i % 97;
        }
        return (int) (acc % 1000);
    }
}
