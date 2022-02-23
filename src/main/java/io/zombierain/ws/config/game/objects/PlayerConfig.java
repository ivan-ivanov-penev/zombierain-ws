package io.zombierain.ws.config.game.objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("player")
public class PlayerConfig extends MovingGobjectConfig {

	public final int gracefulFallVelocityY;

	public final int lives;

	public PlayerConfig(
			int width,
			int height,
			int baseVelocityX,
			int baseVelocityY,
			int bouncingTileVelocityY,
			int gracefulFallVelocityY,
			int lives) {

		super(width, height, baseVelocityX, baseVelocityY, bouncingTileVelocityY);
		this.gracefulFallVelocityY = gracefulFallVelocityY;
		this.lives = lives;
	}
}
