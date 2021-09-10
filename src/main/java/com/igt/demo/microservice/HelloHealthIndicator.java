package com.igt.demo.microservice;

import org.springframework.boot.actuate.health.*;
import org.springframework.stereotype.*;

@Component
public class HelloHealthIndicator implements HealthIndicator {

	@Override public Health health() {
		return Health.up().withDetail("hello", "ready").build();
	}
}
