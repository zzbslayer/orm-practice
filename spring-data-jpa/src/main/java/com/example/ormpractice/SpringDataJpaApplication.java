package com.example.ormpractice;

import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataJpaApplication implements CommandLineRunner {
    @Autowired
    private EmployeeRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
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
        System.out.println("save()");
        repository.save(wxd);
        repository.save(wjs);
        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("findAll():");
        repository.findAll().stream().forEach(System.out::println);

        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("findByAgeBetween(19, 21):");
        repository.findByAgeBetween(19, 21).stream().forEach(System.out::println);

        System.out.println();

        System.out.println("-------------------------------");
        System.out.println("findByNameWithJPQL('WangJiansi'):");
        repository.findByNameWithJPQL(wjs.name).stream().forEach(System.out::println);
    }
}
