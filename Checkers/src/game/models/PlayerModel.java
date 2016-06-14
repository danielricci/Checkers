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

public final class PlayerModel extends GameModel {

	private static int TEAM_INDEX = 0;
	
	private final Team _team;
	private final Map<TileModel, CheckerPiece> _pieces = new HashMap<TileModel, CheckerPiece>();

	protected enum Team {
		PlayerX("/data/red_piece.png"), // TODO - can we not hc this
		PlayerY("/data/black_piece.png"); // TODO - can we not hc this
		
		public final String _teamName;	
		private Team(String teamName) {
			this._teamName = teamName;
		}
	}
	
	public PlayerModel(Observer observer) {
		super(observer);
		_team = Team.values()[TEAM_INDEX];
		TEAM_INDEX++;
	}

	public void removeTilePiece(TileModel tile) {
		_pieces.remove(tile);
	}
	
	public void addTilePiece(TileModel tile) {
		removeTilePiece(tile);
		_pieces.put(tile, new CheckerPiece(_team));
	}
	
	public void updateTilePiece(TileModel oldTile, TileModel newTile) {
		CheckerPiece piece = _pieces.get(oldTile);
		removeTilePiece(oldTile);
		_pieces.put(newTile, piece);
	}	
	
	public CheckerPiece getPiece(TileModel tile) {
		return _pieces.getOrDefault(tile, null);
	}
}