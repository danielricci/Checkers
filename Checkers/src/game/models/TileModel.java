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

import org.eclipse.jdt.annotation.NonNull;

import game.models.PlayerModel.Team.Orientation;

/** 
 * A tile model represents a tile that knows about what it currently holds on itself
 * and its current state; it also is aware of its immediate neighbors
 */
public class TileModel extends GameModel implements IPlayableTile {
    
	private PlayerModel _player;	
	private Selection _selection;
	
	private static int IDENTIFIER = 0;
	private final int _identifier = ++IDENTIFIER;
	
	private final Map<NeighborPosition, Set<TileModel>> _neighbors = new HashMap<NeighborPosition, Set<TileModel>>();
		
	public enum Selection {
		GuideSelected,
		MoveSelected,
		CaptureSelected,
		None;
	}
	
	public enum NeighborPosition {
		
		/**
		 * Note: Make sure that agnostic values follow non-agnostic ones
		 */
		TOP				(1 << 0, false),
		TOP_AGNOSTIC	(1 << 1, true),
		BOTTOM			(1 << 2, false),
		BOTTOM_AGNOSTIC	(1 << 3, true);
	
		private final int _value;
		private final boolean _agnostic;
		
		private NeighborPosition(int value, boolean agnostic ) {
			_value = value;
			_agnostic = agnostic;
		}
		
		protected static NeighborPosition flip(NeighborPosition pos) {
			switch(pos) {
			case BOTTOM:
				return TOP;
			case TOP:
				return BOTTOM;
			default:
				return pos;
			}
		}
		
		/**
		 * Normalizes a position by removing its agnostic value
		 */
		protected static NeighborPosition fromAgnostic(NeighborPosition position) {
			switch(position) {
			case BOTTOM_AGNOSTIC:
			case TOP_AGNOSTIC:
				int val = position._value >> 1;
				for(NeighborPosition pos : NeighborPosition.values()) {
					if(pos._value == val) {
						return pos;
					}
				}			
				break;
			}
			System.out.println("Error with fromAgnostic");
			System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
			return position;
		}
		
		protected boolean isAgnostic() {
			return _agnostic;
		}
		
		protected static NeighborPosition convertOrientation(Orientation orientation) {
			return orientation == Orientation.UP 
				? NeighborPosition.TOP 
				: NeighborPosition.BOTTOM;
		}
		
		protected static NeighborPosition toAgnostic(NeighborPosition position) {
			switch(position) {
			case BOTTOM:
			case TOP:
				int val = position._value << 1;
				for(NeighborPosition pos : NeighborPosition.values()) {
					if(pos._value == val) {
						return pos;
					}
				}
				break;
			case BOTTOM_AGNOSTIC:
				break;
			case TOP_AGNOSTIC:
				break;
			default:
				break;
			}
			
			System.out.println("Error with toAgnostic");
			System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
			return position;
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
   
	private Set<TileModel> getNeighbors(NeighborPosition position) {
		
		if(position.isAgnostic()) {
			position = NeighborPosition.fromAgnostic(position);
		}
		else if(_player.getPlayerOrientation() == Orientation.DOWN) {
			// This is done to normalize the neighbor concept
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
	

   
	public void setSelected(Operation operation, Selection selection, boolean flushBuffer) {
		if(flushBuffer) {
			clearOperations();
		}
		addOperation(operation);
		_selection = selection;
		doneUpdating();
	}
	
	public void setSelected(Operation operation, Selection selection) { setSelected(operation, selection, false); }
		
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
	
	public void removeTile() {
		_player.updatePlayerPiece(this,  null);
		_player = null;
		clearOperations();
		doneUpdating();
	}
	
	public void swapWith(TileModel tileModel) {
		
		// Update the player
		PlayerModel player = _player;
		_player = tileModel._player;
		tileModel._player = player;
		
		// Update the players entry
		tileModel._player.updatePlayerPiece(this, tileModel);
		
		// Send done signals
		doneUpdating();
		tileModel.doneUpdating();
	}
	
	public boolean isPlayerHuman() { return _player != null; }
	public boolean isSelected() { return _selection == Selection.MoveSelected; }
	public boolean isGuideSelected() { return _selection == Selection.GuideSelected; }

	public int getIdentifier() { return _identifier; }
	
	public Selection getSelectionType() { return _selection; }
	public PlayerModel getPlayer() { return _player; }
	
	public Set<TileModel> getAllNeighbors() {
		Set<TileModel> allNeighbours = getNeighbors(NeighborPosition.TOP);
		allNeighbours.addAll(getNeighbors(NeighborPosition.BOTTOM));
		return allNeighbours;
	}
	
	@Override public boolean isMovableTo() {
    	return _player == null && _selection != Selection.MoveSelected;
    }
    
	@Override public boolean isPlayable() {
		return _player != null && _selection != Selection.MoveSelected;
	}

	public @NonNull Set<TileModel> getCapturableNeighbors() {
		
		Set<TileModel> capturablePositions = new HashSet<TileModel>();
		
		if(_player == null) {
			return capturablePositions;
		}
		
		// Get the position of our capturers neighbors
		NeighborPosition position = NeighborPosition.convertOrientation(_player.getPlayerOrientation());
		position = NeighborPosition.flip(position);
		position = NeighborPosition.toAgnostic(position);
		
		
		// Check if any of them have any pieces associated to them
		for(TileModel neighbor : getNeighbors(position)) {
			if(neighbor._player == null) {
				capturablePositions.add(neighbor);
			}
		}
		
		return capturablePositions;
	}
	
	@Override public String toString() {
		return Integer.toString(_identifier);
	}
}