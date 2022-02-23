package io.zombierain.ws.config.game.objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("game")
public class GameConfig {

	public final int width;

	public final int height;

	public final int fps;

	public final int gravity;

	public GameConfig(int width, int height, int fps, int gravity) {

		this.width = width;
		this.height = height;
		this.fps = fps;
		this.gravity = gravity;
	}

	@Override
	public String toString() {

		return "GameConfig{" +
				"width=" + width +
				", height=" + height +
				", fps=" + fps +
				", gravity=" + gravity +
				'}';
	}
}
