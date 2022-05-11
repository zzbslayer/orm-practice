package com.example.ormpractice;

import com.example.ormpractice.entity.Employee;
import com.example.ormpractice.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataMongoApplication implements CommandLineRunner {

    @Autowired
    private EmployeeRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataMongoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        repository.deleteAll();

        Employee wxd = new Employee("001", "WenXuanda", 20);
        Employee wjs = new Employee("002", "WangJiansi", 20);
        repository.save(wxd);
        repository.save(wjs);

        System.out.println("-------------------------------");
        System.out.println("findAll():");
        repository.findAll().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("findByAge(20):");
        repository.findByAge(20).stream().forEach(System.out::println);
        System.out.println();

        wjs.age = 22;
        System.out.println("--------------------------------");
        System.out.println("Update age of WangJiansi as 22");
        repository.save(wjs);
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("findByAgeBetween(21, 23):");
        repository.findByAgeBetween(21, 23).stream().forEach(System.out::println);
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("delete(wjs)");
        repository.delete(wjs);
        System.out.println();

        System.out.println("--------------------------------");
        System.out.println("existsByByteDanceId('002'):");
        System.out.println(repository.existsByByteDanceId("002"));
        System.out.println();
    }
}
