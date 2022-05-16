package com.example.ormpractice;

import com.example.ormpractice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EnableCaching
@SpringBootApplication
public class SpringDataJpaApplication implements CommandLineRunner {
    @Autowired
    private EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        employeeService.init();
        employeeService.findAll();

//        employeeService.transactionRollBackTest();
//        employeeService.cacheTest();

        employeeService.objectStatusTest();
    }
}
