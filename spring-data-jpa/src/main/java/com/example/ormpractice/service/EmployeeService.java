package com.example.ormpractice.service;

import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.entity.Status;
import com.example.ormpractice.repo.EmployeeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository repository;

    @Autowired
    Session session;

    public void transactionRollBackTest() {
        try {
            this._saveButRollbackOnException();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }

        try {
            this._saveButNoRollbackOnException();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    private Employee _saveButRollbackOnException() {
        System.out.println("Save But Rollback On Exception-------------------------------");

        Employee employee = new Employee();
        employee.name = "exception-no-rollback";

        repository.save(employee);
        throw new RuntimeException("exception-no-rollback");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private Employee _saveButNoRollbackOnException() {
        System.out.println("Save But No Rollback On Exception-------------------------------");
        Employee employee = new Employee();
        employee.name = "exception-rollback";

        repository.save(employee);
        throw new RuntimeException("exception-rollback");
    }

    @Transactional
    public void init() {
        
        System.out.println("Init-------------------------------");
        System.out.println("deleteAll()");
        repository.deleteAll();
        System.out.println();

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-12");
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Employee wxd = new Employee("001", "WenXuanda", 20, Status.NORMAL, date, date, date);
        Employee wjs = new Employee("002", "WangJiansi", 20, Status.BANNED, date, date, date);
        System.out.println("-------------------------------");
        System.out.println("save()");
        repository.save(wxd);
        repository.save(wjs);
        System.out.println();
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        System.out.println("findAll():");
        List<Employee> employees = repository.findAll();
        employees.stream().forEach(System.out::println);

        System.out.println();
        return employees;
    }

    @Transactional
    public void cacheTest() {
        System.out.println("Cache Test-------------------------------");
        System.out.println("findByBytedanceId('001'):");
        Employee wxd = repository.findByByteDanceId("001").get();
        System.out.println(wxd);
        System.out.println("-------------------------------");
        System.out.println("deleteByByteDanceId('001'):");
        repository.deleteByByteDanceId("001");
        System.out.println("-------------------------------");
        System.out.println("findByBytedanceId('001'):");
        repository.findByByteDanceId("001").ifPresent(System.out::println);
        repository.save(wxd);

        System.out.println();
    }

    public void objectStatusTest() {
        try {
            this.springDataObjectStatusTest();
        }
        finally {
            repository.findByByteDanceId("001").ifPresent(System.out::println);
        }

        try {
            this.hibernateObjectStatusTest();
        }
        finally {
            repository.findByByteDanceId("001").ifPresent(System.out::println);
        }
    }

    /**
     * Cache must be disabled to run this test
     * since the modification will affect the object in spring cache
     */
    @Transactional
    private void springDataObjectStatusTest() {
        System.out.println("Spring Data Object Status Test-------------------------------");
        Employee e =  repository.findByByteDanceId("001").get();
        e.name = "modified name";
        System.out.println("Modify e.name as " + e.name);
        System.out.println();
    }

    @Transactional
    private void hibernateObjectStatusTest() {
        System.out.println("Hibernate Object Status Test-------------------------------");
        session.beginTransaction();
        TypedQuery<Employee> query = session.createQuery("from Employee where byteDanceId = '001'");
        Employee e = query.getSingleResult();
        e.name = "modified name";
        System.out.println("Modify e.name as " + e.name);
        System.out.println();
        session.getTransaction().commit();
    }
}
