package io.zombierain.ws.config.game.objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("enemy")
public class EnemyConfig extends MovingGobjectConfig {

	public final int spawnIntervalSeconds;

	public final int spawnInitialDelaySeconds;

	public EnemyConfig(
			int width,
			int height,
			int baseVelocityX,
			int baseVelocityY,
			int bouncingTileVelocityY,
			int spawnIntervalSeconds,
			int spawnInitialDelaySeconds) {

		super(width, height, baseVelocityX, baseVelocityY, bouncingTileVelocityY);
		this.spawnIntervalSeconds = spawnIntervalSeconds;
		this.spawnInitialDelaySeconds = spawnInitialDelaySeconds;
	}
}
