package com.arq.post.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {


    public static final String QUEUE_TEXT_PROCESSOR = "text-processor-service.post-processing.v1.q";
    public static final String EXCHANGE_TEXT_PROCESSOR = "text-processor-service.post-processing.v1.e";
    public static final String DEAD_LETTER_QUEUE_TEXT_PROCESSOR = "text-processor-service.post-processing.v1.dlq";
    public static final String QUEUE_TEXT_PROCESSOR_RESULT = "post-service.post-processing-result.v1.q";
    public static final String EXCHANGE_TEXT_PROCESSOR_RESULT = "text-processor-service.post-processing-result.v1.e";
    public static final String DEAD_LETTER_QUEUE_TEXT_PROCESSOR_RESULT = "text-processor-service.post-processing-result.v1.dlq";


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queueTextProcessor() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_TEXT_PROCESSOR);
        return QueueBuilder.durable(QUEUE_TEXT_PROCESSOR).withArguments(args).build();
    }

    @Bean
    public FanoutExchange exchangeTextProcessor() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_TEXT_PROCESSOR).build();
    }

    @Bean
    public Queue deadLetterQueueTextProcessor() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_TEXT_PROCESSOR).build();
    }

    @Bean
    public Binding bindingTextProcessor() {
        return BindingBuilder.bind(queueTextProcessor()).to(exchangeTextProcessor());
    }


    @Bean
    public Queue queueTextProcessorResult() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_TEXT_PROCESSOR_RESULT);
        return QueueBuilder.durable(QUEUE_TEXT_PROCESSOR_RESULT).build();
    }

    @Bean
    public Queue deadLetterQueueTextProcessorResult() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_TEXT_PROCESSOR_RESULT).build();
    }

    @Bean
    public FanoutExchange exchangeTextProcessorResult() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_TEXT_PROCESSOR_RESULT).build();
    }


    @Bean
    public Binding bindingTextProcessorResult() {
        return BindingBuilder.bind(queueTextProcessorResult()).to(exchangeTextProcessorResult());
    }

}