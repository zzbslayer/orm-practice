package com.example.ormpractice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "memberaccountjpa")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(name = "byteDanceId")
    public String byteDanceId;
    @Column(name = "name")
    public String name;
    public Integer age;

    // THIS IS A MUST
    public Employee() {}

    public Employee(String byteDanceId, String name, Integer age) {
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