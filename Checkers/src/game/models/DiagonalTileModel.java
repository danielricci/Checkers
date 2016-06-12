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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

public final class DiagonalTileModel extends GameModel implements IPlayableTile {
    
	private PlayerModel _player;
	private boolean _activated;
	
	private final Map<NeighborPosition, Set<DiagonalTileModel>> _neighbors = new HashMap<NeighborPosition, Set<DiagonalTileModel>>(); 
	private boolean _selected;
	
	public enum NeighborPosition { 
		TOP(0),
		BOTTOM(1);
		
		private int _value;
		private NeighborPosition(int value) {
			_value = value;
		}
	};
	
    public DiagonalTileModel(PlayerModel player, Observer... observers) {
		super(observers);
		_player = player;
		
		doneUpdating();
	}

    // TODO - this functionality should be in the controller!!!!!!!
	public void setNeighbors(NeighborPosition neighborPosition, DiagonalTileModel... neighborTiles) {	
		
		Set<DiagonalTileModel> tiles = new HashSet<DiagonalTileModel>();
		for(DiagonalTileModel neighborTile : neighborTiles) {
			tiles.add(neighborTile);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			_neighbors.get(neighborPosition).addAll(tiles);
		} 
		else {
			_neighbors.put(neighborPosition, tiles);	
		}
		
	}
	
	public Set<DiagonalTileModel> getNeighbors(NeighborPosition position) { 	
		return _neighbors.containsKey(position) ?
				_neighbors.get(position) : new HashSet<DiagonalTileModel>(); 
	}
   
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

	public void setSelected(boolean selected) {
		_selected = selected;
		doneUpdating();
	}
	
	public boolean getSelected() { return _selected; }
}