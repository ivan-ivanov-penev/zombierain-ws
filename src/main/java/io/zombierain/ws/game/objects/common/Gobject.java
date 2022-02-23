package io.zombierain.ws.game.objects.common;

import io.zombierain.ws.config.game.objects.GobjectConfig;
import io.zombierain.ws.game.states.TileState;

public abstract class Gobject {

	protected final int width;

	protected final int height;

	protected final float halfWidth;

	protected final float halfHeight;

	protected float x;

	protected float y;

	public Gobject(GobjectConfig gobjectConfig, float x, float y) {

		this.width = gobjectConfig.width;
		this.height = gobjectConfig.height;
		this.halfWidth = this.width / 2f;
		this.halfHeight = this.height / 2f;
		this.x = x;
		this.y = y;
	}

	public float calculateLeftX() {

		return x - halfWidth;
	}

	public float calculateRightX() {

		return x + halfWidth;
	}

	public float calculateTopY() {

		return y - halfHeight;
	}

	public float calculateBottomY() {

		return y + halfHeight;
	}

	public boolean collides(Gobject object) {

		return isBetweenLeftAndRight(object) && isBetweenTopAndBottom(object);
	}

	public boolean isBetweenLeftAndRight(Gobject object) {

		return  x > object.calculateLeftX() - halfWidth && x < object.calculateRightX() + halfWidth;
	}

	public boolean isBetweenTopAndBottom(Gobject object) {

		return  y > object.calculateTopY() - halfHeight && y < object.calculateBottomY() + halfHeight;
	}

	public boolean xIsBetweenLeftAndRight(Gobject object) {

		return x >= object.calculateLeftX() && x < object.calculateRightX();
	}

	public boolean isOver(Gobject object) {

		return y < object.y;
	}

	public boolean isUnder(Gobject object) {

		return y > object.y;
	}

	public boolean isLeftOf(Gobject object) {

		return x < object.x;
	}

	public boolean isRightOf(Gobject object) {

		return x > object.x;
	}

	public void placeOver(Gobject object) {

		y = object.calculateTopY() - halfHeight;
	}

	public void placeUnder(Gobject object) {

		y = object.calculateBottomY() + halfHeight;
	}

	public void placeLeftOf(Gobject object) {

		x = object.calculateLeftX() - halfWidth;
	}

	public void placeRightOf(Gobject object) {

		x = object.calculateRightX() + halfWidth;
	}
}
