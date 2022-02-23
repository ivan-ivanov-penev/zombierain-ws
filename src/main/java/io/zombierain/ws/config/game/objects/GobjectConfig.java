package io.zombierain.ws.config.game.objects;

public class GobjectConfig {

	public final int width;

	public final int height;

	public GobjectConfig(int width, int height) {

		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {

		return "GobjectConfig{" +
				"width=" + width +
				", height=" + height +
				'}';
	}
}
