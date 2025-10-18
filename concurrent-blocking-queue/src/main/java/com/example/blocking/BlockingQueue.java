package com.example.blocking;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingQueue<T> {
    private final Deque<T> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    public void enqueue(T item) throws InterruptedException {
        if (item == null) throw new NullPointerException("item");
        synchronized (this) {
            while (queue.size() == capacity) {
                this.wait();
            }
            queue.addLast(item);
            this.notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }
            T val = queue.removeFirst();
            this.notifyAll();
            return val;
        }
    }

    public synchronized int size() {
        return queue.size();
    }
}
