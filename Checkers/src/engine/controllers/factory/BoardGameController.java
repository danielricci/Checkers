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

package engine.controllers.factory;

import java.util.LinkedList;
import java.util.Observer;
import java.util.Queue;
import java.util.Vector;

import engine.models.GameTileModel;
import engine.models.PlayerModel;
import engine.models.PlayerModel.Team;

public class BoardGameController extends BaseController {

	private final Vector<GameTileModel> _tiles = new Vector<GameTileModel>();
	private final Queue<PlayerModel> _turns = new LinkedList<PlayerModel>();
	
	public void populatePlayers(Observer observer) {
		PlayerModel player1 = new PlayerModel(observer, Team.PlayerX);
		PlayerModel player2 = new PlayerModel(observer, Team.PlayerY);
		
		_turns.add(player1);
		_turns.add(player2);
	}

	public void populateTile(Observer observer) {
		GameTileModel model = new GameTileModel(observer, _turns.element()); // TODO - remove hardcoded player
		_tiles.addElement(model);
	}

	public PlayerModel getCurrentPlayer() {
		return _turns.peek();
	}	
}