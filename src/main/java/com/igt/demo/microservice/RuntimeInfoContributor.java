package com.igt.demo.microservice;

import java.lang.management.*;
import java.util.*;

import org.apache.catalina.startup.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.actuate.info.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Component
@Profile("!dev")
public class RuntimeInfoContributor implements InfoContributor {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String UNKNOWN = "Unknown";

	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeInfoContributor.class);

	@Override public void contribute(Info.Builder builder) {
		builder.withDetail("os", Map.of(
			"name", System.getProperty("os.name"),
			"version", System.getProperty("os.version")
		)).withDetail("runtime", Map.of(
			"jvm.version", ManagementFactory.getRuntimeMXBean().getVmVersion(),
			"spring-boot.version", getJavaPackageVersion(SpringApplication.class),
			"spring.version", getJavaPackageVersion(ApplicationContext.class),
			"tomcat.version", getJavaPackageVersion(Tomcat.class),
			"h2.version", getH2Version()
		));
	}

	private String getJavaPackageVersion(Class<?> cls) {
		try {
			return ensureVersion(cls.getPackage().getImplementationVersion());
		}
		catch (Exception ex) {
			LOGGER.error("Error getting Java Package version for class " + cls.getName(), ex);
			return UNKNOWN;
		}
	}

	private String getH2Version() {
		try {
			return ensureVersion(jdbcTemplate.queryForObject("SELECT H2VERSION() FROM DUAL", String.class));
		}
		catch (Exception ex) {
			LOGGER.error("Error getting H2 Database version", ex);
			return UNKNOWN;
		}
	}

	private static String ensureVersion(String version) {
		return version != null ? version : UNKNOWN;
	}
}
