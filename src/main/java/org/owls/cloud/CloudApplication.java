package org.owls.cloud;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
* 그냥 실행하면 오류발생함
* https://docs.gradle.org/current/userguide/userguide_single.html#sec:accessing_the_web_via_a_proxy
*
* Gradle project sync failed. Basic functionality (e.g. editing, debugging) will not work properly => 왜 안드로이드 SDK 를 찾고 그러지...
*
* EnableDiscoveryClient ???
* */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudApplication {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(CloudApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CloudApplication.class, args);
	}
}
