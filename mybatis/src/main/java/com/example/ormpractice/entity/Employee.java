package com.example.ormpractice.entity;


public class Employee {

    public Integer id;

    public String byteDanceId;
    public String name;
    public Integer age;

    // THIS IS A MUST
    public Employee(String byteDanceId, String name, Integer age) {
        this.byteDanceId = byteDanceId;
        this.name = name;
        this.age = age;
    }

    public Employee(Integer id, String byteDanceId, String name, Integer age) {
        this.id = id;
        this.byteDanceId = byteDanceId;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(
                "Employee[id=%d, byteDanceId='%s', name='%s', age='%d']",
                id, byteDanceId, name, age);
    }

}