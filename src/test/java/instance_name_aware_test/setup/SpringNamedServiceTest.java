package instance_name_aware_test.setup;

import io.github.shared.contracts.InstanceNameAware;
import org.springframework.stereotype.Service;

@Service("serviceTest")
public class SpringNamedServiceTest implements InstanceNameAware {
}
