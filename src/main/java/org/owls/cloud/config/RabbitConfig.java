package org.owls.cloud.config;

import org.owls.cloud.message.RabbitMessageReceiver;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;    // 이거 임포트 안해도 자동으로 빈 생김
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ch.qos.logback.classic.Logger;

//@EnableRabbit   //https://stackoverflow.com/questions/46922056/failed-to-start-bean-org-springframework-amqp-rabbit-config-internalrabbitliste
@Configuration
public class RabbitConfig {

    final static String topicExchangeName = "exp-topic-exchange";
    final static String messageQueueName = "experiment";

    private static final Logger configLogger =
            (Logger) LoggerFactory.getLogger(RabbitConfig.class);

    @Bean
    Queue queue() {
        // 두번째 인자는 durable 큐를 생성한다는 의미로 true 일 경우, 서버 시작시 큐가 지속된다(survive)
        return new Queue(messageQueueName, false);
    }

    /*
    * Direct / Topic / Fanout 개념
    * Direct 는 조건에 맞는 큐에만 전송
    * Fanout 은 바인딩이 되어 있는 모든 큐에 전송
    * Topic 은 설정에 따라 Direct 와 Fanout 방식으로 작동
    *
    * Exchange 는 발행자에서 생성한 메세지를 각 큐로 라우팅
    * */
    @Bean
    TopicExchange exchange () {
        return new TopicExchange(topicExchangeName);
    }

    /*
    * * 의미는 1단계 구조를 나타내고
    * # 은 모든 매칭을 검사
    * */
    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("foo.bar.#");
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter (RabbitMessageReceiver rabbitMessageReceiver) {
        // 파라미터 default Listen method
        return new MessageListenerAdapter(rabbitMessageReceiver, "receiveMessage");
    }

    @Bean
    SimpleMessageListenerContainer container
            (ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(messageQueueName);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }
}

