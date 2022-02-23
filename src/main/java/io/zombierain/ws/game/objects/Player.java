package io.zombierain.ws.game.objects;

import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.config.game.objects.PlayerConfig;
import io.zombierain.ws.game.objects.common.GravitationalGobject;
import io.zombierain.ws.game.states.PlayerState;
import io.zombierain.ws.game.objects.common.Timer;
import io.zombierain.ws.game.objects.common.Timer.ElapsedTimer;

public class Player extends GravitationalGobject {

	private final Timer squashedByOtherPlayerTimer;

	private final int spawnOffset;

	public final int gracefulFallVelocityY;

	private int lives;

	private int enemiesKilledCount;

	private float velocityXDivider;

	private boolean isStandingOnTile;

	private boolean fallingGracefully;

	private volatile float volatileVelocityX;

	private volatile boolean jumpActionRequired;

	public Player(PlayerConfig playerConfig, GameConfig gameConfig, float x, float y, int spawnOffset) {

		super(playerConfig, gameConfig, x, y);
		this.squashedByOtherPlayerTimer = new ElapsedTimer(gameConfig.fps, 1);
		this.gracefulFallVelocityY = playerConfig.gracefulFallVelocityY;
		this.spawnOffset = spawnOffset;
		this.lives = playerConfig.lives;
		this.isStandingOnTile = false;
		this.fallingGracefully = false;
		this.jumpActionRequired = false;
		this.volatileVelocityX = 0f;
		this.velocityXDivider = 1f;
		this.enemiesKilledCount = 0;
	}

	public void moveLeft() {

		volatileVelocityX = -baseVelocityX;
	}

	public void moveRight() {

		volatileVelocityX = baseVelocityX;
	}

	public void stop() {

		volatileVelocityX = 0;
	}

	public void jump() {

		jumpActionRequired = true;
	}

	public PlayerState constructState() {

		return new PlayerState(
				x, y, volatileVelocityX, velocityY, !isStandingOnTile, lives, dead, !squashedByOtherPlayerTimer.hasElapsed());
	}

	public int getEnemiesKilledCount() {

		return enemiesKilledCount;
	}

	public boolean hasNoMoreLives() {

		return lives < 0;
	}

	@Override
	public void moveHorizontally() {

		squashedByOtherPlayerTimer.tickAndCheckIfElapsed();

		if (isEligibleToMove()) {

			velocityX = volatileVelocityX / velocityXDivider;

			velocityXDivider = 1f;
		}
		else {

			velocityX = 0;
		}

		super.moveHorizontally();
	}

	private boolean isEligibleToMove() {

		return !dead && squashedByOtherPlayerTimer.hasElapsed();
	}

	@Override
	public void moveVertically() {

		if (jumpActionRequired && isStandingOnTile && isEligibleToMove()) {

			velocityY = baseVelocityY;
		}

		jumpActionRequired = false;

		isStandingOnTile = false;

		super.moveVertically();

		attemptToResurrectIfInHeaven();
	}

	private void attemptToResurrectIfInHeaven() {

		if (calculateTopY() > gameHeight * 1.5f) {

			lives -= 1;

			if (hasNoMoreLives()) {

				velocityY = 0;

				y = gameHeight + height;
			}
			else {

				resurrect();
			}
		}
	}

	private void resurrect() {

		dead = false;

		x = gameWidth / 2f + spawnOffset;

		y = -height;

		fallingGracefully = true;

		squashedByOtherPlayerTimer.elapse();

		velocityY = gracefulFallVelocityY;
	}

	@Override
	protected void recalculateVelocityBasedOnGravity() {

		if (!fallingGracefully && !hasNoMoreLives()) {

			super.recalculateVelocityBasedOnGravity();
		}
	}

	@Override
	protected void onBottomTileCollision(Tile tile) {

		super.onBottomTileCollision(tile);

		isStandingOnTile = true;

		fallingGracefully = false;
	}

	@Override
	protected void onHorizontalEnemyCollision(Enemy enemy) {

		attemptToGoToHeaven(enemy);
	}

	private void attemptToGoToHeaven(Enemy enemy) {

		if (!enemy.isHit() && !enemy.isDead()) {

			dead = true;

			fallingGracefully = false;

			velocityY = baseVelocityY;
		}
		else {

			enemiesKilledCount++;
		}
	}

	@Override
	protected void onHorizontalPlayerCollision(Player otherPlayer) {

		velocityXDivider = 3f;

		if (movedRight()) {

			otherPlayer.placeRightOf(this);

			x -= 1; // a bug in which the players collide vertically - perhaps due to the float not rounding properly
		}
		else {

			otherPlayer.placeLeftOf(this);

			x += 1; // a bug in which the players collide vertically - perhaps due to the float not rounding properly
		}
	}

	@Override
	protected void alertForHorizontalEnemyCollision(Enemy enemy) {

		attemptToGoToHeaven(enemy);
	}

	@Override
	protected void onVerticalEnemyCollision(Enemy enemy) {

		attemptToGoToHeaven(enemy);
	}

	@Override
	protected void onVerticalPlayerCollision(Player player) {

		if (ascended()) {

			placeUnder(player);

			squashedByOtherPlayerTimer.reset();
		}
		else {

			placeOver(player);

			velocityY = baseVelocityY;

			fallingGracefully = false;

			player.squashedByOtherPlayerTimer.reset();
		}
	}

	@Override
	protected void alertForVerticalEnemyCollision(Enemy enemy) {

		attemptToGoToHeaven(enemy);
	}
}
