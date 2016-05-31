package mainline.models.concrete;

import java.util.Observer;

import mainline.models.AGameModel;

public class EmptyTileModel extends AGameModel implements IPlayableTile {

	protected EmptyTileModel(Observer observer) {
		super(observer);
	}
	
	@Override public boolean isPlayable() {
		return true;
	}
}