package com.example.ormpractice.repo;

import com.example.ormpractice.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Optional<Employee> findByByteDanceId(String byteDanceId);
    List<Employee> findByName(String name);
    List<Employee> findByAge(int age);

    boolean existsByByteDanceId(String byteDanceId);

    @Query("{'age': {'$gte': ?0, '$lte': ?1}}")
    List<Employee> findByAgeBetween(int from, int to);
}
