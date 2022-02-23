package io.zombierain.ws.game.states;

public abstract class Position {

	public final float x;

	public final float y;

	public Position(float x, float y) {

		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {

		return "Position{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
