/**
* Daniel Ricci <2016> <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package game.models;

import java.util.Observer;

public final class GameTileModel extends GameModel implements IPlayableTile {
    
	private PlayerModel _player;
	private boolean _activated;
	private final GameTileModel[] _neighbors = new GameTileModel[4];	
	
	public enum NeighborPosition { 
		LEFT(0), 
		RIGHT(1), 
		TOP(2),
		BOTTOM(3);
		
		private int _value;
		private NeighborPosition(int value) {
			_value = value;
		}
	};
	
    public GameTileModel(PlayerModel player, Observer... observers) {
		super(observers);
		_player = player;
		
		doneUpdating();
	}

	public void setNeighbor(NeighborPosition position, GameTileModel tile) { _neighbors[position._value] = tile; }
	public GameTileModel getNeighbor(NeighborPosition position) { return _neighbors[position._value]; }
   
	// TODO - can we avoid this?
	public PlayerModel getPlayer() { return _player; }
	
    @Override public boolean isMovableTo() {
    	return _player == null;
    }
    
	@Override public boolean isPlayable() {
		return _player != null;
	}
	
	public boolean getIsActivated() { return _activated; }
	public void setIsActivated(boolean isActivated) { _activated = isActivated; }
}