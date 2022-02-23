package io.zombierain.ws.config.game.objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("tile")
public class TileConfig extends MovingGobjectConfig {

	public TileConfig(int width, int height, int baseVelocityX, int baseVelocityY, int bouncingTileVelocityY) {

		super(width, height, baseVelocityX, baseVelocityY, bouncingTileVelocityY);
	}
}
