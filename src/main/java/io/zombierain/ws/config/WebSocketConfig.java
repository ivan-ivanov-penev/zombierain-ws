package io.zombierain.ws.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final String stompPathInput;

	private final String stompPathOutput;

	private final String outputAllowedOrigins;

	@Autowired
	public WebSocketConfig(
			@Value("${server.stomp.path.input}") String stompPathInput,
			@Value("${server.stomp.path.output}") String stompPathOutput,
			@Value("${server.stomp.output.allowed.origins}") String outputAllowedOrigins) {

		this.stompPathInput = stompPathInput;
		this.stompPathOutput = stompPathOutput;
		this.outputAllowedOrigins = outputAllowedOrigins;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.setApplicationDestinationPrefixes(stompPathInput);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint(stompPathOutput).setAllowedOrigins(outputAllowedOrigins);
	}
}
