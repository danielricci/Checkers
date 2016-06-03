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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.controllers.BoardGameController;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		super(new BoardGameController());
		BoardGameController controller = (BoardGameController) getController(BoardGameController.class);
		controller.populatePlayers(this);
	}

	public class GameTileView extends BaseView {

		private JLabel _coordinateLabel = null;
		private Color _color = null;
	    private Image _image = null;
    
	    public GameTileView(Color color, Image image, int coordinate) {
	    	_color = color;
	    	_image = image;
	    	
	    	setBackground(_color);
	    	setCoordinate(coordinate);
	    	
	    	addListeners();
	    	
	    	BoardGameController controller = (BoardGameController) BoardGameView.this.getController(BoardGameController.class);
	    	controller.populateTile(this);
	    } 
	   
	    private void addListeners() {
	    	addMouseListener(new MouseAdapter() {  		
	    		@Override public void mouseEntered(MouseEvent e) {
	    			return;
	    			/*
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
	    			
	    			Object source = e.getSource();
	    			if(source instanceof BoardPosition) {
	    				BoardPosition position = (BoardPosition)source;
	    				
	    				if(!_locked) {
	    					position.setBackground(Color.LIGHT_GRAY);
	    					try {
								_image = new ImageIcon(position.getClass().getResource(_controller.getPlayerToken())).getImage();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
	    				}
    				
						position.repaint();
	    			}
	    			*/
	    		}
	    		@Override public void mouseExited(MouseEvent e) {
	    			return;
	    			/*
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
    			
    				if(!_locked)
    				{
    					_image = null;	
    				}
    				
    				BoardPosition position = (BoardPosition)e.getSource();
    				setBackground(position._color);
    				*/
	    		}
	    		@Override public void mouseClicked(MouseEvent e) {
					return;
					/*
	    			if(_controller.isGameOver())
	    			{
	    				return;
	    			}
	    			
					// Get the board that we selected to give our event handler some context
					BoardPosition position = (BoardPosition)e.getSource();
				
					// If the player can play and there is no selection yet
					// then take ownership of the position and put our token
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
	    	add(_coordinateLabel);
	    }

		@Override public void update(Observable obs, Object arg) {
		}

		@Override public void render() {
		}
	}
	
	@Override public void update(Observable obs, Object arg) {	
		int x = 55;
	}
	
	@Override public void render() {
	
		_gamePanel.setBackground(Color.RED);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		for (int row = 0, coordinate = 1; row < 12; ++row) {		
			for (int col = 0, colorOffset = (row % 2 == 0 ? 0 : 1); col < 12; ++col, ++coordinate) {
				
				// determine if we should render our game tile for this cell
				boolean shouldRender = (col + colorOffset) % 2 == 0 ? false : true;
				if(!shouldRender) {
					continue;
				}
				
				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;
				_gamePanel.add(new GameTileView(Color.BLACK, null, coordinate), gbc);
			}
		}
		
		add(_gamePanel);
	}
}