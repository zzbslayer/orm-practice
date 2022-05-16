package com.example.ormpractice.service;

import com.example.ormpractice.entity.Department;
import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.entity.Status;
import com.example.ormpractice.repo.DepartmentRepository;
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
public class TestService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    Session session;

    @Transactional
    private void deleteAll() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Transactional
    public void init() {

        System.out.println("Init-------------------------------");
        deleteAll();
        System.out.println();

        Department department = new Department("Data Engine");

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-12");
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Employee wxd = new Employee("001", department, "WenXuanda", 20, Status.NORMAL, date, date, date);
        Employee wjs = new Employee("002", department, "WangJiansi", 20, Status.BANNED, date, date, date);
        employeeRepository.save(wxd);
        employeeRepository.save(wjs);
        System.out.println();
    }

    public void transactionRollBackTest() {
        try {
            this.saveButRollbackOnException();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }

        try {
            this.saveButNoRollbackOnException();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            this.findAll();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    private Employee saveButRollbackOnException() {
        System.out.println("Save But Rollback On Exception-------------------------------");

        Employee employee = new Employee();
        employee.name = "exception-no-rollback";

        employeeRepository.save(employee);
        throw new RuntimeException("exception-no-rollback");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private Employee saveButNoRollbackOnException() {
        System.out.println("Save But No Rollback On Exception-------------------------------");
        Employee employee = new Employee();
        employee.name = "exception-rollback";

        employeeRepository.save(employee);
        throw new RuntimeException("exception-rollback");
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        System.out.println("findAll():");
        List<Employee> employees = employeeRepository.findAll();
        employees.stream().forEach(System.out::println);

        System.out.println();
        return employees;
    }

    @Transactional
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
        try {
            this.springDataObjectStatusTest();
        }
        finally {
            employeeRepository.findByByteDanceId("001").ifPresent(System.out::println);
        }

        try {
            this.hibernateObjectStatusTest();
        }
        finally {
            employeeRepository.findByByteDanceId("001").ifPresent(System.out::println);
        }
    }

    /**
     * Cache must be disabled to run this test
     * since the modification will affect the object in spring cache
     */
    @Transactional
    private void springDataObjectStatusTest() {
        System.out.println("Spring Data Object Status Test-------------------------------");
        Employee e =  employeeRepository.findByByteDanceId("001").get();
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

    @Transactional     // this is a must for lock
    public void pessimisticLockTest() {
        employeeRepository.findByByteDanceIdWithPessimisticLock("001");
    }

    public void optimisticLockTest() {
        _optimisticLockTest();

        Employee e = employeeRepository.findByByteDanceId("001").get();
        System.out.println("After: " + e);
    }

    @Transactional
    private void _optimisticLockTest() {
        System.out.println("Optimistic Lock Test1-------------------------------");
        Employee e = employeeRepository.findByByteDanceId("001").get();
        System.out.println("Before: " + e);
        e.name = "optimistic lock test 1";
        employeeRepository.save(e);
    }

    public void fetchLazyTest() {
        Department department;
        System.out.println("Fetch Lazy Failure-------------------------------");
        try {
            department = fetchLazyFailure();
            System.out.println(department.employees);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println("Fetch Lazy In Hibernate Session-------------------------------");
        department = fetchLazyInHibernateSession();
        System.out.println(department.employees);
        System.out.println();

        System.out.println("Fetch Lazy In Spring Data-------------------------------");
        department = fetchLazyInSpringData();
        System.out.println(department.employees);
        System.out.println();
    }

    @Transactional
    private Department fetchLazyFailure() {
        Department department = departmentRepository.findByDepartmentName("Data Engine").get();
        return department;
    }

    @Transactional
    private Department fetchLazyInHibernateSession() {
        session.beginTransaction();
        TypedQuery<Department> query = session.createQuery("from Department where departmentName = 'Data Engine'");
        Department department = query.getSingleResult();
        department.employees.size();
        session.getTransaction().commit();
        return department;
    }

    @Transactional
    private Department fetchLazyInSpringData() {
        Department department = departmentRepository.findByDepartmentNameEagerly("Data Engine").get();
        return department;
    }

    public void updateTest() {
        System.out.println("Update test-------------------------------");
        Employee e = employeeRepository.findByByteDanceId("001").get();
        _updateTest(e);
    }

    @Transactional
    private void _updateTest(Employee e) {
        System.out.println("_updateTest");
        e.name = "update test";
        employeeRepository.save(e);
    }
}
