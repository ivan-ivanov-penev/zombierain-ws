package io.zombierain.ws.game.engine;

import io.zombierain.ws.game.Game;
import io.zombierain.ws.game.states.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.ArrayBlockingQueue;

public class EngineWorker extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(EngineWorker.class);

	private final long millisBetweenFrames;

	private final ArrayBlockingQueue<Game> gamesToProcess;

	private final SimpMessagingTemplate wsClient;

	public EngineWorker(long millisBetweenFrames, int maxNumberOfGames, SimpMessagingTemplate wsClient) {

		this(millisBetweenFrames, new ArrayBlockingQueue<>(maxNumberOfGames), wsClient);
	}

	EngineWorker(long millisBetweenFrames, ArrayBlockingQueue<Game> gamesToProcess, SimpMessagingTemplate wsClient) {

		this.millisBetweenFrames = millisBetweenFrames;
		this.gamesToProcess = gamesToProcess;
		this.wsClient = wsClient;
	}

	public boolean accept(Game game) {

		boolean canProcessGame = gamesToProcess.offer(game);

		if (canProcessGame) {

			LOGGER.info("Starting a new game: {}", game.id);
		}
		else {

			LOGGER.warn("Unable to accept game '{}' - queue is full", game.id);
		}

		return canProcessGame;
	}

	@Override
	public void run() {

		while (true) {

			long frameStartTime = System.currentTimeMillis();

			gamesToProcess.forEach(this::updateGame);
			gamesToProcess.removeIf(Game::isOver);

			sleepUntilNextFrame(frameStartTime);
		}
	}

	private void updateGame(Game game) {

		try {

			GameState gameState = game.update();

			if (!game.isOver()) {

				wsClient.convertAndSend("/" + game.id + "/update", gameState);
			}
			else {

				LOGGER.info("Game Over: {}", game.id);

				wsClient.convertAndSend("/" + game.id + "/game-over", game.generateScore());
			}
		}
		catch (Exception e) {

			LOGGER.error("Error updating game: {}", game.id, e);
		}
	}

	private void sleepUntilNextFrame(long frameStartTime) {

		long sleepTime = millisBetweenFrames - (System.currentTimeMillis() - frameStartTime);

		if (sleepTime > 0) {

			try {

				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e) {

				LOGGER.warn("Interrupted sleeping between frames");
			}
		}
		else {

			LOGGER.error("Cannot support FPS - lag: {}, number of games: {}", sleepTime, gamesToProcess.size());
		}
	}
}
