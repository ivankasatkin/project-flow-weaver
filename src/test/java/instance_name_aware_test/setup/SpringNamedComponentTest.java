package instance_name_aware_test.setup;

import io.github.shared.contracts.InstanceNameAware;
import org.springframework.stereotype.Component;

@Component("componentTest")
public class SpringNamedComponentTest implements InstanceNameAware {
}
