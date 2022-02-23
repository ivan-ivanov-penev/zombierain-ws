package io.zombierain.ws.game.engine;

import io.zombierain.ws.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Engine {

	private final List<EngineWorker> workers;

	@Autowired
	public Engine(List<EngineWorker> workers) {

		this.workers = workers;
	}

	@PostConstruct
	public void startWorkers() {

		workers.forEach(EngineWorker::start);
	}

	public boolean attemptToStartNewGame(Game game) {

		return attemptToAddGameToWorker(game);
	}

	private boolean attemptToAddGameToWorker(Game game) {

		for (EngineWorker worker : workers) {

			if (worker.accept(game)) {

				return true;
			}
		}

		return false;
	}
}
