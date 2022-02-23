package io.zombierain.ws.controller;

import io.zombierain.ws.controller.service.GameService;
import io.zombierain.ws.game.states.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerWsController {

	private final GameService gameService;

	@Autowired
	public PlayerWsController(GameService gameService) {

		this.gameService = gameService;
	}

	@MessageMapping("/{gameId}/invoke-start")
	@SendTo("/{gameId}/start")
	public GameState invokeStart(@DestinationVariable String gameId) {

		return gameService.invokeGameStart(gameId);
	}

	@MessageMapping("{gameId}/player/1/move/left")
	public void player1MoveLeft(@DestinationVariable String gameId) {

		gameService.movePlayer1Left(gameId);
	}

	@MessageMapping("{gameId}/player/1/move/right")
	public void player1MoveRight(@DestinationVariable String gameId) {

		gameService.movePlayer1Right(gameId);
	}

	@MessageMapping("{gameId}/player/1/stop")
	public void player1Stop(@DestinationVariable String gameId) {

		gameService.player1Stop(gameId);
	}

	@MessageMapping("{gameId}/player/1/jump")
	public void player1Jump(@DestinationVariable String gameId) {

		gameService.player1Jump(gameId);
	}

	@MessageMapping("{gameId}/player/2/move/left")
	public void player2MoveLeft(@DestinationVariable String gameId) {

		gameService.movePlayer2Left(gameId);
	}

	@MessageMapping("{gameId}/player/2/move/right")
	public void player2MoveRight(@DestinationVariable String gameId) {

		gameService.movePlayer2Right(gameId);
	}

	@MessageMapping("{gameId}/player/2/stop")
	public void player2Stop(@DestinationVariable String gameId) {

		gameService.player2Stop(gameId);
	}

	@MessageMapping("{gameId}/player/2/jump")
	public void player2Jump(@DestinationVariable String gameId) {

		gameService.player2Jump(gameId);
	}
}
