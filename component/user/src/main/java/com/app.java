package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import com.api.User;

@SpringBootApplication
@ServletComponentScan(value = "com.filter.server")
public class app {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigurableApplicationContext context = SpringApplication.run(app.class, args);
//		User user = context.getBean(User.class);
//		System.out.println(user);

	}

}
