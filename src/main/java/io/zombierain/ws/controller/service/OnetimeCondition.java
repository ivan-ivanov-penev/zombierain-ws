package io.zombierain.ws.controller.service;

public class OnetimeCondition {

	private volatile boolean fulfilled;

	public OnetimeCondition() {

		this.fulfilled = false;
	}

	public synchronized boolean attemptToFulfilOnce() {

		if (fulfilled) {

			return false;
		}
		else {

			fulfilled = true;

			return true;
		}
	}
}
