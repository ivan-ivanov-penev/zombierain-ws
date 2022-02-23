package io.zombierain.ws.game.objects.common;

import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.config.game.objects.MovingGobjectConfig;

public abstract class GravitationalGobject extends MovingGobject {

	protected final float gravity;

	public GravitationalGobject(MovingGobjectConfig gobjectConfig, GameConfig gameConfig, float x, float y) {

		super(gobjectConfig, gameConfig, x, y);
		this.gravity = gameConfig.gravity;
	}

	@Override
	public void moveVertically() {

		recalculateVelocityBasedOnGravity();

		super.moveVertically();
	}

	protected void recalculateVelocityBasedOnGravity() {

		float newVelocity = velocityY + (gravity / fps);

		velocityY = Math.min(newVelocity, gravity);
	}
}
