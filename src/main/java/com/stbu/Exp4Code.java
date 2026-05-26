package com.stbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.stbu.mapper")
public class Exp4Code {
    public static void main(String[] args) {
        SpringApplication.run(Exp4Code.class, args);
    }
}