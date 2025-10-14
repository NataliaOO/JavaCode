import com.example.ElementCounter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ElementCounterTest {
    @Test
    @DisplayName("List<Integer>: базовый подсчёт")
    void list_basicCount() {
        Map<Integer, Integer> m = ElementCounter.countByElement(List.of(1, 1, 2, 3));
        assertEquals(Map.of(1, 2, 2, 1, 3, 1), m);
    }

    @Test
    @DisplayName("Iterable не-коллекция: однократный проход")
    void iterable_nonCollection() {
        Iterable<String> it = () -> Arrays.asList("a", "b", "a").iterator();
        Map<String, Integer> m = ElementCounter.countByElement(it);
        assertEquals(Map.of("a", 2, "b", 1), m);
    }

    @Test
    @DisplayName("List<String> с null-элементами")
    void list_withNulls() {
        List<String> src = Arrays.asList("x", null, "x", null, "y");
        Map<String, Integer> m = ElementCounter.countByElement(src);
        assertEquals(2, m.get("x"));
        assertEquals(2, m.get(null));
        assertEquals(1, m.get("y"));
        assertEquals(3, m.size());
    }

    @Test
    @DisplayName("Iterable null -> пустая Map")
    void iterable_null_returnsEmpty() {
        Map<Object, Integer> m = ElementCounter.countByElement((Iterable<?>) null);
        assertTrue(m.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> m.put("x", 1), "emptyMap неизменяемая");
    }

    @Test
    @DisplayName("Пустой List -> пустая Map")
    void list_empty() {
        Map<Object, Integer> m = ElementCounter.countByElement(Collections.emptyList());
        assertTrue(m.isEmpty());
    }

    @Test
    @DisplayName("Array<T>: базовый подсчёт")
    void array_reference_basic() {
        String[] arr = {"a", "b", "a", null, "b"};
        Map<String, Integer> m = ElementCounter.countByElement(arr);
        assertEquals(2, m.get("a"));
        assertEquals(2, m.get("b"));
        assertEquals(1, m.get(null));
        assertEquals(3, m.size());
    }

    @Test
    @DisplayName("Array<T> null -> пустая Map")
    void array_reference_null_returnsEmpty() {
        Map<Object, Integer> m = ElementCounter.countByElement((Object[]) null);
        assertTrue(m.isEmpty());
    }

    @Test
    @DisplayName("int[] через Object: корректный подсчёт")
    void array_primitive_int() {
        Object ints = new int[]{1, 1, 2, 3};
        Map<Object, Integer> m = ElementCounter.countByElement(ints);
        assertEquals(Map.of(1, 2, 2, 1, 3, 1), m);
    }

    @Test
    @DisplayName("double[] через Object: корректный подсчёт")
    void array_primitive_double() {
        Object ds = new double[]{1.0, 1.0, 2.5};
        Map<Object, Integer> m = ElementCounter.countByElement(ds);
        assertEquals(2, m.get(1.0));
        assertEquals(1, m.get(2.5));
        assertEquals(2, m.size());
    }

    @Test
    @DisplayName("Object не-массив -> IllegalArgumentException")
    void nonArray_throws() {
        assertThrows(IllegalArgumentException.class, () -> ElementCounter.countByElement("not array"));
    }

    @Test
    @DisplayName("Object null -> пустая Map")
    void object_null_returnsEmpty() {
        Map<Object, Integer> m = ElementCounter.countByElement((Object) null);
        assertTrue(m.isEmpty());
    }
}
