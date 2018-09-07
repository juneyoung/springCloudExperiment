package org.owls.cloud;

import ch.qos.logback.classic.Logger;
import org.owls.cloud.message.RabbitMessageReceiver;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// RestController 는 기본적으로 @ResponseBody 를 활성화한다
@RestController
@RequestMapping("/rabbit")
public class PublisherController {

    private static Logger logger =
        (Logger) LoggerFactory.getLogger(PublisherController.class);

    final static String topicExchangeName = "exp-topic-exchange";
    final static String messageQueueName = "experiment";

    @Autowired
    RabbitMessagingTemplate template;

    @Autowired
    RabbitMessageReceiver receiver;

    @RequestMapping(value = {"/publish"}, produces = { "application/json" })
    public Map publishMessage(HttpServletRequest request){

        Map<String, Object> response = new HashMap<>();
        String result = "success", error = "";
        try {
            template.convertAndSend(topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
            receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            result = "error";
            error = e.getMessage();
        }
        response.put("result", result);
        response.put("error", error);

        return response;
    }

}
