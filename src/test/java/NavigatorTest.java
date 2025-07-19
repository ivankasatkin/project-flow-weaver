import io.github.shared.Navigator;
import io.github.shared.exceptions.NavigatorIllegalArgumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NavigatorTest {

    private Navigator<String> navigator;
    private List<String> testList;

    @BeforeEach
    void init() {
        testList = new ArrayList<>();

        testList.add(null);
        testList.add("Zero");
        testList.add("One");
        testList.add(null);
        testList.add("Two");
        testList.add("Three");
        testList.add("Four");
        testList.add(null);

        navigator = new Navigator<>(testList);
    }

    @AfterEach
    void tearDown() {
        navigator = null;
        testList.clear();
    }

    @Test
    @DisplayName("Pass invalid value to the constructor")
    void constructor_PassNullList_ThrowsNavigatorIllegalArgumentException() {
        assertThrows(NavigatorIllegalArgumentException.class, () -> new Navigator<>(null));
    }

    @Test
    @DisplayName("Pass empty list to the constructor")
    void constructor_PassEmptyList_DoesNotThrowException() {
        assertDoesNotThrow(() -> new Navigator<>(new ArrayList<>()));
    }

    @Test
    @DisplayName("Pass negative index on getByIndex")
    void getByIndex_PassNegativeIndex_ReturnsEmptyOptional() {
        assertEquals(Optional.empty(), navigator.getByIndex(-1));
    }

    @Test
    @DisplayName("Pass index out of bounds on getByIndex")
    void getByIndex_PassIndexOutOfBounds_ReturnsEmptyOptional() {
        assertEquals(Optional.empty(), navigator.getByIndex(testList.size()));
    }

    @Test
    @DisplayName("Pass index within bounds on getByIndex")
    void getByIndex_PassIndexWithinBounds_ReturnsCorrectElement() {
        assertEquals(Optional.of("One"), navigator.getByIndex(2));
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get current element on empty list")
    void first_Current_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.current());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get current element before advancing")
    void current_BeforeAdvance_ReturnsEmptyOptional() {
        assertEquals(Optional.empty(), navigator.current());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get current element after advancing to the first element")
    void current_AfterAdvance_ReturnsCorrectElement() {
        navigator.next();
        assertEquals(Optional.empty(), navigator.current());
        assertEquals(0, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get next non null element on empty list")
    void nextNonNull_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.nextNonNull());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get next non null element on list with only null elements")
    void nextNonNull_OnListWithNullElements_ReturnEmptyOptional() {
        List<String> nullList = getNullElementsList(5);
        navigator = new Navigator<>(nullList);
        assertEquals(Optional.empty(), navigator.nextNonNull());
        assertEquals(4, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get next non null element on list with non null elements")
    void nextNonNull_OnListWithNonNullElements_ReturnsCorrectElement() {
        navigator.next();
        navigator.next();
        navigator.next();
        assertEquals(Optional.of("Two"), navigator.nextNonNull());
        assertEquals(4, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get previous non null element on empty list")
    void previousNonNull_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.previousNonNull());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get previous non null element on list with only null elements")
    void previousNonNull_OnListWithNullElements_ReturnEmptyOptional() {
        List<String> nullList = getNullElementsList(5);
        navigator = new Navigator<>(nullList);
        assertEquals(Optional.empty(), navigator.previousNonNull());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get previous non null element on list with non null elements")
    void previousNonNull_OnListWithNonNullElements_ReturnsCorrectElement() {
        navigator.next();
        navigator.next();
        navigator.next();
        navigator.next();
        navigator.next();
        assertEquals(Optional.of("One"), navigator.previousNonNull());
        assertEquals(2, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first non null element on list with empty list")
    void firstNonNull_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.firstNonNull());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first non null element on list with only null elements")
    void firstNonNull_OnListWithNullElements_ReturnCorrectElement() {
        List<String> nullList = getNullElementsList(4);
        navigator = new Navigator<>(nullList);
        assertEquals(Optional.empty(), navigator.firstNonNull());
        assertEquals(3, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first non null element on list with non null elements")
    void firstNonNull_OnListWithNonNullElements_ReturnsCorrectElement() {
        assertEquals(Optional.of("Zero"), navigator.firstNonNull());
        assertEquals(1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last non null element on empty list")
    void lastNonNull_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.lastNonNull());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last non null element on list with only null elements")
    void lastNonNull_OnListWithNullElements_ReturnCorrectElement() {
        navigator = new Navigator<>(getNullElementsList(6));
        assertEquals(Optional.empty(), navigator.lastNonNull());
        assertEquals(0, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last non null element on list with non null elements")
    void lastNonNull_OnListWithNonNullElements_ReturnsCorrectElement() {
        navigator.moveToIndex(7);
        assertEquals(Optional.of("Four"), navigator.lastNonNull());
        assertEquals(6, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get next element on empty list")
    void next_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.next());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get next element on list with non null elements")
    void next_OnListWithNonNullElements_ReturnsCorrectElement() {
        assertEquals(Optional.empty(), navigator.next());
        assertEquals(0, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get previous element on empty list")
    void previous_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.previous());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get previous element on list with non null elements")
    void previous_OnListWithNonNullElements_ReturnsCorrectElement() {
        navigator.next();
        navigator.next();
        navigator.next();
        assertEquals(Optional.of("Zero"), navigator.previous());
        assertEquals(1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first element on empty list")
    void first_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.first());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first element on list with only null elements")
    void first_OnListWithNullElements_ReturnsCorrectElement() {
        List<String> nullList = getNullElementsList(4);
        navigator = new Navigator<>(nullList);
        assertEquals(Optional.empty(), navigator.first());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get first element on list with non null elements")
    void first_OnListWithNonNullElements_ReturnsCorrectElement() {
        List<String> nonNullList = new ArrayList<>();
        nonNullList.add("Zero");
        nonNullList.add("One");
        nonNullList.add("Two");
        navigator = new Navigator<>(nonNullList);
        assertEquals(Optional.of("Zero"), navigator.first());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last element on empty list")
    void last_OnEmptyList_ReturnEmptyOptional() {
        navigator = new Navigator<>(new ArrayList<>());
        assertEquals(Optional.empty(), navigator.last());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last element on list with only null elements")
    void last_OnListWithNullElements_ReturnsCorrectElement() {
        List<String> nullList = getNullElementsList(6);
        navigator = new Navigator<>(nullList);
        assertEquals(Optional.empty(), navigator.last());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Get last element on list with non null elements")
    void last_OnListWithNonNullElements_ReturnsCorrectElement() {
        testList.set(7, "Five");
        navigator = new Navigator<>(testList);
        assertEquals(Optional.of("Five"), navigator.last());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Check if list has next element on empty list")
    void hasNext_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.hasNext());
    }

    @Test
    @DisplayName("Check if list has next element on list with non null elements")
    void hasNext_OnLastElement_ReturnFalse() {
        navigator.moveToIndex(testList.size() - 1);
        assertFalse(navigator.hasNext());
    }

    @Test
    @DisplayName("Check if list has previous element on empty list")
    void hasPrevious_OnEmptyList_ReturnFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.hasPrevious());
    }

    @Test
    @DisplayName("Check if list has previous element on list with non null elements")
    void hasPrevious_OnFirstElement_ReturnFalse() {
        navigator = new Navigator<>(testList);
        navigator.moveToIndex(0);
        assertFalse(navigator.hasPrevious());
    }

    @Test
    @DisplayName("Set next element on empty list")
    void setNext_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.setNext("New Element"));
    }

    @Test
    @DisplayName("Set next element on list with non null elements")
    void setNext_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(testList.size() - 2);
        assertTrue(navigator.setNext("New Element"));
        assertEquals(Optional.of("New Element"), navigator.last());
    }

    @Test
    @DisplayName("Set previous element on empty list")
    void setPrevious_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.setPrevious("New Element"));
    }

    @Test
    @DisplayName("Set previous element on list with non null elements")
    void setPrevious_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(1);
        assertTrue(navigator.setPrevious("New Element"));
        assertEquals(Optional.of("New Element"), navigator.first());
    }

    @Test
    @DisplayName("Set current element on empty list")
    void setCurrent_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.setCurrent("New Element"));
    }

    @Test
    @DisplayName("Set current element on list with non null elements")
    void setCurrent_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(3);
        assertTrue(navigator.setCurrent("New Element"));
        assertEquals(Optional.of("New Element"), navigator.getByIndex(3));
        assertEquals(3, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Set first element on empty list")
    void setFirst_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.setFirst("New Element"));
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Set first element on list with non null elements")
    void setFirst_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(2);
        assertTrue(navigator.setFirst("New Element"));
        assertEquals(Optional.of("New Element"), navigator.first());
        assertEquals(2, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Set last element on empty list")
    void setLast_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.setLast("New Element"));
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Set last element on list with non null elements")
    void setLast_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(5);
        assertTrue(navigator.setLast("New Element"));
        assertEquals(Optional.of("New Element"), navigator.last());
        assertEquals(5, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove current element on empty list")
    void removeCurrent_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.removeCurrent());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove current element on list with non null elements")
    void removeCurrent_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(4);
        assertTrue(navigator.removeCurrent());
        assertEquals(Optional.of("Three"), navigator.getByIndex(4));
        assertEquals(4, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove next element on empty list")
    void removeNext_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.removeNext());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove next element on list with non null elements")
    void removeNext_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(3);
        assertTrue(navigator.removeNext());
        assertEquals(Optional.of("Three"), navigator.getByIndex(4));
        assertEquals(3, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove previous element on empty list")
    void removePrevious_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.removePrevious());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove previous element on list with non null elements")
    void removePrevious_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(2);
        assertTrue(navigator.removePrevious());
        assertEquals(Optional.empty(), navigator.getByIndex(2));
        assertEquals(2, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove first element on empty list")
    void removeFirst_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.removeFirst());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove first element on list with non null elements")
    void removeFirst_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(1);
        assertTrue(navigator.removeFirst());
        assertEquals(Optional.of("Zero"), navigator.first());
        assertEquals(1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove last element on empty list")
    void removeLast_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.removeLast());
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Remove last element on list with non null elements")
    void removeLast_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(5);
        assertTrue(navigator.removeLast());
        assertEquals(Optional.of("Four"), navigator.last());
        assertEquals(5, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Move to index on empty list")
    void moveToIndex_OnEmptyList_ReturnsFalse() {
        navigator = new Navigator<>(new ArrayList<>());
        assertFalse(navigator.moveToIndex(4));
        assertEquals(-1, navigator.getCurrentCursorPosition());
    }

    @Test
    @DisplayName("Move to index on list with non null elements")
    void moveToIndex_OnListWithNonNullElements_ReturnsTrue() {
        navigator.moveToIndex(3);
        assertTrue(navigator.moveToIndex(4));
        assertEquals(Optional.of("Two"), navigator.getByIndex(4));
        assertEquals(4, navigator.getCurrentCursorPosition());
    }


    private List<String> getNullElementsList(int count) {
        List<String> nullElementsList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nullElementsList.add(null);
        }
        return nullElementsList;
    }


}
