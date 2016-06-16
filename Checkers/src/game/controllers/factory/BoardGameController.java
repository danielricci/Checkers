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

import java.util.LinkedList;
import java.util.Observer;
import java.util.Queue;
import java.util.Vector;

import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;

public class BoardGameController extends BaseController {

	private final Vector<TileModel> _tiles = new Vector<TileModel>();		
	private static final int _rows = 12;
	private final Queue<Operation> _operations = new LinkedList<Operation>();
	private final Queue<TileModel> _selectedTiles = new LinkedList<TileModel>();
	
  	public TileModel populateTile(PlayerModel player, Observer... observers) {		
		TileModel model = new TileModel(player, observers);
		_tiles.addElement(model);
		
		return model;
	}

  	public void addTileModelSelected(TileModel tile) {
  		_selectedTiles.add(tile);  		
  	}
  	
  	public void addGameOperation(Operation operation) { 
  		_operations.add(operation);
	}
  	
  	public void processCommands() {  		
  		while(_operations.size() > 0){
  	  		Operation operation = _operations.poll();
  	  		System.out.println("performing operation " + operation.toString());  	  		
  		}
  	}
  	
  
  	
	public int getBoardDimensions() {
		return _rows;
	}

	public void clear() { _selectedTiles.clear(); _operations.clear(); }
}