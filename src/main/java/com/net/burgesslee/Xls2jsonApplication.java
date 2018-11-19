package com.net.burgesslee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages={"com.net.burgesslee.dao"})
public class Xls2jsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(Xls2jsonApplication.class, args);
	}
}