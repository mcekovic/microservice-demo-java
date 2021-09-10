package com.igt.demo.microservice;

import java.util.*;
import java.util.concurrent.*;

import io.micrometer.core.instrument.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

	@Autowired
	private HelloConfiguration config;

	@Autowired
	private MeterRegistry registry;

	private final Map<String, Counter> counters = new ConcurrentHashMap<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/hello")
	public String hello(
		@RequestParam(required = false) String name
	) {
		var resolvedName = getName(name);
		incrementCounter(resolvedName);
		var message = "Hello " + resolvedName + "!";
		LOGGER.info(message);
		return message;
	}

	private String getName(String name) {
		return isNullOrBlank(name) ? config.getDefaultName() : name.strip();
	}

	private static boolean isNullOrBlank(String s) {
		return s == null || s.isBlank();
	}


	// Metrics

	private void incrementCounter(String name) {
		counters.computeIfAbsent(name, this::createCounter).increment();
	}

	private Counter createCounter(String name) {
		return registry.counter("hello.count", Tags.of("name", name));
	}
}
