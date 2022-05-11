package com.example.ormpractice;

import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.repo.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBatisApplication implements CommandLineRunner {

    @Autowired
    private EmployeeMapper repository;

    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("-------------------------------");
        System.out.println("deleteAll()");
        repository.deleteAll();
        System.out.println();

        Employee wxd = new Employee("001", "WenXuanda", 20);
        Employee wjs = new Employee("002", "WangJiansi", 20);
        System.out.println("-------------------------------");
        System.out.println("insert()");
        System.out.println(wxd);
        System.out.println(wjs);
        repository.insert(wxd);
        repository.insert(wjs);
        System.out.println();


        System.out.println("-------------------------------");
        System.out.println("findAllWithResultType():");
        repository.findAllWithResultType().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("findAllWithResultMap():");
        repository.findAllWithResultMap().stream().forEach(System.out::println);

        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("findByName('WangJiansi'):");
        repository.findByName(wjs.name).stream().forEach(System.out::println);
    }
}
