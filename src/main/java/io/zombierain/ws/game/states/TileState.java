package io.zombierain.ws.game.states;

public class TileState extends Position {

	public final int index;

	public TileState(float x, float y, int index) {

		super(x, y);
		this.index = index;
	}
}
