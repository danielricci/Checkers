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

import game.models.PlayerModel.Team.Orientation;

/** 
 * A tile model represents a tile that knows about what it currently holds on itself
 * and its current state; it also is aware of its immediate neighbors
 */
public class TileModel extends GameModel implements IPlayableTile {
    
	/**
	 * The player, if any, that controls this tile
	 */
	private PlayerModel _player;
	
	private boolean _selected;
	
	/**
	 * The immediate neighbors of this tile 
	 */
	private final Map<NeighborPosition, Set<TileModel>> _neighbors = new HashMap<NeighborPosition, Set<TileModel>>();
		
	public enum NeighborPosition { 
		TOP,
		BOTTOM;
		
		public static NeighborPosition flip(NeighborPosition pos) {
			switch(pos) {
			case BOTTOM:
				return TOP;
			case TOP:
				return BOTTOM;
			default:
				return pos;
			}
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

	private Set<TileModel> getNeighbors(NeighborPosition position) {
		
		// Fetch the correct point of view of the player
		if(_player.getPlayerOrientation() == Orientation.DOWN)
		{
			position = NeighborPosition.flip(position);
		}
		
		return _neighbors.containsKey(position) ?
				_neighbors.get(position) : new HashSet<TileModel>(); 
	}

	public Set<TileModel> getNeighbors() {
		
		NeighborPosition neighborPosition = NeighborPosition.TOP;
		if(_player.getPlayerOrientation() == Orientation.DOWN)
		{
			neighborPosition = NeighborPosition.flip(neighborPosition);
		}
		
		return _neighbors.containsKey(neighborPosition) ?
				_neighbors.get(neighborPosition) : new HashSet<TileModel>();
	}
	
	public Set<TileModel> getAllNeighbors() {
		Set<TileModel> allNeighbours = getNeighbors(NeighborPosition.TOP);
		allNeighbours.addAll(getNeighbors(NeighborPosition.BOTTOM));
		return allNeighbours;
	}
   
	public void setSelected(Operation operation) {
		addOperation(operation);
		_selected = !_selected;
		doneUpdating();
	}
	
	public boolean isSelected() { return _selected; }

	public PlayerModel getPlayer() { return _player; }
	
    @Override public boolean isMovableTo() {
    	return _player == null && !_selected;
    }
    
	@Override public boolean isPlayable() {
		return _player != null && !_selected;
	}
}