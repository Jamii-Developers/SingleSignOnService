package com.jamii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
@SpringBootApplication
public class ApplicationStart
{
    public static void main(String[] args) {SpringApplication.run(ApplicationStart.class, args);}
}
