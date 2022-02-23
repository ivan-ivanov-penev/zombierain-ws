package io.zombierain.ws.config.game.objects;

public class MovingGobjectConfig extends GobjectConfig {

	public final int baseVelocityX;

	public final int baseVelocityY;

	public final int bouncingTileVelocityY;

	public MovingGobjectConfig(int width, int height, int baseVelocityX, int baseVelocityY, int bouncingTileVelocityY) {

		super(width, height);
		this.baseVelocityX = baseVelocityX;
		this.baseVelocityY = baseVelocityY;
		this.bouncingTileVelocityY = bouncingTileVelocityY;
	}

	@Override
	public String toString() {

		return "MovingGobjectConfig{" +
				"width=" + width +
				", height=" + height +
				", baseVelocityX=" + baseVelocityX +
				", baseVelocityY=" + baseVelocityY +
				", bouncingTileVelocityY=" + bouncingTileVelocityY +
				'}';
	}
}
