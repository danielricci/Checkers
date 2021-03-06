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
import java.util.Map;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import game.IDestructable;
import game.content.PlayerPiece;
import game.models.PlayerModel.Team.Orientation;

/** 
 * A tile model represents a tile that knows about what it currently holds on itself
 * and its current state; it also is aware of its immediate neighbors
 */
public class TileModel extends GameModel implements IPlayableTile, IDestructable, Comparable<TileModel> {
    
	private PlayerModel _player;	
	private Selection _selection;
	
	private static int IDENTIFIER;
	private final int _identifier = ++IDENTIFIER;
    
	private final Map<NeighborPosition, SortedSet<TileModel>> _neighbors = new HashMap<>();
		
	private final boolean _isKingTile;
	
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
			case BOTTOM:
				break;
			case TOP:
				break;
			default:
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
	
    public TileModel(PlayerModel player, boolean isKingTile, Observer... observers) {
		super(observers);
		
		_player = player;
		_isKingTile = isKingTile;
		System.out.println("Tile " + _identifier + " is " + (isKingTile ? " a king " : "not a king"));
		if(player != null) {
			player.addTilePiece(this);
		}
		doneUpdating();
	}
   
	private SortedSet<TileModel> getNeighbors(NeighborPosition position) {
		
		if(position.isAgnostic()) {
			position = NeighborPosition.fromAgnostic(position);
		}
		else if(_player.getPlayerOrientation() == Orientation.DOWN) {
			// This is done to normalize the neighbor concept
			position = NeighborPosition.flip(position);
		}
		
		SortedSet<TileModel> tiles = new TreeSet<>();
		if(_neighbors.containsKey(position)) {
			tiles.addAll(_neighbors.get(position));
		}
		return tiles;
	}
	public SortedSet<TileModel> getBackwardNeighbors() {
		SortedSet<TileModel> neighbors = new TreeSet<>();
		NeighborPosition neighborPosition = NeighborPosition.BOTTOM;
		
		if(_player.getPlayerOrientation() == Orientation.DOWN)
		{
			neighborPosition = NeighborPosition.flip(neighborPosition);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			neighbors.addAll(_neighbors.get(neighborPosition));
		}
		
		return neighbors;				
	}
	
	public SortedSet<TileModel> getForwardNeighbors() {
		
		SortedSet<TileModel> neighbors = new TreeSet<>();
		
		NeighborPosition neighborPosition = NeighborPosition.TOP;
		if(_player != null && _player.getPlayerOrientation() == Orientation.DOWN)
		{
			neighborPosition = NeighborPosition.flip(neighborPosition);
		}
		
		if(_neighbors.containsKey(neighborPosition)) {
			neighbors.addAll(_neighbors.get(neighborPosition));
		}
		
		return neighbors;		
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

		SortedSet<TileModel> tiles = new TreeSet<>();
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
	
	/**
	 * Updates the player of this 
	 * @param player
	 */
	public void updateOwner(PlayerModel player) {
		
		// Update the piece of the tile so that it is now
		// associated to this tile
		PlayerPiece piece = _player.getPlayerPiece(this);
		
		// Update the piece to indicate the new player that owns it
		piece.updatePlayerPiece(this, player);
		
		// Update the tile to indicate the player is currently playing on it
		_player = player;
				
		// Update the players list of pieces that it owns
		player.addTilePiece(this, piece);
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
	
	public SortedSet<TileModel> getAllNeighbors() {
		SortedSet<TileModel> allNeighbours = new TreeSet<>(
			getNeighbors(NeighborPosition.TOP)
		);
		allNeighbours.addAll(getNeighbors(NeighborPosition.BOTTOM));
		
		return allNeighbours;
	}
	
	@Override public boolean isMovableTo() {
    	return _player == null && _selection != Selection.MoveSelected;
    }
    
	@Override public boolean isPlayable() {
		return _player != null && _selection != Selection.MoveSelected;
	}
	
	public boolean getIsKingTile() {
		return _isKingTile;
	}
	
	/**
	 * Gets all of the tiles surrounding this tile that are considered "capturable" w.r.t the passed in capturer
	 * 
	 * @param capturer The tile performing the capture on this tile
	 * 
	 * @return List of tiles that are capturable by the capturer
	 */
	public Vector<TileModel> getCapturableNeighbors(TileModel capturer) {
		
		Vector<TileModel> capturablePositions = new Vector<>();
		if(_player == null || capturer.getPlayer() == _player) {
			return capturablePositions;
		}
		
		NeighborPosition position = NeighborPosition.convertOrientation(_player.getPlayerOrientation());
		populateCapturableTiles(capturer, capturablePositions, capturer.getForwardNeighbors(), position);
		if(capturer.getPlayer().getPlayerPiece(capturer).getIsKinged()) {
			populateCapturableTiles(capturer, capturablePositions, capturer.getBackwardNeighbors(), NeighborPosition.flip(position));
		}
		
		return capturablePositions;
	}
	
	private void populateCapturableTiles(TileModel capturer, Vector<TileModel> outCapturablePositions, SortedSet<TileModel> capturerNeighbors, NeighborPosition position) {
		int index = -1;
		if(capturerNeighbors.contains(this)) {
			index = capturerNeighbors.headSet(this).size();
		} 
		
		if(index == -1) {
			return;
		}
			
		position = NeighborPosition.flip(position);
		position = NeighborPosition.toAgnostic(position);
		
		Object[] neighborObjects = getNeighbors(position).toArray();
		if(neighborObjects.length > 1) {
			TileModel diagonalTile = (TileModel) neighborObjects[index];
			if(capturer.getForwardNeighbors().size() == 1 && diagonalTile.getForwardNeighbors().size() == 1) {
				diagonalTile = (TileModel)neighborObjects[++index];
			}
			
			if(diagonalTile.isMovableTo()) {
				outCapturablePositions.add(diagonalTile);
			}	
		}
	}
	
	@Override public String toString() {
		return Integer.toString(_identifier);
	}

	@Override public int compareTo(TileModel tileModel) {
		if(tileModel == this || _identifier == tileModel._identifier) {
			return 0;
		}
		
		return _identifier < tileModel._identifier ? -1 : 1;
	}

	@Override public void destroy() {
		
	}
}