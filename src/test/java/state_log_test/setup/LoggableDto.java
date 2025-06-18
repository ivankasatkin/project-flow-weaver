package state_log_test.setup;


import io.github.shared.annotations.ToStateLog;
import io.github.shared.contracts.InstanceNameAware;

import java.util.Objects;

public class LoggableDto extends TestDto implements InstanceNameAware {

    public LoggableDto() {
    }

    public LoggableDto(String name, String surname, int age) {
        super(name, surname, age);
    }

    public LoggableDto(TestDto testDto, String login, String password) {
        super(testDto.getName(), testDto.getSurname(), testDto.getAge());
        this.login = login;
        this.password = password;
    }

    @ToStateLog
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggableDto)) return false;
        if (!super.equals(o)) return false;
        LoggableDto that = (LoggableDto) o;
        return Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password);
    }
}
