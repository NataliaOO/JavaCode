package com.example.snapshot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyStringBuilderTest {

    @Test
    @DisplayName("Пустой конструктор: начальное состояние пустое")
    void ctor_empty() {
        MyStringBuilder sb = new MyStringBuilder();
        assertEquals(0, sb.length());
        assertEquals("", sb.toString());
    }

    @Test
    @DisplayName("Конструктор от строки: строка попадает внутрь")
    void ctor_fromString() {
        MyStringBuilder sb = new MyStringBuilder("Hello");
        assertEquals(5, sb.length());
        assertEquals("Hello", sb.toString());
    }

    @Test
    @DisplayName("append(String): конкатенирует по порядку")
    void append_appendsInOrder() {
        MyStringBuilder sb = new MyStringBuilder();
        sb.append("A").append("B").append("C");
        assertEquals("ABC", sb.toString());
    }

    @Test
    @DisplayName("insert: вставляет по индексу, сдвигая хвост")
    void insert_insertsAtIndex() {
        MyStringBuilder sb = new MyStringBuilder("Hello world");
        sb.insert(5, ",");
        assertEquals("Hello, world", sb.toString());
    }

    @Test
    @DisplayName("delete: удаляет полуинтервал [start,end)")
    void delete_removesRange() {
        MyStringBuilder sb = new MyStringBuilder("Hello, world");
        sb.delete(5, 6); // убрать запятую
        assertEquals("Hello world", sb.toString());
    }

    @Test
    @DisplayName("replace: заменяет полуинтервал [start,end) строкой")
    void replace_replacesRange() {
        MyStringBuilder sb = new MyStringBuilder("Hello, world");
        sb.replace(7, 12, "Java"); // world -> Java
        assertEquals("Hello, Java", sb.toString());
    }

    @Test
    @DisplayName("length и substring работают как у StringBuilder")
    void length_and_substring() {
        MyStringBuilder sb = new MyStringBuilder("Hello, Java");
        assertEquals(11, sb.length());
        assertEquals("Java", sb.substring(7, 11));
    }

    // --- Поиск ---

    @Test
    @DisplayName("indexOf: находит первое вхождение c fromIndex")
    void indexOf_works() {
        MyStringBuilder sb = new MyStringBuilder("ababa");
        assertEquals(0, sb.indexOf("ab"));
        assertEquals(2, sb.indexOf("aba", 1));
        assertEquals(-1, sb.indexOf("zzz"));
    }

    @Test
    @DisplayName("lastIndexOf: находит последнее вхождение c fromIndex")
    void lastIndexOf_works() {
        MyStringBuilder sb = new MyStringBuilder("ababa");
        assertEquals(2, sb.lastIndexOf("aba"));
        assertEquals(0, sb.lastIndexOf("aba", 2));
        assertEquals(-1, sb.lastIndexOf("zzz"));
    }

    @Test
    @DisplayName("undo: откатывает последнее изменение")
    void undo_revertsLastMutation() {
        MyStringBuilder sb = new MyStringBuilder();
        sb.append("Hello");
        sb.append(" world");
        assertEquals("Hello world", sb.toString());

        sb.undo(); // отменяем второй append
        assertEquals("Hello", sb.toString());

        sb.undo(); // отменяем первый append
        assertEquals("", sb.toString());
    }

    @Test
    @DisplayName("undo после clear: возвращает предыдущее содержимое")
    void undo_afterClear_restoresContent() {
        MyStringBuilder sb = new MyStringBuilder("XYZ");
        sb.clear();
        assertEquals("", sb.toString());

        sb.undo(); // откат clear
        assertEquals("XYZ", sb.toString());
    }

    @Test
    @DisplayName("Множественные undo не падают (no-op при пустой истории)")
    void undo_excessNoOp() {
        MyStringBuilder sb = new MyStringBuilder("A");
        sb.undo(); // -> ""
        sb.undo(); // лишний, должен быть no-op
        assertEquals("", sb.toString());
    }
}