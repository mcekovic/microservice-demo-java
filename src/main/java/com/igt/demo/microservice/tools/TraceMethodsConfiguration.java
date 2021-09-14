package com.igt.demo.microservice.tools;

import org.aopalliance.aop.*;
import org.springframework.aop.*;
import org.springframework.aop.interceptor.*;
import org.springframework.aop.support.*;
import org.springframework.aop.support.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;

import static org.springframework.beans.factory.config.BeanDefinition.*;

@Configuration @Role(ROLE_INFRASTRUCTURE)
@ConditionalOnProperty("hello.method-tracing.enabled")
public class TraceMethodsConfiguration {

	@Bean @Role(ROLE_INFRASTRUCTURE)
	public Advice traceInterceptor() {
		var traceInterceptor = new CustomizableTraceInterceptor();
		traceInterceptor.setUseDynamicLogger(true);
		traceInterceptor.setEnterMessage("$[methodName]($[arguments])");
		traceInterceptor.setExitMessage("~$[methodName]=$[returnValue] ($[invocationTime])");
		traceInterceptor.setExceptionMessage("!$[methodName]");
		return traceInterceptor;
	}

	@Bean @Role(ROLE_INFRASTRUCTURE)
	public Advisor traceAdvisor() {
		var tracedPointcut = new ComposablePointcut(new AnnotationMatchingPointcut(Traced.class))
			.union(new AnnotationMatchingPointcut(null, Traced.class));
		return new DefaultPointcutAdvisor(tracedPointcut, traceInterceptor());
	}
}
