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

import java.util.Observer;

public final class GameTileModel extends GameModel implements IPlayableTile {
    
	private PlayerModel _player;

	/*
    private GameTileModel _left;
    private GameTileModel _top;
    private GameTileModel _right;
    private GameTileModel _bottom;	
	*/

    public GameTileModel(Observer observer, PlayerModel player) {
		super(observer);
		_player = player;
		
		setChanged();
		notifyObservers();
	}
	
    /*
	public void setTop(GameTileModel position) { _top = position; }
    public GameTileModel getTop() { return _top; }
    
    public void setBottom(GameTileModel position) { _bottom = position; }
    public GameTileModel getBottom() { return _bottom; }
    
    public void setLeft(GameTileModel position) { _left = position; }
    public GameTileModel getLeft() { return _left; }
    
    public void setRight(GameTileModel position) { _right = position; }
    public GameTileModel getRight() { return _right; }
    */
   
    public PlayerModel getPlayer() { return _player; } 
   
    @Override public boolean isMovableTo() {
    	return _player == null;
    }
    
	@Override public boolean isPlayable() {
		return _player != null;
	}
}