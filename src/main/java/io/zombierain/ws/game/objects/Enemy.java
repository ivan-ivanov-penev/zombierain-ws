package io.zombierain.ws.game.objects;

import io.zombierain.ws.config.game.objects.EnemyConfig;
import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.game.objects.common.Gobject;
import io.zombierain.ws.game.objects.common.GravitationalGobject;
import io.zombierain.ws.game.states.EnemyState;
import io.zombierain.ws.game.objects.common.Timer;

import java.util.Random;

public class Enemy extends GravitationalGobject {

	private static final Random RANDOM = new Random();

	public final int id;

	private boolean hit;

	private boolean aggressive;

	private boolean goingToReviveSoon;

	private final Timer reviveTimer;

	public Enemy(EnemyConfig enemyConfig, GameConfig gameConfig, float x, float y, int id) {

		super(enemyConfig, gameConfig, x, y);
		this.id = id;
		this.velocityX = enemyConfig.baseVelocityX * (x < 500 ? 1 : -1);
		this.hit = false;
		this.aggressive = false;
		this.goingToReviveSoon = false;
		this.reviveTimer = new Timer(gameConfig.fps, 6);
	}

	public boolean isToBeRemoved() {

		return calculateTopY() >= gameHeight;
	}

	public boolean isHit() {

		return hit;
	}

	public EnemyState constructState() {

		return new EnemyState(x, y, id, velocityX, hit, dead, aggressive, goingToReviveSoon);
	}

	@Override
	public void moveHorizontally() {

		if (hit && reviveTimer.tickAndCheckIfElapsed()) {

			aggressive = true;

			revive();
		}

		goingToReviveSoon = !dead && reviveTimer.willElapseSoon();

		super.moveHorizontally();
	}

	@Override
	public void moveVertically() {

		if (calculateTopY() < gameHeight) {

			super.moveVertically();
		}
	}

	private void revive() {

		hit = false;

		velocityX = baseVelocityX * (RANDOM.nextBoolean() ? 1 : -1) * (aggressive ? 1.5f : 1f);

		reviveTimer.reset();
	}

	@Override
	protected void onLeftOutOfWorldBoundary() {

		super.onLeftOutOfWorldBoundary();

		attemptTeleportToTopTile();
	}

	@Override
	protected void onRightOutOfWorldBoundary() {

		super.onRightOutOfWorldBoundary();

		attemptTeleportToTopTile();
	}

	private void attemptTeleportToTopTile() {

		if (!dead && y > gameHeight * 0.75) {

			y = height * 2 + halfHeight;

			velocityX = -velocityX;
		}
	}

	@Override
	protected void onBottomTileCollision(Tile tile) {

		super.onBottomTileCollision(tile);

		if (hit && !tile.isBouncing()) {

			velocityX = 0;
		}
	}

	@Override
	protected void onBouncingBottomTile(Tile tile) {

		if (!hit) {

			hit = true;

			super.onBouncingBottomTile(tile);

			velocityX = velocityX * (isLeftOf(tile) ? -1 : 1);
		}
		else if (velocityX == 0) {

			revive();

			super.onBouncingBottomTile(tile);

			y -= 15; // this fixes an issue of reviving and then immediately dying again (so not reviving at all)
		}
	}

	@Override
	public void onHorizontalCollision(Gobject object) {

		super.handleHorizontalCollision(object);

		velocityX = -velocityX;

		enforceHorizontalWorldBoundaries();
	}

	@Override
	protected void onHorizontalPlayerCollision(Player player) {

		attemptToGoToHeaven(player);
	}

	@Override
	protected void alertForHorizontalPlayerCollision(Player player) {

		velocityX = -velocityX;

		attemptToGoToHeaven(player);
	}

	@Override
	protected void onVerticalEnemyCollision(Enemy object) {

		velocityY = bouncingTileVelocityY;
	}

	@Override
	protected void onVerticalPlayerCollision(Player player) {

		attemptToGoToHeaven(player);
	}

	@Override
	protected void alertForVerticalPlayerCollision(Player player) {

		velocityX = -velocityX;

		attemptToGoToHeaven(player);
	}

	private void attemptToGoToHeaven(Player player) {

		if (hit && !dead) {

			dead = true;

			velocityX = isLeftOf(player) ? - baseVelocityX : baseVelocityX;

			velocityY = baseVelocityY;
		}
	}
}
