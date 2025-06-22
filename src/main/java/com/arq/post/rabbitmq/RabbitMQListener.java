package com.arq.post.rabbitmq;

import com.arq.post.api.model.PostTextProcessorResult;
import com.arq.post.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.arq.post.rabbitmq.RabbitMQConfig.QUEUE_TEXT_PROCESSOR_RESULT;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final PostService postService;

    @RabbitListener(queues = QUEUE_TEXT_PROCESSOR_RESULT, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessingTemperature(@Payload PostTextProcessorResult postTextProcessorResult) {
        postService.savedResultTextProcessor(postTextProcessorResult);
    }

}