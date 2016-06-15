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

package game.views.factory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import game.controllers.factory.TileController;
import game.models.PlayerPiece;
import game.models.GameModel;
import game.models.PlayerModel;
import game.models.TileModel;

@SuppressWarnings("serial")
public class TileView extends BaseView {

	private static final Color _defaultColor = Color.LIGHT_GRAY;
	private static final Color _hoverColor = Color.DARK_GRAY;
    
	private Image _image; // todo - we can remove this, and do a component based removal afterwards?
	
    @Override protected void registerListeners() {
    	
    	addMouseListener(new MouseAdapter() {  		
    		
    		@Override public void mouseEntered(MouseEvent event) {
    			//setBackground(_hoverColor);
			}
    		
    		@Override public void mouseExited(MouseEvent event) {
    			//setBackground(_defaultColor);
    		}

    		@Override public void mouseClicked(MouseEvent event) {
    			/*
    			 * Clicking on a tile results in potentially a range of things
    			 * It is not the views job to determine what these things are,
    			 * so we need to query a bunch of things, and this is done in 
    			 * the controller
    			 */
    			TileController controller = getController(TileController.class);
    			controller.event_mouseClicked();
    			
    			/*
    			TileModel model = controller.tileSelected();
    			for(TileModel tile : model.getNeighbors(NeighborPosition.BOTTOM)) {
    				tile.setSelected(true);
    			}
    			for(TileModel tile : model.getNeighbors(NeighborPosition.TOP)) {
    				tile.setSelected(true);
    			}*/
			}
		});
    }
    
    private void updateSelectedCommand(boolean selected) {
    	setBackground(selected ? _hoverColor : _defaultColor);
    }
    
	@Override public void update(Observable obs, Object arg) {
	
		TileModel model = (TileModel)obs;
		
		for(GameModel.Operation operation : model.getOperations()) {
			switch(operation) {
			case EmptyTileSelected:
			case PlayerPieceSelected:
				updateSelectedCommand(true);
				break;
			case PlayerPieceCancel:
			case EmptyTileCancel:
				updateSelectedCommand(false);
				break;
			default:
				break;
			}
		}
		
		if(_image == null) {
			PlayerModel player = model.getPlayer();
			if(player != null) {
				PlayerPiece piece = player.getPiece(model);
				if(piece != null) {
					_image = piece.getImage();
					repaint();
				}
			}
		}
		
		/*MouseListener[] mouseListeners = getListeners(MouseListener.class);
		for(MouseListener ml : mouseListeners) {
			ml.mouseEntered(null);
		}*/
	}
	
	@Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(hints);
        g2d.drawImage(_image, 16, 16, 32, 32, null, null);       
	}
	
	@Override public void render() {
		setBackground(_defaultColor);
	}
}