package com.example.resttemplateworkship;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.resttemplateworkship.Log.log;

@RestController
public class TargetController {

    @PostMapping(value = "/post")
    @SneakyThrows
    public void postTarget() {
        log("start");
        Thread.sleep(1000);
        log("end");
    }
}
