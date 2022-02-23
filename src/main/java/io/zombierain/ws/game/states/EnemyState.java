package io.zombierain.ws.game.states;

public class EnemyState extends Position {

	public final int id;

	public final float velocityX;

	public final boolean hit;

	public final boolean dead;

	public final boolean aggressive;

	public final boolean goingToReviveSoon;

	public EnemyState(
			float x,
			float y,
			int id,
			float velocityX,
			boolean hit,
			boolean dead,
			boolean aggressive,
			boolean goingToReviveSoon) {

		super(x, y);
		this.id = id;
		this.velocityX = velocityX;
		this.hit = hit;
		this.dead = dead;
		this.aggressive = aggressive;
		this.goingToReviveSoon = goingToReviveSoon;
	}
}
