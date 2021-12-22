package com.igt.demo.microservice;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@ConfigurationProperties("hello")
@Getter @Setter
public class HelloConfiguration {

	private String defaultName = "Stranger";
}
