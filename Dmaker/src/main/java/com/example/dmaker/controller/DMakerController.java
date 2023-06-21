package com.example.dmaker.controller;

import com.example.dmaker.dto.CreateDeveloper;
import com.example.dmaker.dto.DeveloperDetailDto;
import com.example.dmaker.dto.DeveloperDto;
import com.example.dmaker.dto.EditDeveloper;
import com.example.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/delelopers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/deleloper/{memberId}")
    public DeveloperDetailDto getAllDeveloperDetail(@PathVariable String memberId) {
        log.info("GET /developer HTTP/1.1");

        return dMakerService.getAllDeveloperDetail(memberId);
    }


    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(@Valid @RequestBody CreateDeveloper.Request request) {
        log.info("request: {}", request);

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/deleloper/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable String memberId, @Valid @RequestBody EditDeveloper.Request request) {
        log.info("PUT /developer HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @PutMapping("/deleloper/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable String memberId) {
        log.info("DELETE /developer HTTP/1.1");

        return dMakerService.deleteDeveloper(memberId);
    }
}
