package com.poemSys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PoemApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PoemApplication.class, args);
    }

}
