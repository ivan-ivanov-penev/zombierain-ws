package io.zombierain.ws.game;

import com.google.common.collect.Lists;
import io.zombierain.ws.game.objects.Enemy;
import io.zombierain.ws.game.objects.common.GravitationalGobject;
import io.zombierain.ws.game.objects.Player;
import io.zombierain.ws.game.objects.Tile;

import java.util.List;

public class UpdateService {

	private final Player player1;

	private final Player player2;

	private final List<Tile> tiles;

	public UpdateService(Player player1, Player player2, List<Tile> tiles) {

		this.player1 = player1;
		this.player2 = player2;
		this.tiles = tiles;
	}

	public void update(List<Enemy> enemies) {

		tiles.forEach(Tile::update);

		List<GravitationalGobject> allGameObjects = Lists.newArrayList(player1, player2);
		allGameObjects.addAll(enemies);

		for (GravitationalGobject object : allGameObjects) {

			processHorizontalMovement(object, allGameObjects);

			processVerticalMovement(object, allGameObjects);
		}
	}

	private void processHorizontalMovement(GravitationalGobject object, List<GravitationalGobject> allGameObjects) {

		object.moveHorizontally();

		if (!object.isDead()) {

			checkForHorizontalTileCollision(object);

			checkForHorizontalGobjectCollision(object, allGameObjects);
		}
	}

	private void checkForHorizontalTileCollision(GravitationalGobject object) {

		for (Tile tile : tiles) {

			if (object.collides(tile)) {

				object.onHorizontalTileCollision(tile);

				break; // due to the nature of the map a gobject can collide horizontally only with a single tile
			}
		}
	}

	private void checkForHorizontalGobjectCollision(
			GravitationalGobject object, List<GravitationalGobject> allGameObjects) {

		for (GravitationalGobject potentialCollision : allGameObjects) {

			if (object != potentialCollision && object.collides(potentialCollision) && !potentialCollision.isDead()) {

				object.onHorizontalCollision(potentialCollision);

				potentialCollision.alertForHorizontalCollision(object);
			}
		}
	}

	public void processVerticalMovement(GravitationalGobject object, List<GravitationalGobject> allGameObjects) {

		object.moveVertically();

		if (!object.isDead()) {

			checkForVerticalTileCollision(object);

			checkForVerticalGobjectCollision(object, allGameObjects);
		}
	}

	private void checkForVerticalTileCollision(GravitationalGobject object) {

		for (Tile tile : tiles) {

			if (object.collides(tile)) {

				object.onVerticalTileCollision(tile);
			}

			if (object.calculateTopY() == tile.calculateBottomY() && object.xIsBetweenLeftAndRight(tile)) {

				tile.triggerBounce();
			}
		}
	}

	private void checkForVerticalGobjectCollision(
			GravitationalGobject object, List<GravitationalGobject> allGameObjects) {

		for (GravitationalGobject potentialCollision : allGameObjects) {

			if (object != potentialCollision && object.collides(potentialCollision) && !potentialCollision.isDead()) {

				object.onVerticalCollision(potentialCollision);

				potentialCollision.alertForVerticalCollision(object);
			}
		}
	}
}
