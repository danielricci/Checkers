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

public class BoardGameController extends BaseController {

	private final Vector<TileModel> _tiles = new Vector<TileModel>();		
	private static final int _rows = 12;
	private final Vector<Operation> _operations = new Vector<Operation>();
	
  	public TileModel populateTile(PlayerModel player, Observer... observers) {		
		TileModel model = new TileModel(player, observers);
		_tiles.addElement(model);
		
		return model;
	}
	
  	public void addGameOperation(Operation operation) { _operations.addElement(operation); };
  	
  	public void processOperations() {
  		_operations.clear();
  	}
  	
  	public void showTileGuides(TileModel tileModel) {
		for(TileModel neighbor : tileModel.getNeighbors()) {
			neighbor.setSelected(Operation.ShowGuides);
		}
  	}
  	
  	
  	
  	
	public int getBoardDimensions() {
		return _rows;
	}
}