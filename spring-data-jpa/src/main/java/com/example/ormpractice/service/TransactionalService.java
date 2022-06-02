package com.example.ormpractice.service;

import com.example.ormpractice.entity.Department;
import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.entity.Status;
import com.example.ormpractice.repo.DepartmentRepository;
import com.example.ormpractice.repo.EmployeeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransactionalService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    Session session;

    @Transactional
    public void initTx() {

        System.out.println("Init-------------------------------");
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
        employeeRepository.flush();
        departmentRepository.flush();
        System.out.println();

        Department department = Department.builder().departmentName("Data Engine").build();
        Department testDepartment = Department.builder().departmentName("Test Department").build();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-12");
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Employee wxd = Employee.builder()
                .id(1)
                .byteDanceId("001")
                .department(department)
                .name("WenXuanda")
                .age(20)
                .status(Status.NORMAL)
                .date(date)
                .time(date)
                .timestamp(date)
                .build();
        Employee wjs = Employee.builder()
                .id(2)
                .byteDanceId("002")
                .department(department)
                .name("WangJiansi")
                .age(20)
                .status(Status.BANNED)
                .date(date)
                .time(date)
                .timestamp(date)
                .build();
        Employee hza = Employee.builder()
                .id(3)
                .byteDanceId("003")
                .department(testDepartment)
                .name("Huangziang")
                .age(20)
                .status(Status.NORMAL)
                .date(date)
                .time(date)
                .timestamp(date)
                .build();

        departmentRepository.save(department);
        departmentRepository.save(testDepartment);
        employeeRepository.save(wxd);
        employeeRepository.save(wjs);
        employeeRepository.save(hza);
        System.out.println();
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public Employee saveButRollbackOnExceptionTx() {
        System.out.println("Save But Rollback On Exception-------------------------------");

        Employee employee = new Employee();
        employee.setName("exception-no-rollback");

        employeeRepository.save(employee);
        throw new RuntimeException("exception-no-rollback");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Employee saveButNoRollbackOnExceptionTx() {
        System.out.println("Save But No Rollback On Exception-------------------------------");
        Employee employee = new Employee();
        employee.setName("exception-rollback");

        employeeRepository.save(employee);
        throw new RuntimeException("exception-rollback");
    }

    /**
     * Cache must be disabled to run this test
     * since the modification will affect the object in spring cache
     */
    @Transactional
    public void springDataObjectStatusTx() {
        System.out.println("Spring Data Object Status Test-------------------------------");
        Employee e =  employeeRepository.findByByteDanceId("001").get();
        System.out.println(e);
        e.setName("modified name");
        System.out.println("Modify e.name as " + e.getName());
        System.out.println();
    }

    @Transactional
    public void hibernateObjectStatusTx() {
        System.out.println("Hibernate Object Status Test-------------------------------");
        session.beginTransaction();
        TypedQuery<Employee> query = session.createQuery("from Employee where byteDanceId = '001'");
        Employee e = query.getSingleResult();
        e.setName("modified name");
        System.out.println("Modify e.name as " + e.getName());
        System.out.println();
        session.getTransaction().commit();
    }

    @Transactional     // this is a must for lock
    public void pessimisticLockTx() {
        employeeRepository.findByByteDanceIdWithPessimisticLock("001");
    }

    @Transactional
    public void optimisticLockTx() {
        System.out.println("Optimistic Lock Test1-------------------------------");
        Employee e = employeeRepository.findByByteDanceId("001").get();
        System.out.println("Before: " + e);
        e.setName("optimistic lock test 1");
        employeeRepository.save(e);
    }

    @Transactional
    public Department fetchLazyFailureTx() {
        Department department = departmentRepository.findByDepartmentName("Data Engine").get();
        return department;
    }

    @Transactional
    public Department fetchLazyInHibernateSessionTx() {
        session.beginTransaction();
        TypedQuery<Department> query = session.createQuery("from Department where departmentName = 'Data Engine'");
        Department department = query.getSingleResult();
        department.getEmployees().size();
        session.getTransaction().commit();
        return department;
    }

    @Transactional
    public Department fetchLazyInSpringDataTx() {
        Department department = departmentRepository.findByDepartmentNameEagerly("Data Engine").get();
        return department;
    }

    @Transactional
    public void updateTx1(Employee e) {
        System.out.println("updateTx1");
        e.setName("updateTx1");
        employeeRepository.save(e);
    }

    @Transactional
    public void updateTx2() {
        System.out.println("updateTx2");
        Employee e = employeeRepository.findByByteDanceId("001").get();
        e.setName("updateTx2");
        employeeRepository.save(e);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredTx() {
        System.out.println("required");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void mandatoryTx() {
        System.out.println("mandatory");
    }

    @Transactional
    public Page<Employee> pageableTx(Pageable pageable) {
        Page<Employee> page = employeeRepository.findByName( "WangJiansi" ,pageable);
        return page;
    }
}
