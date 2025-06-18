package instance_name_aware_test;

import instance_name_aware_test.setup.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstanceNameAwareTest {

    private SpringNamedComponentTest springNamedComponent = new SpringNamedComponentTest();
    private SpringNamedServiceTest springNamedService = new SpringNamedServiceTest();
    private SpringComponentTest springComponent = new SpringComponentTest();
    private SpringRepositoryTest springRepository = new SpringRepositoryTest();
    private NonSpringClassTest nonSpringClass = new NonSpringClassTest();

    @Test
    void getInstanceName_CallOnNonSpringClass_ReturnDecapitalizedClassSimpleName() {
        assertEquals("nonSpringClassTest", nonSpringClass.getInstanceName());
    }

    @Test
    void getInstanceName_CallOnSpringComponent_ReturnDecapitalizedClassSimpleName() {
        assertEquals("springComponentTest", springComponent.getInstanceName());
    }

    @Test
    void getInstanceName_CallOnSpringRepository_ReturnDecapitalizedClassSimpleName() {
        assertEquals("springRepositoryTest", springRepository.getInstanceName());
    }

    @Test
    void getInstanceName_CallOnSpringNamedComponent_ReturnValuedComponentName() {
        assertEquals("componentTest", springNamedComponent.getInstanceName());
    }

    @Test
    void getInstanceName_CallOnSpringNamedService_ReturnValuedServiceName() {
        assertEquals("serviceTest", springNamedService.getInstanceName());
    }
}