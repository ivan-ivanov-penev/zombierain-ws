package io.zombierain.ws.game.objects.common;

import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.config.game.objects.MovingGobjectConfig;
import io.zombierain.ws.game.objects.Enemy;
import io.zombierain.ws.game.objects.Player;
import io.zombierain.ws.game.objects.Tile;

public abstract class MovingGobject extends Gobject {

	protected final int gameWidth;

	protected final int gameHeight;

	protected final int fps;

	protected final int baseVelocityX;

	protected final int baseVelocityY;

	protected final int bouncingTileVelocityY;

	protected float velocityX;

	protected float velocityY;

	protected float previousX;

	protected float previousY;

	protected boolean dead;

	public MovingGobject(MovingGobjectConfig gobjectConfig, GameConfig gameConfig, float x, float y) {

		super(gobjectConfig, x, y);
		this.baseVelocityX = gobjectConfig.baseVelocityX;
		this.baseVelocityY = gobjectConfig.baseVelocityY;
		this.bouncingTileVelocityY = gobjectConfig.bouncingTileVelocityY;
		this.gameWidth = gameConfig.width;
		this.gameHeight = gameConfig.height;
		this.fps = gameConfig.fps;
		this.previousX = x;
		this.previousY = y;
		this.dead = false;
	}

	public boolean isDead() {

		return dead;
	}

	public boolean movedLeft() {

		return previousX > x;
	}

	public boolean movedRight() {

		return previousX < x;
	}

	public boolean ascended() {

		return previousY > y;
	}

	public boolean descended() {

		return previousY < y;
	}

	public void moveHorizontally() {

		previousX = x;

		x += velocityX / fps;

		enforceHorizontalWorldBoundaries();
	}

	protected void enforceHorizontalWorldBoundaries() {

		if (calculateLeftX() > gameWidth - 2) {

			onRightOutOfWorldBoundary();
		}
		else if (calculateRightX() < 2) {

			onLeftOutOfWorldBoundary();
		}
	}

	protected void onRightOutOfWorldBoundary() {

		x = -halfWidth + 2;

		previousX = x - 1;
	}

	protected void onLeftOutOfWorldBoundary() {

		x = gameWidth + halfWidth - 2;

		previousX = x + 1;
	}

	protected void moveVertically() {

		previousY = y;

		y += velocityY / fps;
	}

	public void onHorizontalTileCollision(Tile tile) {

		if (!tile.isBouncing()) { // should be handled by the vertical update otherwise movement trajectory become chaotic

			if (movedRight()) {

				placeLeftOf(tile);
			}
			else {

				placeRightOf(tile);
			}
		}
	}

	protected void onBouncingBottomTile(Tile tile) {

		velocityY = bouncingTileVelocityY;
	}

	public void onVerticalTileCollision(Tile tile) {

		velocityY = 0;

		if (ascended()) {

			placeUnder(tile);
		}
		else {

			onBottomTileCollision(tile);
		}
	}

	protected void onBottomTileCollision(Tile tile) {

		placeOver(tile);

		if (tile.isBouncing()) {

			onBouncingBottomTile(tile);
		}
	}

	public void onHorizontalCollision(Gobject object) {

		if (object instanceof Enemy) {

			onHorizontalEnemyCollision((Enemy) object);
		}
		else if (object instanceof Player) {

			onHorizontalPlayerCollision((Player) object);
		}
	}

	protected void onHorizontalEnemyCollision(Enemy enemy) {

		handleHorizontalCollision(enemy);
	}

	protected void onHorizontalPlayerCollision(Player player) {

		handleHorizontalCollision(player);
	}

	protected void handleHorizontalCollision(Gobject object) {

		if (movedRight()) {

			placeLeftOf(object);
		}
		else {

			placeRightOf(object);
		}
	}

	public void alertForHorizontalCollision(Gobject object) {

		if (object instanceof Enemy) {

			alertForHorizontalEnemyCollision((Enemy) object);
		}
		else if (object instanceof Player) {

			alertForHorizontalPlayerCollision((Player) object);
		}
	}

	protected void alertForHorizontalEnemyCollision(Enemy enemy) {}

	protected void alertForHorizontalPlayerCollision(Player player) {}

	public void onVerticalCollision(Gobject object) {

		if (object instanceof Enemy) {

			onVerticalEnemyCollision((Enemy) object);
		}
		else if (object instanceof Player) {

			onVerticalPlayerCollision((Player) object);
		}
	}

	protected void onVerticalEnemyCollision(Enemy enemy) {

		handleVerticalCollision(enemy);
	}

	protected void onVerticalPlayerCollision(Player player) {

		handleVerticalCollision(player);
	}

	protected void handleVerticalCollision(Gobject object) {

		if (descended()) {

			placeOver(object);
		}
		else {

			placeUnder(object);
		}
	}

	public void alertForVerticalCollision(Gobject object) {

		if (object instanceof Enemy) {

			alertForVerticalEnemyCollision((Enemy) object);
		}
		else if (object instanceof Player) {

			alertForVerticalPlayerCollision((Player) object);
		}
	}

	protected void alertForVerticalEnemyCollision(Enemy enemy) {}

	protected void alertForVerticalPlayerCollision(Player player) {}
}
