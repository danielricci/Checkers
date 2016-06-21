/**
* Daniel Ricci <thedanny09@gmail.com>
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

package game.controllers.factory;

import java.util.Observer;
import java.util.Vector;

import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;
import game.models.TileModel.Selection;

public class BoardGameController extends BaseController {

	private final Vector<TileModel> _tiles = new Vector<TileModel>();		
	private static final int _rows = 12;
	
	private TileModel _previouslySelectedTile;
	
  	public TileModel populateTile(PlayerModel player, Observer... observers) {		
		TileModel model = new TileModel(player, observers);
		_tiles.addElement(model);
		
		return model;
	}

  	public void addTileModelOnSelected(TileModel tile) {
		if(_previouslySelectedTile != null) {
			_previouslySelectedTile.setSelected(Operation.PlayerPieceMoveCancel, Selection.None, true);			
			if(_previouslySelectedTile != tile) {
				tile.setSelected(Operation.PlayerPieceSelected, Selection.MoveSelected);
				_previouslySelectedTile = tile;
			}
			else {
				_previouslySelectedTile = null;				
			}
		} 
		else {
			_previouslySelectedTile = tile;	
		}
  	} 	

	public int getBoardDimensions() {
		return _rows;
	}

	public void processTileMove(TileModel selectedTile) {		
		_previouslySelectedTile.setSelected(Operation.HideGuides, Selection.None, true);
		
		// We are performing a capture, so the capture tile is a neighbor
		// of what we have selected, so we need to handle the move to state
		if(!_previouslySelectedTile.getNeighbors().contains(selectedTile)) {
			// TODO - this causes the hide guides to be effectively called twice
			// all just to clear its tile operation, can we do this a bit cleaner?
			selectedTile.setSelected(Operation.HideGuides, Selection.None, true);
		}
		
		for(TileModel model : _previouslySelectedTile.getNeighbors()) {
			// TODO - instead of get all neighbors can we have a getAgnosticNeighbors(NeighborPosition) ?
			if(model.getSelectionType() == Selection.CaptureSelected && model.getAllNeighbors().contains(selectedTile)) {
				model.removeTile();
			}
			model.setSelected(Operation.HideGuides, Selection.None, true);
		}
		
		_previouslySelectedTile.swapWith(selectedTile);
		_previouslySelectedTile = null;
	}

	public void processTileCancel(TileModel tileModel) {
		for(TileModel model : _previouslySelectedTile.getNeighbors()) {
			model.setSelected(Operation.HideGuides, Selection.None, true);
		}	
		_previouslySelectedTile = null;
	}
}