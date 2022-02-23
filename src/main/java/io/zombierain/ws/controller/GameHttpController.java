package io.zombierain.ws.controller;

import io.zombierain.ws.controller.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/game")
public class GameHttpController {

	private static final Logger LOGGER = LoggerFactory.getLogger("game-requests.logger");

	private final GameService gameService;

	@Autowired
	public GameHttpController(GameService gameService) {

		this.gameService = gameService;
	}

	@GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public GamesCount getCount() {

		return new GamesCount(gameService.getNumberOfActiveGames());
	}

	@PostMapping(value = "/host", produces = MediaType.APPLICATION_JSON_VALUE)
	public GameId postHost() {

		LOGGER.info("NEW");

		String gameId = gameService.hostNewGame();

		return new GameId(gameId);
	}

	@PostMapping(value = "/join/{gameId}")
	public void postJoin(@PathVariable String gameId, HttpServletResponse response) {

		if (!gameService.joinGame(gameId)) {

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	public static class GameId {

		public final String gameId;

		public GameId(String gameId) {

			this.gameId = gameId;
		}
	}

	public static class GamesCount {

		public final long currentlyActiveGames;

		public GamesCount(long currentlyActiveGames) {

			this.currentlyActiveGames = currentlyActiveGames;
		}
	}
}
