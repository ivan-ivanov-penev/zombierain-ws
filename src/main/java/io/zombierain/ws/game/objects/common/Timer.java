package io.zombierain.ws.game.objects.common;

public class Timer {

	private final int frameCountGoal;

	private int frameCounter;

	public Timer(int gameFps, int durationInSeconds) {

		this.frameCountGoal = durationInSeconds * gameFps;
		this.frameCounter = 0;
	}

	public boolean tickAndCheckIfElapsed() {

		if (frameCounter < frameCountGoal) {

			frameCounter++;
		}

		return hasElapsed();
	}

	public boolean tickAndResetOnElapse() {

		frameCounter++;

		if (frameCounter > frameCountGoal) {

			reset();

			return true;
		}
		else {

			return false;
		}
	}

	public boolean willElapseSoon() {

		return frameCounter > frameCountGoal * 0.66;
	}

	public void reset() {

		frameCounter = 0;
	}

	public void elapse() {

		frameCounter = frameCountGoal;
	}

	public boolean hasElapsed() {

		return frameCounter == frameCountGoal;
	}

	public static class ElapsedTimer extends Timer {

		public ElapsedTimer(int gameFps, int durationInSeconds) {

			super(gameFps, durationInSeconds);
			this.elapse();
		}
	}
}
