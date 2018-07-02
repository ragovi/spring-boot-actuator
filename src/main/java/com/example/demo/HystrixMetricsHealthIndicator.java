package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandMetrics;

@Component
public class HystrixMetricsHealthIndicator extends AbstractHealthIndicator {

	/**
	 * Method adds the hystrix commands to the builder 
	 * to check for the circuit breaker open, close states.
	 */
	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		List<String> breakers = new ArrayList<String>();
        Collection<HystrixCommandMetrics> metrics = HystrixCommandMetrics.getInstances();
        for (HystrixCommandMetrics command : metrics) {
			HystrixCircuitBreaker breaker = HystrixCircuitBreaker.Factory.getInstance(command.getCommandKey() );
			if(breaker.isOpen()){
				breakers.add(command.getCommandGroup().name()+ " :: "+ command.getCommandKey().name());
			}
		}
        
        if(!breakers.isEmpty()) {
        	builder.outOfService().withDetail("openCircuitBreakers", breakers);
        } else {
        	builder.up();
        }
	}
}