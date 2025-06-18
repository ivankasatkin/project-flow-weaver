package state_log_test.setup;

import java.util.Objects;

public class TestDto {

    public TestDto() {
    }

    public TestDto(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    private String name;
    private String surname;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestDto)) return false;
        TestDto testDto = (TestDto) o;
        return age == testDto.age && Objects.equals(name, testDto.name) && Objects.equals(surname, testDto.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age);
    }
}
