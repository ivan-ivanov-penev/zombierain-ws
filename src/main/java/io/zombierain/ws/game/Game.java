package io.zombierain.ws.game;

import io.zombierain.ws.game.objects.Player;
import io.zombierain.ws.game.objects.Tile;
import io.zombierain.ws.game.states.GameState;
import io.zombierain.ws.game.states.ScoreState;
import io.zombierain.ws.game.states.TileState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

	public final String id;

	public final LocalDateTime timeCreated;

	private final List<Tile> tiles;

	public final Player player1;

	public final Player player2;

	private final EnemyGenerator enemyGenerator;

	private final UpdateService updateService;

	public Game(
			String id,
			LocalDateTime timeCreated,
			List<Tile> tiles,
			Player player1,
			Player player2,
			EnemyGenerator enemyGenerator,
			UpdateService updateService) {

		this.id = id;
		this.timeCreated = timeCreated;
		this.tiles = tiles;
		this.player1 = player1;
		this.player2 = player2;
		this.enemyGenerator = enemyGenerator;
		this.updateService = updateService;
	}

	public GameState constructInitialState() {

		List<TileState> tilePositions = new ArrayList<>();

		for (int i = 0; i < tiles.size(); i++) {

			tilePositions.add(tiles.get(i).constructState(i));
		}

		return new GameState(
				tilePositions, player1.constructState(), player2.constructState(), enemyGenerator.constructStates());
	}

	private GameState getCurrentState() {

		List<TileState> tilePositions = new ArrayList<>();

		for (int i = 0; i < tiles.size(); i++) {

			Tile tile = tiles.get(i);

			if (tile.isBouncing()) {

				tilePositions.add(tile.constructState(i));
			}
		}

		return new GameState(
				tilePositions, player1.constructState(), player2.constructState(), enemyGenerator.constructStates());
	}

	public boolean isOver() {

		return (player1.hasNoMoreLives() && player2.hasNoMoreLives()) || enemyGenerator.zombiesExceedLimit();
	}

	public GameState update() {

		updateService.update(enemyGenerator.update());

		return getCurrentState();
	}

	public ScoreState generateScore() {

		return new ScoreState(player1.getEnemiesKilledCount(), player2.getEnemiesKilledCount());
	}
}
