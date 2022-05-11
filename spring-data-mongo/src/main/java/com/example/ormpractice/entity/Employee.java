package com.example.ormpractice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("employee")
public class Employee {

    @Id
    public String id;

    public String byteDanceId;
    public String name;
    public Integer age;

    public Employee(String byteDanceId, String name, Integer age) {
        this.byteDanceId = byteDanceId;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(
                "Employee[id=%s, byteDanceId='%s', name='%s', age='%d']",
                id, byteDanceId, name, age);
    }
}