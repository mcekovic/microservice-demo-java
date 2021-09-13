package com.igt.demo.microservice;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

	@Autowired
	private HelloService service;

	@GetMapping("/hello")
	public String hello(
		@RequestParam(required = false) String name
	) {
		return service.hello(name);
	}
}
