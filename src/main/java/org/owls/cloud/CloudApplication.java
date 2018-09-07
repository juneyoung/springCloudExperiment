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
*
* Cloud Stream 이 AMQP 의 편의를 제공함
* RabbitMQ 와 kafka 중 택 1
* kafka 는 끊임없이 들어오는 스트리밍에 적합
* rabbitMQ 는 다양하고 복잡란 메세지 라우팅을 지원(Exchange)
*
* 일단은 머신에 RabbitMQ 가 있으니 RabbitMQ 를  사용해 테스트를 진행한다
* https://zuminternet.github.io/spring_cloud_stream_rabbitmq_02/
* 스프링에서 제공하는 라이브러리를 사용하지 않는다면 MQClient 를 사용하여
* 팩토리 -> 커넥션 -> 채널 -> 익스체인지선언->큐 선언 -> 큐 바인딩으로 진행해야 한다
* */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudApplication {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(CloudApplication.class);
	public static void main(String[] args) {
		logger.info("Running CloudApplication Experiment");
		SpringApplication.run(CloudApplication.class, args);
	}
}
