package com.example.ormpractice.repo;

import com.example.ormpractice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByDepartmentName(String departmentName);

    @Query("select d from Department d JOIN FETCH d.employees WHERE d.departmentName = ?1")
    Optional<Department> findByDepartmentNameEagerly(String departmentName);
}
