package mainline.models;

import java.awt.Color;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

public class BoardPositionModel extends GameModel {

	private PlayerModel _owner = null;
	private int _coordinate = -1;

	// TODO - do we need this still?
	private boolean _locked = false; 

	// TODO - Instead of this should we have diagonals?
    private BoardPositionModel _left = null;
    private BoardPositionModel _top = null;
    private BoardPositionModel _right = null;
    private BoardPositionModel _bottom = null;	
	
	// java.awt
	// TODO - can this be removed?
	private Color _color = null;
	private Image _image = null;

	public BoardPositionModel(Color color, Image image) {
		_color = color;
    	_image = image;
	}
	
	public void setLeft(BoardPositionModel position) { _left = position; }
    public void setTop(BoardPositionModel position) { _top = position; }
    public void setRight(BoardPositionModel position) { _right = position; }
    public void setBottom(BoardPositionModel position) { _bottom = position; }
    	    
    public BoardPositionModel getNeighbourTop() { return _top; }
    public BoardPositionModel getNeighbourBottom() { return _bottom; }
    public BoardPositionModel getNeighbourLeft() { return _left; }
    public BoardPositionModel getNeighbourRight() { return _right; }


    
    
    
    
    
    
    
    
    
    
    
    
    
}
