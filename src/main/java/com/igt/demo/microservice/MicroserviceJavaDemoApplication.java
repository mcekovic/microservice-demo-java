package com.igt.demo.microservice;

import io.micrometer.core.aop.*;
import io.micrometer.core.instrument.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;

@SpringBootApplication
public class MicroserviceJavaDemoApplication {

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceJavaDemoApplication.class, args);
	}
}
