package io.zombierain.ws.game.states;

import io.zombierain.ws.game.states.EnemyState;
import io.zombierain.ws.game.states.PlayerState;
import io.zombierain.ws.game.states.TileState;

import java.util.List;

public class GameState {

	public final List<TileState> tiles;

	public final PlayerState player1;

	public final PlayerState player2;

	public final List<EnemyState> enemies;

	public GameState(List<TileState> tiles, PlayerState player1, PlayerState player2, List<EnemyState> enemies) {

		this.tiles = tiles;
		this.player1 = player1;
		this.player2 = player2;
		this.enemies = enemies;
	}
}
