package com.example.demo;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class SomeResourceController {

    @HystrixCommand(fallbackMethod = "defaultValue", commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="1500")})
    @RequestMapping(path = "/resource-with-hystrix", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String someResource(){
        URI uri = URI.create("http://localhost:8080/some-resource");
        RestOperations restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    private String defaultValue(){
        return "local value in case of something goes wrong";
    }
}