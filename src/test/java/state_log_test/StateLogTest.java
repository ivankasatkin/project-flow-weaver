package state_log_test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.shared.StateLog;
import io.github.shared.exceptions.StateLogEntryIllegalArgumentException;
import io.github.shared.exceptions.StateLogIllegalArgumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state_log_test.setup.LoggableDto;
import state_log_test.setup.TestDto;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class StateLogTest {

    private final Gson mapper = new GsonBuilder().serializeNulls().create();

    StateLog mainLog;
    StateLog auxLog;
    TestDto johnTestDto;
    TestDto mikeTestDto;
    TestDto janeTestDto;
    LoggableDto johnLoggableDto;
    LoggableDto mikeLoggableDto;
    LoggableDto janeLoggableDto;

    @BeforeEach
    void setup() {
        mainLog = StateLog.empty();
        auxLog = StateLog.empty();
        johnTestDto = new TestDto("John", "Smith", 27);
        mikeTestDto = new TestDto("Mike", "Morton", 24);
        janeTestDto = new TestDto("Jane", "Austin", 19);
        johnLoggableDto = new LoggableDto(johnTestDto, "john", "smith");
        mikeLoggableDto = new LoggableDto(mikeTestDto, "mike", "morton");
        janeLoggableDto = new LoggableDto(janeTestDto, "jane", "austin");
    }

    @AfterEach
    void tearDown() {
        mainLog = null;
        auxLog = null;
        johnTestDto = null;
        mikeTestDto = null;
        janeTestDto = null;
        johnLoggableDto = null;
        mikeLoggableDto = null;
        janeLoggableDto = null;
    }

    @Test
    @DisplayName("Submitting null as an entry")
    void fromEntry_SubmitNullAsParameter_ThrowStateLogIllegalArgumentException() {
        assertThrows(StateLogIllegalArgumentException.class, () -> StateLog.fromEntry(null));
    }

    @Test
    @DisplayName("Submitting non-null entry")
    void fromEntry_SubmitNonNullEntry_ReturnStateLogWithSingleEntry() {
        mainLog = StateLog.fromEntry(new StateLog.Entry(janeTestDto.getName(), janeTestDto));
        assertEquals(1, mainLog.size());
    }

    @Test
    @DisplayName("Submitting null entries list")
    void fromEntryList_PassNullEntryList_ThrowStateLogIllegalArgumentException() {
        assertThrows(StateLogIllegalArgumentException.class, () -> StateLog.fromEntryList(null));
    }

    @Test
    @DisplayName("Submitting a list with all null entries")
    void fromEntryList_PassListWithNullElements_ReturnStateLogWithZeroSize() {
        mainLog = StateLog.fromEntryList(Arrays.asList(null, null, null, null, null));
        assertEquals(0, mainLog.size());
    }

    @Test
    @DisplayName("Submitting list with all non-null entries")
    void fromEntryList_PassListWithNonNullElements_ReturnExpectedStateLogSize() {
        StateLog.Entry entry = new StateLog.Entry("title", new Object());
        mainLog = StateLog.fromEntryList(Arrays.asList(entry, entry, entry, entry, entry));
        assertEquals(5, mainLog.size());
    }

    @Test
    @DisplayName("Submitting null other state log")
    void fromOther_PassNullAsParameter_ThrowStateLogIllegalArgumentException() {
        assertThrows(StateLogIllegalArgumentException.class, () -> StateLog.fromOther(null));
    }

    @Test
    @DisplayName("Submitting empty other state log")
    void fromOther_PassEmptyStateLogAsParameter_ReturnStateLogWithZeroSize() {
        auxLog = StateLog.fromOther(mainLog);
        assertEquals(0, auxLog.size());
    }

    @Test
    @DisplayName("Submitting non-empty other state log")
    void fromOther_PassValidOtherStateLog_ReturnStateLogWithSizeEqualsToOtherStateLogSize() {
        fillMainLog();
        auxLog = StateLog.fromOther(mainLog);
        assertEquals(auxLog.size(), mainLog.size());
    }

    @Test
    @DisplayName("Submitting null entry title")
    void append_PassNullTitle_ThrowStateLogEntryIllegalArgumentException() {
        assertThrows(StateLogEntryIllegalArgumentException.class, () -> mainLog.append(null, new Object()));
    }

    @Test
    @DisplayName("Submitting empty entry title")
    void append_PassEmptyTitle_ThrowStateLogEntryIllegalArgumentException() {
        assertThrows(StateLogEntryIllegalArgumentException.class, () -> mainLog.append("", new Object()));
    }

    @Test
    @DisplayName("Submitting null entry value")
    void append_PassNullValueObject_ThrowStateLogEntryIllegalArgumentException() {
        assertThrows(StateLogEntryIllegalArgumentException.class, () -> mainLog.append("title", null));
    }

    @Test
    @DisplayName("Submitting null name aware instance")
    void append_PassNullInstanceNameAware_ThrowStateLogEntryIllegalArgumentException() {
        assertThrows(StateLogEntryIllegalArgumentException.class, () -> mainLog.append(null));
    }

    @Test
    @DisplayName("Fetching log size on empty state log")
    void size_CallOnEmptyStateLog_ReturnZero() {
        assertEquals(0, mainLog.size());
    }

    @Test
    @DisplayName("Fetching log size on non-empty state log")
    void size_AppendThreeEntries_ReturnThree() {
        fillMainLog();
        assertEquals(3, mainLog.size());
    }

    @Test
    @DisplayName("Fetching log size built with other non-empty state log")
    void size_CreateNewStateLogFromStateLogWithThreeEntriesAndAddTwoEntries_ReturnFive() {
        fillMainLog();
        auxLog = StateLog.fromOther(mainLog)
                .append(mikeTestDto.getName(), mikeTestDto)
                .append(janeTestDto.getName(), janeTestDto)
        ;

        assertEquals(5, auxLog.size());
    }

    @Test
    @DisplayName("Exporting map from empty state log")
    void asMap_CallOnEmptyStateLog_ReturnEmptyMap() {
        assertTrue(mainLog.asMap().isEmpty());
    }

    @Test
    @DisplayName("Exporting map from non-empty state log")
    void asMap_AddThreeEntries_ReturnMapContainingThreeElements() {
        fillMainLog();
        assertEquals(3, mainLog.asMap().size());
    }

    @Test
    @DisplayName("Exporting JSON from empty state log")
    void asJson_CallOnEmptyStateLog_ReturnEmptyJson() {
        String emptyJson = null;
        try {
            emptyJson = mapper.toJson(Collections.emptyList());
        } catch (Exception ignored) {}
        assertEquals(emptyJson, mainLog.asJson());
    }

    @Test
    @DisplayName("Exporting JSON from non-empty state log")
    void asJson_BuildJsonFromStateLogContainingLoggableEntries_JsonDoesNotContainPasswordField() {
        mainLog.append(johnLoggableDto.getName(), johnLoggableDto);
        assertFalse(mainLog.asJson().contains("password"));
    }

    @Test
    @DisplayName("Exporting JSON from state log built from other non-null state log")
    void asJson_BuildJsonFromStateLogContainingLoggableEntries_JsonContainsValidLogin() {
        mainLog.append(johnLoggableDto.getName(), johnLoggableDto);
        String str = mainLog.asJson();
        assertTrue(mainLog.asJson().contains("\"login\":\"john\""));
    }

    private void fillMainLog() {
        mainLog
                .append(johnTestDto.getName(), johnTestDto)
                .append(mikeTestDto.getName(), mikeTestDto)
                .append(janeTestDto.getName(), janeTestDto);
    }
}
