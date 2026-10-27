package ru.edu.pr04;

public final class Employee {
    private final String id;
    private final String fullName;

    public Employee(String id, String fullName) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Укажите id сотрудника");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Укажите ФИО сотрудника");
        this.id = id.trim();
        this.fullName = fullName.trim();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
}
