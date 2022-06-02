package com.example.ormpractice.repo;

import com.example.ormpractice.entity.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("select e from Employee e WHERE e.name = ?1")
    List<Employee> findByNameWithJPQL(String name);

    List<Employee> findByName(String name);
    List<Employee> findByAgeBetween(int a1, int a2);

    @Cacheable(value = "employees", key = "#byteDanceId", unless = "#result == null")
    Optional<Employee> findByByteDanceId(String byteDanceId);

    @CachePut(value = "employees", key = "#employee.byteDanceId")
    <S extends Employee> S save(S employee);

    @CacheEvict(value = "employees", allEntries = true)
    void deleteAll();

    @CacheEvict(value = "employees", key = "#byteDanceId")
    void deleteByByteDanceId(String byteDanceId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("select e from Employee e WHERE e.byteDanceId = ?1")
    Optional<Employee> findByByteDanceIdWithPessimisticLock(String byteDanceId);

    @Query("select e from Employee e WHERE e.byteDanceId = ?1")
    Optional<Employee> findByByteDanceIdWithOptimisticForceIncrementLock(String byteDanceId);

    Page<Employee> findAll(Pageable pageable);

    @Query("select e from Employee e WHERE e.name = :name")
    Page<Employee> findByName(@Param("name") String name, Pageable pageable);
}
