package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import datadog.opentracing.DDTracer;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@SpringBootApplication
public class Application {

	static final String topicExchangeName = "spring-boot-exchange";
	static final String queueName = "spring-boot";

//	@Bean
//	DDTracer initTracer(@Value("springrabbitmqprod") String service) {
//		DDTracer tracer = DDTracer.builder().build();
//		GlobalTracer.register(tracer);
//		// register the same tracer with the Datadog API
//		datadog.trace.api.GlobalTracer.registerIfAbsent(tracer);
//		return tracer;
//	}

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplateBuilder().build();
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
        //DDTracer tracer = DDTracer.builder().build();
		
		SpringApplication.run(Application.class, args);
	}
}
