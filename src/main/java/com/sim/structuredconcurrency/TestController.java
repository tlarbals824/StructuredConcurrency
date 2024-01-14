package com.sim.structuredconcurrency;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/sync")
    public Long sync(){
        return testService.calculateSync();
    }

    @GetMapping("/async")
    public Long async(){
        return testService.calculateAsync();
    }

    @GetMapping("/asyncWithStructuredConcurrency")
    public Long asyncWithStructuredConcurrency(){
        return testService.calculateAsyncWithStructuredConcurrency();
    }
}
