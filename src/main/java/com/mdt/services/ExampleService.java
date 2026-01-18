package com.mdt.services;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleService {

    @Inject
    public ExampleService() {
    }

    public void doSomething() {
        log.info("ExampleService is doing something!");
    }
}
