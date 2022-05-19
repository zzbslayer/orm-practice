package com.example.ormpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    Integer id;

    @Column(name = "byte_dance_id", unique = true) // this is optional if column name equals to field name
    String byteDanceId;
    String name;
    Integer age;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id", nullable = false)
    Department department;

    @Enumerated(EnumType.ORDINAL)
    Status status;

    @Temporal(TemporalType.DATE)
    Date date;

    @Temporal(TemporalType.TIME)
    Date time;

    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    @Version
    Integer version;

    @Transient
    boolean isNew = true;

    @Override
    public String toString() {
        return String.format(
                "Employee[id=%d, byteDanceId='%s', departmentName='%s', name='%s', age='%d', status='%s', date='%s', time='%s', timestamp='%s', version=%d]",
                id, byteDanceId, department.departmentName, name, age, status, date, time, timestamp, version);
    }
}