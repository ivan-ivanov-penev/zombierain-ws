package io.zombierain.ws.controller.service;

import com.google.common.cache.Cache;
import io.zombierain.ws.game.*;
import io.zombierain.ws.game.engine.Engine;
import io.zombierain.ws.game.states.GameState;
import io.zombierain.ws.game.states.InvalidGameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class GameService {

	private final String serverIsFullErrorMessage;

	private final Cache<String, GameInfo> idToGameInfo;

	private final IdGenerator idGenerator;

	private final GameCreator gameCreator;

	private final Engine engine;

	@Autowired
	public GameService(
			@Value("${server.full.error.message}") String serverIsFullErrorMessage,
			Cache<String, GameInfo> idToGameInfo,
			IdGenerator idGenerator,
			GameCreator gameCreator,
			Engine engine) {

		this.serverIsFullErrorMessage = serverIsFullErrorMessage;
		this.idToGameInfo = idToGameInfo;
		this.idGenerator = idGenerator;
		this.gameCreator = gameCreator;
		this.engine = engine;
	}

	public long getNumberOfActiveGames() {

		return idToGameInfo.size();
	}

	public String hostNewGame() {

		idToGameInfo.cleanUp();

		String gameId = idGenerator.generateGameId();

		while (idToGameInfo.asMap().putIfAbsent(gameId, new GameInfo(gameId)) != null) {

			gameId = idGenerator.generateGameId();
		}

		return gameId;
	}

	public boolean joinGame(String gameId) {

		GameInfo gameInfo = idToGameInfo.getIfPresent(gameId);

		return gameInfo != null && gameInfo.joinPlayerIfNotJoined();
	}

	public GameState invokeGameStart(String gameId) {

		GameInfo gameInfo = idToGameInfo.getIfPresent(gameId);

		if (gameInfo != null && gameInfo.startGameIfNotStarted()) {

			GameState initialState = gameInfo.game.constructInitialState(); // avoids concurrent state generation

			if (engine.attemptToStartNewGame(gameInfo.game)) {

				return initialState;
			}
			else {

				return new InvalidGameState(serverIsFullErrorMessage);
			}
		}
		else {

			throw new RuntimeException("Attempted to start a game with invalid ID: " + gameId);
		}
	}

	public void movePlayer1Left(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player1.moveLeft());
	}

	public void movePlayer1Right(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player1.moveRight());
	}

	public void player1Stop(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player1.stop());
	}

	public void player1Jump(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player1.jump());
	}

	public void movePlayer2Left(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player2.moveLeft());
	}

	public void movePlayer2Right(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player2.moveRight());
	}

	public void player2Stop(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player2.stop());
	}

	public void player2Jump(String gameId) {

		applyIfGameIsPresent(gameId, game -> game.player2.jump());
	}

	private void applyIfGameIsPresent(String gameId, Consumer<Game> consumer) {

		GameInfo gameInfo = idToGameInfo.getIfPresent(gameId);

		if (gameInfo != null) {

			consumer.accept(gameInfo.game);
		}
	}

	public class GameInfo {

		private final Game game;

		private final OnetimeCondition secondPlayerJoined;

		private final OnetimeCondition gameStarted;

		private GameInfo(String gameId) {

			this(gameCreator.createNewGame(gameId), new OnetimeCondition(), new OnetimeCondition());
		}

		private GameInfo(Game game, OnetimeCondition secondPlayerJoined, OnetimeCondition gameStarted) {

			this.game = game;
			this.secondPlayerJoined = secondPlayerJoined;
			this.gameStarted = gameStarted;
		}

		public boolean joinPlayerIfNotJoined() {

			return secondPlayerJoined.attemptToFulfilOnce();
		}

		public boolean startGameIfNotStarted() {

			return gameStarted.attemptToFulfilOnce();
		}
	}
}
