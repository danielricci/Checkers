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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import game.content.PlayerPiece;
import game.models.PlayerModel.Team.Orientation;

public final class PlayerModel extends GameModel {

	private static int TEAM_INDEX = 0;
	
	private final Team _team;
	private final Map<TileModel, PlayerPiece> _pieces = new HashMap<TileModel, PlayerPiece>();
	private final int _tileCoordinate = TEAM_INDEX;
	
	public enum Team {
		PlayerX("/data/red_piece.png", Orientation.DOWN, Color.RED), 
		PlayerY("/data/black_piece.png", Orientation.UP, Color.BLACK);
		
		public enum Orientation { 
			UP, 
			DOWN
		}
		
		public final Color _teamColor;
		public final String _teamName;	
		public final Orientation _orientation;
		
		private Team(String teamName, Orientation orientation, Color teamColor) {
			_teamName = teamName;
			_orientation = orientation;
			_teamColor = teamColor;
		}
		
		@Override public String toString() {
			return "Orientation: \t" + _orientation.toString() + "\n " + "Name: \t" +  _teamName;
		}
	}
	
	public PlayerModel(Observer observer) {
		super(observer);
		_team = Team.values()[TEAM_INDEX++];
	}

	public void updatePlayerPiece(TileModel oldTile, TileModel newTile) {
		
		if(newTile != null) {
			_pieces.put(newTile, _pieces.get(oldTile));	
		}
		_pieces.remove(oldTile);
	}
	
	public void addTilePiece(TileModel tile) {
		_pieces.remove(tile);
		_pieces.put(tile, new PlayerPiece(_team));	
	}
	
	public PlayerPiece getPiece(TileModel tile) {
		return _pieces.getOrDefault(tile, null);
	}
	
	public int getTileCoordinate() {
		return _tileCoordinate;
	}
	
	public Color getTeamColor() { return _team._teamColor; }
	
	protected Orientation getPlayerOrientation() {
		return _team._orientation;
	}
	
	@Override public String toString() {
		return "Index: \t" + _tileCoordinate + "\n " + _team;	
	}
}