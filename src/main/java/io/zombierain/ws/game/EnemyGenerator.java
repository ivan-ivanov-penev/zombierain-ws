package io.zombierain.ws.game;

import io.zombierain.ws.config.game.objects.EnemyConfig;
import io.zombierain.ws.config.game.objects.GameConfig;
import io.zombierain.ws.game.objects.Enemy;
import io.zombierain.ws.game.objects.common.Timer;
import io.zombierain.ws.game.states.EnemyState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnemyGenerator {

	private final GameConfig gameConfig;

	private final EnemyConfig enemyConfig;

	private final List<Enemy> enemies;

	private final Timer initTimer;

	private final Timer spawnTimer;

	private int enemyCounter;

	public EnemyGenerator(EnemyConfig enemyConfig, GameConfig gameConfig) {

		this(enemyConfig,
				gameConfig,
				new ArrayList<>(),
				new Timer(gameConfig.fps, enemyConfig.spawnInitialDelaySeconds),
				new Timer(gameConfig.fps, enemyConfig.spawnIntervalSeconds),
				0);
	}

	EnemyGenerator(
			EnemyConfig enemyConfig,
			GameConfig gameConfig,
			List<Enemy> enemies,
			Timer initTimer,
			Timer spawnTimer,
			int enemyCounter) {

		this.gameConfig = gameConfig;
		this.enemyConfig = enemyConfig;
		this.enemies = enemies;
		this.initTimer = initTimer;
		this.spawnTimer = spawnTimer;
		this.enemyCounter = enemyCounter;
	}

	public List<Enemy> update() {

		if (initTimer.tickAndCheckIfElapsed()) {

			while (countAliveEnemies() < 2) {

				addNewEnemy();
			}

			if (spawnTimer.tickAndResetOnElapse()) {

				addNewEnemy();
			}
		}

		return enemies;
	}

	private int countAliveEnemies() {

		List<Enemy> enemiesToRemove = new ArrayList<>();

		for (Enemy enemy : enemies) {

			if (enemy.isToBeRemoved()) {

				enemiesToRemove.add(enemy);
			}
		}

		enemies.removeAll(enemiesToRemove);

		return enemies.size();
	}

	private void addNewEnemy() {

		float x = enemies.size() % 2 == 0 ? enemyConfig.width / 2f : gameConfig.width - enemyConfig.width / 2f;

		enemies.add(new Enemy(enemyConfig, gameConfig, x, -enemyConfig.height, enemyCounter++));
	}

	public List<EnemyState> constructStates() {

		return enemies.stream().map(Enemy::constructState).collect(Collectors.toList());
	}

	public boolean zombiesExceedLimit() {

		return enemies.size() > 15;
	}
}
