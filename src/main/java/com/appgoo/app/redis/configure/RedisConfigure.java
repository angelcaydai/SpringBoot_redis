package com.appgoo.app.redis.configure;

import java.lang.reflect.Method;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Configuration
@EnableCaching
public class RedisConfigure {

	@Inject
	RedisProperties redisProperties;

	@Primary
	@Bean("masterJedisConnectionFactory")
	JedisConnectionFactory masterJedisConnectionFactory() {

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
		jedisConnectionFactory.setHostName(redisProperties.getHost());
		jedisConnectionFactory.setPort(redisProperties.getPort());

		return jedisConnectionFactory;
	}

	@Primary
	@Bean(name = "masterRedisTemplate")
	public RedisTemplate<String, Object> masterRedisTemplate(
			@Qualifier("masterJedisConnectionFactory") RedisConnectionFactory factory) {

		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(masterJedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new RedisObjectSerializer());
		return template;
	}

	@Primary
	@Bean("masterScacheManager")
	public CacheManager masterScacheManager(
			@Qualifier("masterRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		// rcm.setDefaultExpiration(60);// 秒
		return rcm;
	}

	// @Bean("salveJedisConnectionFactory")
	// JedisConnectionFactory salveJedisConnectionFactory() {
	// JedisConnectionFactory jedisConnectionFactory = new
	// JedisConnectionFactory();
	// jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
	// jedisConnectionFactory.setHostName(redisProperties.getHost());
	// jedisConnectionFactory.setPort(redisProperties.getPort());
	// return jedisConnectionFactory;
	// }
	//
	// @Bean(name = "salveRedisTemplate")
	// public RedisTemplate<String, Object> salveRedisTemplate(
	// @Qualifier("salveJedisConnectionFactory") RedisConnectionFactory factory)
	// {
	//
	// RedisTemplate<String, Object> template = new RedisTemplate<String,
	// Object>();
	// template.setConnectionFactory(salveJedisConnectionFactory());
	// template.setKeySerializer(new StringRedisSerializer());
	// template.setValueSerializer(new RedisObjectSerializer());
	// return template;
	// }
	//
	// @Bean("salveScacheManager")
	// public CacheManager salveScacheManager(
	// @Qualifier("salveRedisTemplate") RedisTemplate<String, Object>
	// redisTemplate) {
	// RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
	// // 设置缓存过期时间
	// // rcm.setDefaultExpiration(60);// 秒
	// return rcm;
	// }

	// 自定义key 生成策略
	@Bean
	public KeyGenerator wiselyKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	@Component
	@PropertySource("classpath:redis.properties")
	class RedisProperties {

		@Value("${spring.redis.database}")
		private int database;
		@Value("${spring.redis.host}")
		private String host;
		@Value("${spring.redis.port}")
		private int port;

		public int getDatabase() {
			return database;
		}

		public void setDatabase(int database) {
			this.database = database;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}
}
