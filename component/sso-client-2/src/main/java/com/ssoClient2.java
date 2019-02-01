package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(value = "com.filter.client")
public class ssoClient2 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SpringApplication.run(ssoClient2.class, args);
    }

}
