package io.zombierain.ws.game.objects;

import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.config.game.objects.TileConfig;
import io.zombierain.ws.game.objects.common.MovingGobject;
import io.zombierain.ws.game.states.TileState;

public class Tile extends MovingGobject {

	private final float initialY;

	public Tile(TileConfig tileConfig, GameConfig gameConfig, float x, float y) {

		super(tileConfig, gameConfig, x, y);
		this.initialY = y;
	}

	public TileState constructState(int index) {

		return new TileState(x, y, index);
	}

	public void triggerBounce() { // or startBounce, startSingleBounce

		velocityY = -baseVelocityY;
	}

	public boolean isBouncing() {

		return velocityY != 0;
	}

	public void update() {

		if (isBouncing()) {

			super.moveVertically();

			if (y < initialY - halfWidth) {

				y = initialY - halfWidth;

				velocityY = baseVelocityY;
			}
			else if (y > initialY) {

				y = initialY;

				velocityY = 0;
			}
		}
	}
}
