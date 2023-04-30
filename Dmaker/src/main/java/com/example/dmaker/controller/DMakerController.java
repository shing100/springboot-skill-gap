package com.example.dmaker.controller;

import com.example.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/delelopers")
    public List<String> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("snow", "Elsa", "Olaf");
    }

    @PostMapping("/create-developers")
    public List<String> createDevelopers() {
        log.info("POST /create-developers HTTP/1.1");
        dMakerService.createDeveloper();

        return Collections.singletonList("Olaf");
    }
}
