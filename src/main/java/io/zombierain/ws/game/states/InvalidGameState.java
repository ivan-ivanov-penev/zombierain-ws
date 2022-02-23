package io.zombierain.ws.game.states;

import java.util.List;

public class InvalidGameState extends GameState {

	public final String message;

	public InvalidGameState(String message) {

		this(null, null, null, null, message);
	}

	public InvalidGameState(
			List<TileState> tiles, PlayerState player1, PlayerState player2, List<EnemyState> enemies, String message) {

		super(tiles, player1, player2, enemies);
		this.message = message;
	}
}
