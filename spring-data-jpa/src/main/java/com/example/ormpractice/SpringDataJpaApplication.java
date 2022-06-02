package com.example.ormpractice;

import com.example.ormpractice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableCaching
@SpringBootApplication
public class SpringDataJpaApplication implements CommandLineRunner {
    @Autowired
    private TestService testService;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        testService.init();
        testService.findAll();
//        testService.updateTest();

//        testService.transactionRollBackTest();
//        testService.cacheTest();

//        testService.objectStatusTest();
//        testService.optimisticLockTest();
//        testService.fetchLazyTest();
//        testService.transactionTest();

        testService.pageableTest();
    }
}
