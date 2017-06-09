package com.appgoo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.appgoo.app.*" }, excludeFilters = {
		@Filter(type = FilterType.REGEX, pattern = "com.appgoo.app.util.*"),
		@Filter(type = FilterType.REGEX, pattern = "com.appgoo.app.*.util.*") })
public class App extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(App.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class);
	}

}
