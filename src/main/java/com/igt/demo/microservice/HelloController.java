package com.igt.demo.microservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello(
		@RequestParam(required = false) String name
	) {
		return "Hello " + getName(name) + "!";
	}

	private static String getName(String name) {
		return isNullOrBlank(name) ? "stranger" : name.strip();
	}

	private static boolean isNullOrBlank(String s) {
		return s == null || s.isBlank();
	}
}
