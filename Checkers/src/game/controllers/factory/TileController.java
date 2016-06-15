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

import java.util.Set;

import game.controllers.factory.ControllerFactory.ControllerType;
import game.models.GameModel.Operation;
import game.models.TileModel;
import game.models.TileModel.NeighborPosition;

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
		
		// Check if the player tries to select the piece of our opponent
		if(_tile.getPlayer() != null && _tile.getPlayer() != playerController.getCurrentPlayer()) {
			System.out.println("Cannot select other players pieces!");	
			return;
		}
		
		if(_tile.isSelected()) {
			if(_tile.getPlayer() == playerController.getCurrentPlayer()) {
				System.out.println("Player is deselecting his own selected tile with his piece on it");
				_tile.setSelected(Operation.PlayerPieceCancel);				
			}
			else {
				System.out.println("Player is deselecting on an empty tile that was selected");
				_tile.setSelected(Operation.EmptyTileCancel);
			}
		}
		else {
			if(_tile.getPlayer() == playerController.getCurrentPlayer()) {
				if(hasMoves())
				{
					System.out.println("Player is selecting his own tile for the first time");
					_tile.setSelected(Operation.PlayerPieceSelected);	
				}
				else 
				{
					System.out.println("Player is selecting his own tile but there are no moves");
				}
			}
			else {
				System.out.println("Player is selecting an empty tile");
				_tile.setSelected(Operation.EmptyTileSelected);				
			}
		}
	}
	
	private boolean hasMoves() {
		
		Set<TileModel> neighbors = _tile.getNeighbors();
		if(neighbors.size() == 0) {
			return false;
		}

		for(TileModel neighbor : neighbors) {
			if(neighbor.isMovableTo()) {
				return true;
			}
		}
		
		return false;
	}
}
