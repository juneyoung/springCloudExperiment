package org.owls.cloud.message;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class RabbitMessageReceiver {

    private static final Logger logger =
            (Logger) LoggerFactory.getLogger(RabbitMessageReceiver.class);

    // CountDownLatch 의 역할은 ??
    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch(){
        return this.latch;
    }

    public void receiveMessage(String message){
        logger.info("Received message :: {}", message);
        latch.countDown();
    }
}
