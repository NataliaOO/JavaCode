package com.example.snapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyStringBuilder {

    private char[] value;
    private int count;

    // неизменяемый снимок
    private record Memento(char[] data, int length) {}
    private final List<Memento> history = new ArrayList<>();
    private int top = -1;

    public MyStringBuilder() {
        this.value = new char[16];
        this.count = 0;
    }

    public MyStringBuilder(String initial) {
        this();
        if (initial != null && !initial.isEmpty()) {
            saveSnapshot();
            int n = initial.length();
            ensureCapacity(n);
            initial.getChars(0, n, value, 0);
            count = n;
        }
    }

    private void saveSnapshot() {
        if (top < history.size() - 1) {
            history.subList(top + 1, history.size()).clear();
        }
        history.add(new Memento(Arrays.copyOf(value, count), count));
        top = history.size() - 1;
    }

    private void ensureCapacity(int minCap) {
        if (minCap > value.length) {
            int newCap = Math.max(minCap, value.length * 2);
            value = Arrays.copyOf(value, newCap);
        }
    }

    private void checkRange(int start, int end) {
        if (start < 0 || end < start || end > count) {
            throw new StringIndexOutOfBoundsException("start=" + start + ", end=" + end + ", len=" + count);
        }
    }

    private void checkIndexInclusive(int idx) {
        if (idx < 0 || idx > count) {
            throw new StringIndexOutOfBoundsException("index=" + idx + ", len=" + count);
        }
    }

    public int length() { return count; }

    @Override public String toString() { return new String(value, 0, count); }

    public String substring(int begin, int end) {
        checkRange(begin, end);
        return new String(value, begin, end - begin);
    }

    public int indexOf(String str) { return indexOf(str, 0); }

    public int indexOf(String str, int fromIndex) {
        Objects.requireNonNull(str, "str");
        if (fromIndex < 0) fromIndex = 0;
        if (str.isEmpty()) return Math.min(fromIndex, count);
        int m = str.length();
        if (m > count - fromIndex) return -1;
        char first = str.charAt(0);
        for (int i = fromIndex; i <= count - m; i++) {
            if (value[i] != first) continue;
            int j = 1;
            while (j < m && value[i + j] == str.charAt(j)) j++;
            if (j == m) return i;
        }
        return -1;
    }

    public int lastIndexOf(String str) { return lastIndexOf(str, count); }

    public int lastIndexOf(String str, int fromIndex) {
        Objects.requireNonNull(str, "str");
        if (str.isEmpty()) return Math.min(fromIndex, count);
        int m = str.length();
        int i = Math.min(fromIndex, count - 1) - (m - 1);
        for (; i >= 0; i--) {
            int j = 0;
            while (j < m && value[i + j] == str.charAt(j)) j++;
            if (j == m) return i;
        }
        return -1;
    }

    public MyStringBuilder append(String s) {
        Objects.requireNonNull(s, "s");
        saveSnapshot();
        int n = s.length();
        ensureCapacity(count + n);
        s.getChars(0, n, value, count);
        count += n;
        return this;
    }

    public MyStringBuilder insert(int offset, String s) {
        Objects.requireNonNull(s, "s");
        checkIndexInclusive(offset);
        saveSnapshot();
        int n = s.length();
        ensureCapacity(count + n);
        System.arraycopy(value, offset, value, offset + n, count - offset);
        s.getChars(0, n, value, offset);
        count += n;
        return this;
    }

    public MyStringBuilder delete(int start, int end) {
        checkRange(start, end);
        if (start == end) return this;
        saveSnapshot();
        int len = end - start;
        System.arraycopy(value, end, value, start, count - end);
        count -= len;
        return this;
    }

    public MyStringBuilder replace(int start, int end, String str) {
        Objects.requireNonNull(str, "str");
        checkRange(start, end);
        saveSnapshot();
        int remove = end - start;
        int add = str.length();
        int newLen = count - remove + add;
        ensureCapacity(newLen);
        if (add != remove) {
            System.arraycopy(value, end, value, start + add, count - end);
        }
        str.getChars(0, add, value, start);
        count = newLen;
        return this;
    }

    public void clear() {
        if (count == 0) return;
        saveSnapshot();
        count = 0;
    }

    public void undo() {
        if (top < 0) return;
        Memento m = history.get(top);
        if (value.length < m.length) value = new char[m.length];
        System.arraycopy(m.data, 0, value, 0, m.length);
        count = m.length;
        top--;
    }
}
