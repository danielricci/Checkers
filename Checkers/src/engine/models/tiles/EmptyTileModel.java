package engine.models.tiles;

import java.util.Observer;

import engine.models.core.GameModel;

public class EmptyTileModel extends GameModel implements IPlayableTile {

	protected EmptyTileModel(Observer observer) {
		super(observer);
	}
	
	@Override public boolean isPlayable() {
		return true;
	}
}