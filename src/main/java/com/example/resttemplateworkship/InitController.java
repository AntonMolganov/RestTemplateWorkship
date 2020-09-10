package com.example.resttemplateworkship;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @Autowired
    private TestRunner testRunner;

    @GetMapping(value = "/start")
    @SneakyThrows
    public void startTest() {
        testRunner.test();
    }

}
