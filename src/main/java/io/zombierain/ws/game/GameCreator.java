package io.zombierain.ws.game;

import io.zombierain.ws.config.game.objects.EnemyConfig;
import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.config.game.objects.PlayerConfig;
import io.zombierain.ws.config.game.objects.TileConfig;
import io.zombierain.ws.game.objects.Player;
import io.zombierain.ws.game.objects.Tile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameCreator {

	private final GameConfig gameConfig;

	private final TileConfig tileConfig;

	private final PlayerConfig playerConfig;

	private final EnemyConfig enemyConfig;

	@Autowired
	public GameCreator(GameConfig gameConfig, TileConfig tileConfig, PlayerConfig playerConfig, EnemyConfig enemyConfig) {

		this.gameConfig = gameConfig;
		this.tileConfig = tileConfig;
		this.playerConfig = playerConfig;
		this.enemyConfig = enemyConfig;
	}

	public Game createNewGame(String gameId) {

		List<Tile> tiles = createTiles();

		float playerY = gameConfig.height - 2 * tileConfig.height - playerConfig.height / 2f;
		float player1X = 8.5f * tileConfig.width;
		float player2X = gameConfig.width - player1X;

		List<Tile> tilesWithoutZeroRow = tiles.subList((gameConfig.width / tileConfig.width), tiles.size());

		Player player1 = new Player(playerConfig, gameConfig, player1X, playerY, -playerConfig.width);
		Player player2 = new Player(playerConfig, gameConfig, player2X, playerY, playerConfig.width);

		EnemyGenerator enemyGenerator = new EnemyGenerator(enemyConfig, gameConfig);

		return new Game(
				gameId,
				LocalDateTime.now(),
				tiles,
				player1,
				player2,
				enemyGenerator,
				new UpdateService(player1, player2, tilesWithoutZeroRow));
	}

	// TODO refactor and make smarter
	private List<Tile> createTiles() {

		List<Tile> tiles = new ArrayList<>();

		int tileLength = tileConfig.width;
		float halfTileLength = tileLength / 2f;

		int gameWidth = gameConfig.width;
		int gameHeight = gameConfig.height;

		// ground floor - first row
		for (int i = 0; i < (gameWidth / tileLength); i++) {

			float x = i * tileLength + halfTileLength;
			float groundFloorY = gameHeight - halfTileLength;

			tiles.add(new Tile(tileConfig, gameConfig, x, groundFloorY));
		}

		// ground floor - second row
		for (int i = 0; i < (gameWidth / tileLength); i++) {

			float x = i * tileLength + halfTileLength;
			float groundFloorY = gameHeight - halfTileLength;

			tiles.add(new Tile(tileConfig, gameConfig, x, groundFloorY - tileLength));
		}

		// first floor - left
		for (int i = 0; i < 12; i++) {

			float x = i * tileLength + halfTileLength;
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 7f * tileLength));
		}

		// first floor - right
		for (int i = 0; i < 12; i++) {

			float x = gameWidth - (i * tileLength + halfTileLength);
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 7f * tileLength));
		}

		// second floor - left
		for (int i = 0; i < 4; i++) {

			float x = i * tileLength + halfTileLength;
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 11.5f * tileLength));
		}

		// second floor - right
		for (int i = 0; i < 4; i++) {

			float x = gameWidth - (i * tileLength + halfTileLength);
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 11.5f * tileLength));
		}
		
		// third floor - middle
		for (int i = 0; i < 16; i++) {

			float x = ((gameWidth - 16 * tileLength) / 2f) + (i * tileLength + halfTileLength);
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 12.5f * tileLength));
		}

		// fourth floor - left
		for (int i = 0; i < 14; i++) {

			float x = i * tileLength + halfTileLength;
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 18f * tileLength));
		}

		// fourth floor - right
		for (int i = 0; i < 14; i++) {

			float x = gameWidth - (i * tileLength + halfTileLength);
			tiles.add(new Tile(tileConfig, gameConfig, x, gameHeight - 18f * tileLength));
		}

		return tiles;
	}
}
