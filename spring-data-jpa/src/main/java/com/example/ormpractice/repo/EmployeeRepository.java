package com.example.ormpractice.repo;

import com.example.ormpractice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("select e from Employee e WHERE e.name = ?1")
    List<Employee> findByNameWithJPQL(String name);
    List<Employee> findByName(String name);
    List<Employee> findByAgeBetween(int a1, int a2);
}
