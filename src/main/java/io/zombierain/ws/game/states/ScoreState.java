package io.zombierain.ws.game.states;

public class ScoreState {

	public final int player1EnemiesKilledCount;

	public final int player2EnemiesKilledCount;

	public ScoreState(int player1EnemiesKilledCount, int player2EnemiesKilledCount) {

		this.player1EnemiesKilledCount = player1EnemiesKilledCount;
		this.player2EnemiesKilledCount = player2EnemiesKilledCount;
	}
}
