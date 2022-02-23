package io.zombierain.ws.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IdGenerator {

	private static final char[] CODE_CHARACTERS = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	private final int idLength;

	private final Random random;

	@Autowired
	public IdGenerator(@Value("${game.id.length}") int idLength) {

		this(idLength, new Random());
	}

	public IdGenerator(int idLength, Random random) {

		this.idLength = idLength;
		this.random = random;
	}

	public String generateGameId() {

		StringBuilder gameId = new StringBuilder();

		for (int i = 0; i < idLength; i++) {

			int index = random.nextInt(CODE_CHARACTERS.length);

			gameId.append(CODE_CHARACTERS[index]);
		}

		return gameId.toString();
	}
}
