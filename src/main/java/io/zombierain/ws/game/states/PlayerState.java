package io.zombierain.ws.game.states;

public class PlayerState extends Position {

	public final float velocityX;

	public final float velocityY;

	public final boolean isInAir;

	public final int lives;

	public final boolean dead;

	public final boolean squashed;

	public PlayerState(
			float x,
			float y,
			float velocityX,
			float velocityY,
			boolean isInAir,
			int lives,
			boolean dead,
			boolean squashed) {

		super(x, y);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.isInAir = isInAir;
		this.lives = lives;
		this.dead = dead;
		this.squashed = squashed;
	}
}
