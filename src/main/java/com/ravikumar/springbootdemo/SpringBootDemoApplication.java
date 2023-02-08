package com.ravikumar.springbootdemo;

import com.ravikumar.springbootdemo.domain.Role;
import com.ravikumar.springbootdemo.domain.User;
import com.ravikumar.springbootdemo.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringBootDemoApplication {
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	};

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}
	@Bean
	CommandLineRunner run(UserServiceImpl userService) {
		return args -> {
			userService.saveRole(new Role("ROLE_ADMIN"));
			userService.saveRole(new Role("ROLE_USER"));
			User admin = userService.saveUser(new User("admin", "admin", "admin@admin.com", "admin", "admin", "541251", new ArrayList<>()));
			User user = userService.saveUser(new User("John", "Smith", "johnsmith@example.com", "user", "user", "541252", new ArrayList<>()));
			userService.addRoleToUser("admin", Role.RoleName.ROLE_ADMIN.name());
			userService.addRoleToUser("user", Role.RoleName.ROLE_USER.name());
			System.out.println(admin);
		};
	}

}
