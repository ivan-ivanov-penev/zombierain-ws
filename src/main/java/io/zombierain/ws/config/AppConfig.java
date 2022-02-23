package io.zombierain.ws.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.controller.service.GameService.GameInfo;
import io.zombierain.ws.game.engine.EngineWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer(
			@Value("${cors.path.pattern}") String pathPattern,
			@Value("${cors.allowed.origins:}") List<String> allowedOrigins,
			@Value("${cors.allowed.methods:}") List<String> allowedMethods,
			@Value("${cors.allowed.headers:}") List<String> allowedHeaders,
			@Value("${cors.allow.credentials:false}") boolean allowCredentials) {

		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				CorsRegistration corsRegistration = registry.addMapping(pathPattern).allowCredentials(allowCredentials);

				allowedOrigins.forEach(corsRegistration::allowedOrigins);
				allowedMethods.forEach(corsRegistration::allowedMethods);
				allowedHeaders.forEach(corsRegistration::allowedHeaders);
			}
		};
	}

	@Bean(name = "idToGameInfo")
	public Cache<String, GameInfo> createIdToGameInfoCache(
			@Value("${game.idle.expiration.time.minutes}") int expirationTimeMinutes) {

		return CacheBuilder.newBuilder().expireAfterAccess(expirationTimeMinutes, TimeUnit.MINUTES).build();
	}

	@Bean(name = "workers")
	public List<EngineWorker> createEngineWorkers(
			@Value("${engine.workers.count:0}") int engineWorkersCount,
			@Value("${max.games.per.worker}") int maxNumberOfGamesPerWorker,
			SimpMessagingTemplate wsClient,
			GameConfig config) {

		int workersCount = engineWorkersCount > 0 ? engineWorkersCount : Runtime.getRuntime().availableProcessors() * 2;

		List<EngineWorker> workers = new ArrayList<>(workersCount);

		for (int i = 0; i < workersCount; i++) {

			EngineWorker worker = new EngineWorker(1000 / config.fps, maxNumberOfGamesPerWorker, wsClient);
			worker.setDaemon(true);
			worker.setName("Worker_" + i);

			workers.add(worker);
		}

		return workers;
	}
}
