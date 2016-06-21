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

import java.awt.Image;
import java.util.Set;

import game.controllers.factory.ControllerFactory.ControllerType;
import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;
import game.models.TileModel.NeighborPosition;
import game.models.TileModel.Selection;

public class TileController extends BaseController {
	
	private TileModel _tile;

	public TileController(TileModel tile) {
		_tile = tile;
	}
	
	public TileModel tileSelected() {
		return _tile;
	}
	
	public void setNeighbors(NeighborPosition neighborPosition, TileModel... neighborTiles) {	
		_tile.setNeighbors(neighborPosition, neighborTiles);
	}

	public void event_mouseClicked() {
		
		PlayerController playerController = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		
		// Makes it so that when we start a new game the first selected
		// player will start the game
		if(playerController.getCurrentPlayer() == null) {
			PlayerModel player = _tile.getPlayer();
			
			// Prevents exceptions from being thrown when determining the 
			// player at the start of the game when the tile selected has
			// no player
			if(player == null) {
				return;
			}
			playerController.setCurrentPlayer(_tile.getPlayer());
		}
		
		
		// Check if the player tries to select the piece of our opponent
		if(_tile.getPlayer() != null && _tile.getPlayer() != playerController.getCurrentPlayer()) {
			System.out.println("Cannot select other players pieces!");	
			return;
		}
		
		if(_tile.isSelected() || _tile.isGuideSelected() && _tile.getPlayer() != playerController.getCurrentPlayer()) {
			if(!_tile.isMovableTo()) {
				_tile.setSelected(Operation.PlayerPieceMoveCancel, Selection.None);
			} 
			else {
				_tile.setSelected(Operation.PlayerPieceMoveAccepted, Selection.None, true);				
			}				
		}
		else {
			if(_tile.getPlayer() == playerController.getCurrentPlayer()) {
				if(hasMoves())
				{
					System.out.println("Player has selected a piece to be moved");
					_tile.setSelected(Operation.PlayerPieceSelected, Selection.MoveSelected, true);	
				}
				else 
				{
					System.out.println("Player is selecting his own tile but there are no moves");
				}
			}
			else {
				System.out.println("Player is selecting a tile that is not a valid move");
			}
		}
	}
	
	private boolean hasMoves() {
		
		Set<TileModel> neighbors = _tile.getNeighbors();
		if(neighbors.size() == 0) {
			return false;
		}

		for(TileModel neighbor : neighbors) {
			if(neighbor.isMovableTo() || neighbor.getCapturableNeighbors().size() > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public Image getTileImage() {
		PlayerModel model = _tile.getPlayer();
		if(model != null) {
			return model.getPiece(_tile).getImage();
		}
		
		return null;
	}
	
	public void tileGuidesCommand(TileModel tileModel, Operation operation) {
		// todo - capturable scenarios should only occur
		// on a linear trajectory only!!!!
		
		Selection selection = operation == Operation.ShowGuides ? Selection.GuideSelected : Selection.None;
		for(TileModel neighbor : tileModel.getNeighbors()) {
			if(neighbor.isMovableTo()) {
				neighbor.setSelected(operation, selection);				
			}
			else if(neighbor.getPlayer() != tileModel.getPlayer()){
				Set<TileModel> capturablePositions = neighbor.getCapturableNeighbors();
				if(capturablePositions.size() > 0) {
					for(TileModel capturablePosition : capturablePositions) {
						capturablePosition.setSelected(operation, selection);
					}
					neighbor.setSelected(operation, Selection.CaptureSelected);					
				}
			}
		}
  	}

	public int getTileID() {
		return _tile.getIdentifier();
	}
}
