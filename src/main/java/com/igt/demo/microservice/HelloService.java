package com.igt.demo.microservice;

import java.util.*;
import java.util.concurrent.*;

import com.igt.demo.microservice.tools.*;
import io.micrometer.core.annotation.*;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class HelloService {

	@Autowired
	private HelloConfiguration config;

	@Autowired
	private MeterRegistry registry;

	private final Map<String, Counter> counters = new ConcurrentHashMap<>();

	@Timed @Traced
	public String hello(String name) {
		var resolvedName = getName(name);
		incrementCounter(resolvedName);
		return "Hello " + resolvedName + "!";
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
