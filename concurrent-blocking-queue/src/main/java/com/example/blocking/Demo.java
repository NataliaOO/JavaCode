package com.example.blocking;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> q = new BlockingQueue<>(5);

        Runnable producer = () -> {
            for (int i = 1; i <= 20; i++) {
                try {
                    q.enqueue(i);
                    System.out.println("produced " + i + ", size=" + q.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        Runnable consumer = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer v = q.dequeue();
                    System.out.println(Thread.currentThread().getName() + " consumed " + v + ", size=" + q.size());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread p1 = new Thread(producer, "producer-1");
        Thread c1 = new Thread(consumer, "consumer-1");
        Thread c2 = new Thread(consumer, "consumer-2");

        p1.start(); c1.start(); c2.start();

        p1.join();               // ждём завершения производства
        Thread.sleep(500);       // даём потребителям доесть
        c1.interrupt(); c2.interrupt();
    }
}