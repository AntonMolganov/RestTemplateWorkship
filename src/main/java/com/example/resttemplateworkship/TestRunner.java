package com.example.resttemplateworkship;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static com.example.resttemplateworkship.Log.log;

@Service
public class TestRunner {

    private static final int REQUESTS_NUM = 10;
    private static final String URL = "http://localhost:8080/post";


    @Autowired @Qualifier("defaultRestTemplate")
    RestTemplate defaultRestTemplate;

    @Autowired
    AsyncRestTemplate asyncRestTemplate;

    @Autowired @Qualifier("advancedRestTemplate")
    RestTemplate advancedRestTemplate;

    @Autowired @Qualifier("advancedRestTemplate2")
    RestTemplate advancedRestTemplate2;

    @Autowired
    private ForkJoinPool threadPool;

    public void test(){
        defaultTest();
        System.out.println();
        asyncTest();
        System.out.println();
        advancedTest();
        System.out.println();
        advancedTest2();
    }

    private void defaultTest() {
        log("start default RestTemplate testing...");

        List<CompletableFuture<Void>> tasks = new LinkedList<>();
        for (int i = 0; i < REQUESTS_NUM; i++) {
            tasks.add(CompletableFuture.runAsync(this::runDefaultPost, threadPool));
        }
        tasks.forEach(CompletableFuture::join);

        log("end default RestTemplate testing...");
    }

    private void asyncTest() {
        log("start async RestTemplate testing...");

        List<CompletableFuture<Void>> tasks = new LinkedList<>();
        for (int i = 0; i < REQUESTS_NUM; i++) {
            tasks.add(CompletableFuture.runAsync(this::runAsyncPost, threadPool));
        }
        tasks.forEach(CompletableFuture::join);

        log("end async RestTemplate testing...");
    }

    private void advancedTest() {
        log("start advanced RestTemplate testing...");

        List<CompletableFuture<Void>> tasks = new LinkedList<>();
        for (int i = 0; i < REQUESTS_NUM; i++) {
            tasks.add(CompletableFuture.runAsync(this::runAdvancedPost, threadPool));
        }
        tasks.forEach(CompletableFuture::join);

        log("end advanced RestTemplate testing...");
    }

    private void advancedTest2() {
        log("start advanced2 RestTemplate testing...");

        List<CompletableFuture<Void>> tasks = new LinkedList<>();
        for (int i = 0; i < REQUESTS_NUM; i++) {
            tasks.add(CompletableFuture.runAsync(this::runAdvancedPost2, threadPool));
        }
        tasks.forEach(CompletableFuture::join);

        log("end advanced2 RestTemplate testing...");
    }

    private void runDefaultPost() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL).build();
        defaultRestTemplate.postForEntity(uriComponents.toUriString(), new HttpEntity<>(null, null), String.class);
    }

    @SneakyThrows
    private void runAsyncPost() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL).build();
        asyncRestTemplate.postForEntity(uriComponents.toUriString(), new HttpEntity<>(null, null), String.class).get();
    }

    private void runAdvancedPost() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL).build();
        advancedRestTemplate.postForEntity(uriComponents.toUriString(), new HttpEntity<>(null, null), String.class);
    }

    private void runAdvancedPost2() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URL).build();
        advancedRestTemplate2.postForEntity(uriComponents.toUriString(), new HttpEntity<>(null, null), String.class);
    }

}
