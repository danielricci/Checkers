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
import game.models.GameModel;
import game.models.GameModel.Operation;
import game.models.PlayerModel;
import game.models.TileModel;
import game.pieces.PlayerPiece;

@SuppressWarnings("serial")
public class TileView extends BaseView {

	private static final Color _defaultColor = Color.LIGHT_GRAY;
	private static final Color _selectedColor = Color.DARK_GRAY;
	private static final Color _guideColor = Color.BLUE;
	private static final Color _captureColor = Color.GREEN;
	
	private Image _image;
	
    @Override protected void registerListeners() {
    	
    	addMouseListener(new MouseAdapter() {  		
    		
    		@Override public void mouseEntered(MouseEvent event) {
    			//setBackground(_hoverColor);
			}
    		
    		@Override public void mouseExited(MouseEvent event) {
    			//setBackground(_defaultColor);
    		}

    		@Override public void mouseClicked(MouseEvent event) {
    			TileController controller = getController(TileController.class);
    			controller.event_mouseClicked();
			}
		});
    }
    
    private void updateSelectedCommand(Color color) {
    	setBackground(color);
    }
    
	@Override public void update(Observable obs, Object arg) {
		
		TileModel tileModel = (TileModel)obs;
		TileController tileController = getController(TileController.class);
		
		for(Operation operation : tileModel.getOperations()) {
			switch(operation) {
			case EmptyTileSelected:
				break;
			case PlayerPieceSelected:
				updateSelectedCommand(_selectedColor);
				tileController.tileGuidesCommand(tileModel, Operation.ShowGuides); // TODO - shouldn't this be done by the BoardGameController
				break;
			case PlayerPieceMoveCancel:
				updateSelectedCommand(_defaultColor);
				tileController.tileGuidesCommand(tileModel, Operation.HideGuides); // TODO - shouldn't this be done by the BoardGameController
				break;
			case PlayerPieceMoveAccepted:
				updateSelectedCommand(_defaultColor);
				break;
			case HideGuides:
				updateSelectedCommand(_defaultColor);
				break;
			case ShowGuides:
				updateSelectedCommand(_guideColor);
				break;
			default:
				refresh(tileModel);
				break;
			}
		}
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

		if(_image == null) {
			TileController controller = getController(TileController.class);
			_image = controller.getTileImage();
			repaint();
		}
	}
	
	@Override public void refresh(GameModel gameModel) {
		
		TileModel model = (TileModel)gameModel;
		PlayerModel player = model.getPlayer();

		if(player == null) {
			_image = null;
		}
		else {
			PlayerPiece piece = player.getPiece(model);
			if(piece != null) {
				_image = piece.getImage();				
			}
		}
		
		repaint();
	}
}