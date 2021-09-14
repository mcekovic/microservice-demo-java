package com.igt.demo.microservice.tools;

import org.springframework.boot.actuate.health.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
@Profile("!dev")
public class HelloHealthIndicator implements HealthIndicator {

	@Override public Health health() {
		return Health.up().withDetail("hello", "ready").build();
	}
}
