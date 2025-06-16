import io.github.shared.DisposableSwitch;
import io.github.shared.exceptions.DisposableSwitchAlreadyDisposedException;
import io.github.shared.exceptions.DisposableSwitchIllegalArgumentException;
import io.github.shared.exceptions.DisposableSwitchModeRegistryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisposableSwitchTest {

    Random random = new Random();

    DisposableSwitch<String, String, String> disposableSwitch;
    String permittedMode;
    String secondaryPermittedMode;
    String restrictedMode;
    String secondaryRestrictedMode;
    Function<String, String> permittedOperation;
    Function<String, String> secondaryPermittedOperation;
    Function<String, String> restrictedOperation;
    Function<String, String> secondaryRestrictedOperation;
    String input;

    @BeforeEach
    void init() {
        permittedMode = "permitted";
        secondaryPermittedMode = "also_permitted";
        restrictedMode = "restricted";
        secondaryRestrictedMode = "also_restricted";
        permittedOperation = p -> p + " successful";
        secondaryPermittedOperation = p -> p + " also successful";
        restrictedOperation = r -> r + " reaches restriction";
        secondaryRestrictedOperation = r -> r + " also reaches restriction";
        disposableSwitch = new DisposableSwitch<>();
        input = "Test";
    }

    @AfterEach
    void tarDown() {
        permittedMode = null;
        secondaryPermittedMode = null;
        restrictedMode = null;
        secondaryRestrictedMode = null;
        permittedOperation = null;
        secondaryPermittedOperation = null;
        restrictedOperation = null;
        secondaryRestrictedOperation =null;
        disposableSwitch = null;
        input = null;
    }

    @Test
    @DisplayName("Pass null permitted mode to constructor")
    void constructor_PassNullMode_DoesNotThrowException() {
        assertDoesNotThrow(() -> new DisposableSwitch<String, String, String>(null, permittedOperation));
    }

    @Test
    @DisplayName("Pass null permitted operation to constructor")
    void constructor_PassNullOperation_ThrowDisposableSwitchIllegalArgumentException() {
        assertThrows(DisposableSwitchIllegalArgumentException.class, () -> new DisposableSwitch<String, String, String>(permittedMode, null));
    }

    @Test
    @DisplayName("Pass null mode on adding permitted mode")
    void addPermittedMode_PassNullMode_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.addPermittedMode(null, permittedOperation));
    }

    @Test
    @DisplayName("Pass null operation on adding permitted mode")
    void addPermittedMode_PassNullOperation_ThrowDisposableSwitchIllegalArgumentException() {
        assertThrows(DisposableSwitchIllegalArgumentException.class, () -> disposableSwitch.addPermittedMode(permittedMode, null));
    }

    @Test
    @DisplayName("Try adding already existing non-null mode")
    void addPermittedMode_AddAlreadyExistingMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addPermittedMode(permittedMode, permittedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addPermittedMode(permittedMode, permittedOperation));
    }

    @Test
    @DisplayName("Try adding already existing null mode")
    void addPermittedNode_AddAlreadyExistingNullMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addPermittedMode(null, permittedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addPermittedMode(null, permittedOperation));
    }

    @Test
    @DisplayName("Try adding permitted non-null mode equals to restricted non-null mode")
    void addPermittedMode_AddModeEqualsToRestrictedMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addRestrictedMode(permittedMode, permittedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addPermittedMode(permittedMode, permittedOperation));
    }

    @Test
    @DisplayName("Try adding permitted null mode equals to restricted null mode")
    void addPermittedMode_AddNullModeEqualsToRestrictedMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addRestrictedMode(null, permittedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addPermittedMode(null, permittedOperation));
    }

    @Test
    @DisplayName("Pass null mode on adding restricted mode")
    void addRestrictedMode_PassNullMode_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.addRestrictedMode(null, restrictedOperation));
    }

    @Test
    @DisplayName("Pass null operation on adding restricted mode")
    void addRestrictedMode_PassNullOperation_ThrowDisposableSwitchIllegalArgumentException() {
        assertThrows(DisposableSwitchIllegalArgumentException.class, () -> disposableSwitch.addRestrictedMode(restrictedMode, null));
    }

    @Test
    @DisplayName("Try adding already existing non-null restricted mode")
    void addRestrictedMode_AddAlreadyExistingMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addRestrictedMode(restrictedMode, restrictedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addRestrictedMode(restrictedMode, restrictedOperation));
    }

    @Test
    @DisplayName("Try adding non-null restricted mode equals to permitted non-null mode")
    void addRestrictedMode_AddModeEqualToPermittedMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addPermittedMode(restrictedMode, restrictedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addRestrictedMode(restrictedMode, restrictedOperation));
    }

    @Test
    @DisplayName("Try adding already existing null restricted mode")
    void addRestrictedMode_AddAlreadyExistingRestrictedNullMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addRestrictedMode(null, restrictedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addRestrictedMode(null, restrictedOperation));
    }

    @Test
    @DisplayName("Try adding null restricted mode equals to permitted null mode")
    void addRestrictedMode_AddNullRestrictedModeEqualsToNullPermittedMode_ThrowDisposableSwitchModeRegistryException() {
        disposableSwitch.addRestrictedMode(null, restrictedOperation);
        assertThrows(DisposableSwitchModeRegistryException.class, () -> disposableSwitch.addPermittedMode(null, restrictedOperation));
    }

    @Test
    @DisplayName("Try removing null permitted mode from empty registry")
    void removePermittedMode_RemovePermittedNullModeFromEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.removePermittedMode(null));
    }

    @Test
    @DisplayName("Try removing non-null permitted mode from empty registry")
    void removePermittedMode_RemovePermittedModeFromEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.removePermittedMode(permittedMode));
    }

    @Test
    @DisplayName("Try removing non-existent restricted mode from non-empty registry")
    void removePermittedMode_RemoveNonExistentPermittedModeFromNonEmptyRegistry_DoesNotThrowException() {
        disposableSwitch.addPermittedMode(permittedMode, permittedOperation);
        assertDoesNotThrow(() -> disposableSwitch.removeRestrictedMode(secondaryPermittedMode));
    }

    @Test
    @DisplayName("Get estimated permitted non-empty modes' registry size after successful permitted mode removal")
    void removePermittedMode_RemovePermittedModeFromNonEmptyRegistry_GetEstimatedRegistrySize() {
        disposableSwitch
                .addPermittedMode(permittedMode, permittedOperation)
                .addPermittedMode(secondaryPermittedMode, secondaryPermittedOperation);
        disposableSwitch.removePermittedMode(secondaryPermittedMode);
        assertEquals(1, disposableSwitch.getPermittedModesRegistrySize());
    }

    @Test
    @DisplayName("Try removing null restricted mode from empty registries")
    void removeRestrictedMode_RemoveRestrictedNullModeFromEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.removeRestrictedMode(null));
    }

    @Test
    @DisplayName("Try removing non-null restricted mode from empty registry")
    void removePermittedMode_RemoveRestrictedModeFromEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.removeRestrictedMode(restrictedMode));
    }

    @Test
    @DisplayName("Try removing non-existent restricted mode from non-empty registry")
    void removeRestrictedMode_RemoveNonExistentRestrictedModeFromNonEmptyRegistry_DoesNotThrowException() {
        disposableSwitch.addRestrictedMode(restrictedMode, restrictedOperation);
        assertDoesNotThrow(() -> disposableSwitch.removeRestrictedMode(secondaryRestrictedMode));
    }

    @Test
    @DisplayName("Get estimated restricted non-empty modes' registry size after successful restricted mode removal")
    void removeRestrictedMode_RemoveRestrictedModeFromNonEmptyRegistry_GetEstimatedRegistrySize() {
        disposableSwitch
                .addRestrictedMode(restrictedMode, restrictedOperation)
                .addRestrictedMode(secondaryRestrictedMode, secondaryRestrictedOperation);
        disposableSwitch.removeRestrictedMode(secondaryRestrictedMode);
        assertEquals(1, disposableSwitch.getRestrictedModesRegistrySize());
    }

    @Test
    @DisplayName("Try clearing empty permitted modes registry")
    void clearPermittedModes_CallOnEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.clearPermittedModes());
    }

    @Test
    @DisplayName("Try clearing non-empty permitted modes registry")
    void clearPermittedModes_CallOnNonEmptyRegistry_getZeroPermittedModeRegistrySize() {
        disposableSwitch
                .addPermittedMode(restrictedMode, restrictedOperation)
                .addPermittedMode(secondaryRestrictedMode, secondaryRestrictedOperation);
        disposableSwitch.clearPermittedModes();
        assertEquals(0, disposableSwitch.getPermittedModesRegistrySize());
    }

    @Test
    @DisplayName("Try clearing empty restricted modes registry")
    void clearRestrictedModes_CallOnEmptyRegistry_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.clearRestrictedModes());
    }

    @Test
    @DisplayName("Try clearing non-empty restricted modes registry")
    void clearRestrictedModes_CallOnNonEmptyRegistry_getZeroRestrictedModeRegistrySize() {
        disposableSwitch
                .addRestrictedMode(restrictedMode, restrictedOperation)
                .addRestrictedMode(secondaryRestrictedMode, secondaryRestrictedOperation);
        disposableSwitch.clearPermittedModes();
        assertEquals(0, disposableSwitch.getPermittedModesRegistrySize());
    }

    @Test
    @DisplayName("Try clearing all empty mode registries")
    void clearAllModes_CallOnEmptyRegistries_DoesNotThrowException() {
        assertDoesNotThrow(() -> disposableSwitch.clearAllModes());
    }

    @Test
    @DisplayName("Try clearing all non-empty registries")
    void clearAllModes_CallOnNonEmptyRegistries_GetZeroRegistriesSize() {
        disposableSwitch
                .addPermittedMode(permittedMode, permittedOperation)
                .addRestrictedMode(restrictedMode, restrictedOperation)
        ;
        disposableSwitch.clearAllModes();
        assertEquals(0, disposableSwitch.getPermittedModesRegistrySize());
        assertEquals(0, disposableSwitch.getRestrictedModesRegistrySize());
    }

    @Test
    @DisplayName("Apply on disposed DisposableSwitch")
    void apply_InstanceIsDisposed_ThrowsDisposableSwitchAlreadyDisposedException() {
        registerModes();
        disposableSwitch.dispose();
        assertThrows(DisposableSwitchAlreadyDisposedException.class, () -> disposableSwitch.apply(permittedMode, input));
    }

    @Test
    @DisplayName("Apply on permitted mode several times")
    void apply_CallOnPermittedModeSeveralTimes_DoesNotThrowException() {
        registerModes();
        for (int i = 0; i < random.nextInt(100); i++) {
            assertDoesNotThrow(() -> disposableSwitch.apply(permittedMode, input));
        }
    }

    @Test
    @DisplayName("Apply on restricted mode twice")
    void apply_CallOnRestrictedModeTwice_ThrowDisposableSwitchAlreadyDisposedException() {
        registerModes();
        disposableSwitch.apply(restrictedMode, input);
        assertThrows(DisposableSwitchAlreadyDisposedException.class, () -> disposableSwitch.apply(restrictedMode, input));
    }

    @Test
    @DisplayName("Apply on permitted mode towards getting estimated result")
    void apply_CallOnPermittedMode_GetEstimatedResult() {
        registerModes();
        assertEquals("Test successful", disposableSwitch.apply(permittedMode, input));
    }

    @Test
    @DisplayName("Apply on restricted mode towards getting estimated result")
    void apply_CallOnRestrictedMode_getEstimatedResult() {
        registerModes();
        assertEquals("Test reaches restriction", disposableSwitch.apply(restrictedMode, input));
    }

    @Test
    @DisplayName("TryApply on disposed instance")
    void tryApply_CallOnDisposedInstance_GetEmptyOptional() {
        disposableSwitch.dispose();
        assertEquals(Optional.empty(), disposableSwitch.tryApply(permittedMode, input));
    }

    @Test
    @DisplayName("TryApply with permitted mode")
    void tryApply_CallOnPermittedMode_GetNonEmptyOptional() {
        registerModes();
        assertNotEquals(Optional.empty(), disposableSwitch.tryApply(permittedMode, input));
    }

    @Test
    @DisplayName("TryApply with restricted mode twice")
    void tryApply_CallOnRestrictedModeTwice_GetEmptyOptional() {
        registerModes();
        disposableSwitch.tryApply(restrictedMode, input);
        assertEquals(Optional.empty(), disposableSwitch.tryApply(restrictedMode, input));
    }

    private void registerModes() {
        disposableSwitch
                .addPermittedMode(permittedMode, permittedOperation)
                .addPermittedMode(secondaryPermittedMode, secondaryPermittedOperation)
                .addRestrictedMode(restrictedMode, restrictedOperation)
                .addRestrictedMode(secondaryRestrictedMode, secondaryRestrictedOperation);
    }
}
