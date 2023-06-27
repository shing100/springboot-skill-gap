package com.kingname.api;

import com.kingname.core.SimpleEntity;
import com.kingname.core.SimpleEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EntityScan("com.kingname.core")
@EnableJpaRepositories("com.kingname.core")
@RestController
@SpringBootApplication
public class ApiApplication {

    private final SimpleEntityRepository repository;

    public ApiApplication(SimpleEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SimpleEntity> findAll() {
        return repository.findAll();
    }

    @PostMapping("/save")
    public SimpleEntity save() {
        final SimpleEntity entity = new SimpleEntity();
        return repository.save(entity);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
