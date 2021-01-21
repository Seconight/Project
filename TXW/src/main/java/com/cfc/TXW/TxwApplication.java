package com.cfc.TXW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "Controller")
public class TxwApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxwApplication.class, args);
	}

}
