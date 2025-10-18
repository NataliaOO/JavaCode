package com.example;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final long id;
    private long balance;
    private final ReentrantLock lock = new ReentrantLock();

    BankAccount(long id, long initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("initialBalance < 0");
        this.id = id;
        this.balance = initialBalance;
    }

    long getId() {
        return id;
    }

    void lock() {
        lock.lock();
    }

    void unlock() {
        lock.unlock();
    }

    void deposit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    boolean withdraw(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        lock.lock();
        try {
            if (balance < amount) return false;
            balance -= amount;
            return true;
        } finally {
            lock.unlock();
        }
    }

    long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
