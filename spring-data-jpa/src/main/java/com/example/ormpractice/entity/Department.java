package com.example.ormpractice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {

    public Department() {}

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(unique = true)
    public String departmentName;

    @Version
    public Integer version;


    @OneToMany(mappedBy = "department")
    public Set<Employee> employees;

    @Override
    public String toString() {
        return String.format(
                "Department[id=%d, departmentName='%s', version=%d]",
                id, departmentName, version);
    }
}
