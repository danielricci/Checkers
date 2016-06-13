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

/** 
 * A tile model represents a tile that knows about what it currently holds on itself
 * and its current state; it also is aware of its immediate neighbors
 */
public class TileModel extends GameModel implements IPlayableTile {
    
	/**
	 * The player, if any, that controls this tile
	 */
	private PlayerModel _player;
	
	/**
	 * The immediate neighbors of this tile 
	 */
	private final Map<NeighborPosition, Set<TileModel>> _neighbors = new HashMap<NeighborPosition, Set<TileModel>>();
	
	/**
	 * If this tile is currently in a selected state
	 */
	private boolean _selected;
	
	public enum NeighborPosition { 
		TOP(0),
		BOTTOM(1);
		
		private int _value;
		private NeighborPosition(int value) {
			_value = value;
		}
	};
	
    public TileModel(PlayerModel player, Observer... observers) {
		super(observers);
		_player = player;
		if(player != null) {
			player.addTilePiece(this);
		}
		doneUpdating();
	}

	public void setNeighbors(NeighborPosition neighborPosition, TileModel... neighborTiles) {	
		
		Set<TileModel> tiles = new HashSet<TileModel>();
		for(TileModel neighborTile : neighborTiles) {
			tiles.add(neighborTile);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			_neighbors.get(neighborPosition).addAll(tiles);
		} 
		else {
			_neighbors.put(neighborPosition, tiles);	
		}
	}
	
	public Set<TileModel> getNeighbors(NeighborPosition position) { 	
		return _neighbors.containsKey(position) ?
				_neighbors.get(position) : new HashSet<TileModel>(); 
	}
   
	public void setSelected(boolean selected) {
		_selected = selected;
		doneUpdating();
	}
	
	public boolean getSelected() { return _selected; }
	public PlayerModel getPlayer() { return _player; }
	
    @Override public boolean isMovableTo() {
    	return _player == null;
    }
    
	@Override public boolean isPlayable() {
		return _player != null;
	}
}