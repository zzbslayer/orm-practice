package com.example.ormpractice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    //@Column(name = "byteDanceId") // this is optional if column name equals to field name
    public String byteDanceId;
    public String name;
    public Integer age;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Temporal(TemporalType.DATE)
    Date date;

    @Temporal(TemporalType.TIME)
    Date time;

    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    // THIS IS A MUST
    public Employee() {}

    public Employee(String byteDanceId, String name, Integer age, Status status, Date date, Date time, Date timestamp) {
        this.byteDanceId = byteDanceId;
        this.name = name;
        this.age = age;
        this.status = status;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "Employee[id=%d, byteDanceId='%s', name='%s', age='%d', status='%s', date='%s', time='%s', timestamp='%s']",
                id, byteDanceId, name, age, status, date, time, timestamp);
    }
}