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

package engine.views.factory;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.JLabel;

import engine.controllers.BoardGameController;

@SuppressWarnings("serial")
public class GameTileView extends BaseView {

	private JLabel _coordinateLabel;
	private int _coordinate = -1;
	private static Color _defaultColor = Color.BLACK;
    private Image _image;

    public GameTileView(int coordinate) {
    	super(new BoardGameController());
    	_coordinate = coordinate;
    } 
   
    private void addListeners() {
    	
    	addMouseListener(new MouseAdapter() {  		
    		
    		@Override public void mouseEntered(MouseEvent event) {	    		
				setBackground(Color.LIGHT_GRAY);
			}
    		
    		@Override public void mouseExited(MouseEvent event) {
    			setBackground(_defaultColor);
    		}

    		@Override public void mouseClicked(MouseEvent event) {
				return;
				/*
				if(!_locked) {
					try {
						_image = new ImageIcon(position.getClass().getResource(_controller.getPlayerToken())).getImage();
						_controller.performMove(e);
						position._locked = true;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				position.repaint();
				*/
			}
		});
    }
    
    private void setCoordinate(int coordinate) {
		_coordinateLabel = new JLabel(coordinate + "");
		_coordinateLabel.setForeground(Color.WHITE);
		//add(_coordinateLabel);    		
    }

    // TODO Instead of using observable, can we wrap around it
    // so we get back something that has some more added helpers like getSourceAs
    // and it returns back the object as that or null
	@Override public void update(Observable obs, Object arg) {
		if(_image == null) {
			//GameTileModel model = (GameTileModel)obs;
			//_image = new ImageIcon(getClass().getResource(model.getPlayer().getTokenPath())).getImage();				
		}
	}

	@Override public void render() {
		setBackground(_defaultColor);
    	setCoordinate(_coordinate);
    	
    	addListeners();
    	
    	BoardGameController controller = (BoardGameController)getController(BoardGameController.class);
    	controller.populateTile(this);
	}
}
