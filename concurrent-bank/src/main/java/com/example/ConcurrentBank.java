package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBank {
    private final AtomicLong idSeq = new AtomicLong(1);
    private final ConcurrentHashMap<Long, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(long initialBalance) {
        long id = idSeq.getAndIncrement();
        BankAccount acc = new BankAccount(id, initialBalance);
        accounts.put(id, acc);
        return acc;
    }

    public void transfer(BankAccount from, BankAccount to, long amount) {
        if (from == null || to == null) throw new IllegalArgumentException("account is null");
        if (from == to) return;
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");

        // фиксированный порядок захвата локов по id — чтобы избежать дедлоков
        BankAccount first = from.getId() < to.getId() ? from : to;
        BankAccount second = from.getId() < to.getId() ? to : from;

        first.lock();
        second.lock();
        try {
            if (from.withdraw(amount)) {
                to.deposit(amount);
            } else {
                // недостаточно средств — транзакция атомарно не проходит
                // можно бросить исключение
                System.out.println("Insufficient funds for transfer from account "
                        + from.getId() + " to account " + to.getId());
            }
        } finally {
            second.unlock();
            first.unlock();
        }
    }

    public long getTotalBalance() {
        // Получаем snapshot списка счетов и блокируем их в порядке id,
        // чтобы получить консистентную сумму и не словить дедлок.
        List<BankAccount> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(BankAccount::getId));

        for (BankAccount a : list) a.lock();
        try {
            long sum = 0L;
            for (BankAccount a : list) sum += a.getBalance();
            return sum;
        } finally {
            for (int i = list.size() - 1; i >= 0; i--) list.get(i).unlock();
        }
    }

}
