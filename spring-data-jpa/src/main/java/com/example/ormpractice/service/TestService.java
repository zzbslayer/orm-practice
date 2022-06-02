package com.example.ormpractice.service;

import com.example.ormpractice.entity.Department;
import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.repo.DepartmentRepository;
import com.example.ormpractice.repo.EmployeeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.IllegalTransactionStateException;

import java.util.List;

@Service
public class TestService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    Session session;

    @Autowired
    TransactionalService transactionalService;

    public void init() {
        transactionalService.initTx();
    }

    public void transactionRollBackTest() {
        try {
            transactionalService.saveButRollbackOnExceptionTx();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }

        try {
            transactionalService.saveButNoRollbackOnExceptionTx();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }
    }


    public List<Employee> findAll() {
        System.out.println("findAll():");
        List<Employee> employees = employeeRepository.findByAgeBetween(18,30);
        employees.stream().forEach(System.out::println);

        System.out.println();
        return employees;
    }

    public void cacheTest() {
        System.out.println("Cache Test-------------------------------");
        System.out.println("findByBytedanceId('001'):");
        Employee wxd = employeeRepository.findByByteDanceId("001").get();
        System.out.println(wxd);
        System.out.println("-------------------------------");
        System.out.println("deleteByByteDanceId('001'):");
        employeeRepository.deleteByByteDanceId("001");
        System.out.println("-------------------------------");
        System.out.println("findByBytedanceId('001'):");
        employeeRepository.findByByteDanceId("001").ifPresent(System.out::println);
        employeeRepository.save(wxd);

        System.out.println();
    }

    public void objectStatusTest() {
        transactionalService.springDataObjectStatusTx();
        employeeRepository.findByByteDanceId("001").ifPresent(System.out::println);
    }

    public void lockTest() {
        transactionalService.pessimisticLockTx();

        transactionalService.optimisticLockTx();
        Employee e = employeeRepository.findByByteDanceId("001").get();
        System.out.println("After: " + e);
    }



    public void fetchLazyTest() {
        Department department;
        System.out.println("Fetch Lazy Failure-------------------------------");
        try {
            department = transactionalService.fetchLazyFailureTx();
            System.out.println(department.getEmployees());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println("Fetch Lazy In Hibernate Session-------------------------------");
        department = transactionalService.fetchLazyInHibernateSessionTx();
        System.out.println(department.getEmployees());
        System.out.println();

        System.out.println("Fetch Lazy In Spring Data-------------------------------");
        department = transactionalService.fetchLazyInSpringDataTx();
        System.out.println(department.getEmployees());
        System.out.println();
    }

    public void updateTest() {
        System.out.println("Update test-------------------------------");
        Employee e = employeeRepository.findByByteDanceId("001").get();
        transactionalService.updateTx1(e);
        //transactionalService.updateTx2();
    }

    public void transactionTest() {
        transactionalService.requiredTx();
        try {
            transactionalService.mandatoryTx();
        }
        catch (IllegalTransactionStateException e) {
            e.printStackTrace();
        }
    }

    public void pageableTest() {
        System.out.println("Pageable test-------------------------------");

        Pageable pageable = PageRequest.of( 0 , 3 , Sort.Direction.DESC, "id" );
        Page<Employee> page = transactionalService.pageableTx(pageable);
        System.out.println("Total pages: " + page.getTotalPages());
        System.out.println("Total elements: " + page.getTotalElements());
        page.stream().forEach(System.out::println);
    }
}
