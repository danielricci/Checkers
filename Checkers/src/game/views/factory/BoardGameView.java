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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import game.controllers.factory.ControllerFactory;
import game.controllers.factory.ControllerFactory.ControllerType;
import game.controllers.factory.GameTileController;
import game.controllers.factory.PlayerController;
import game.external.EngineHelper;
import game.models.PlayerModel;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		PlayerController controller = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		controller.populatePlayers(this);
	}

	@Override public void render() {
	
		_gamePanel.setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		PlayerController playerController = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
    	
		for (int row = 0; row < 12; ++row) {
			
			PlayerModel.Team team = null;
			if(EngineHelper.isBetweenOrEqual(row, 0, 4)) {
				team = PlayerModel.Team.PlayerX;				
			} else if(EngineHelper.isBetweenOrEqual(row, 7, 11)) {
				team = PlayerModel.Team.PlayerY;
			}
			
			PlayerModel player = playerController.getPlayer(team);
			for (int col = 0, colorOffset = (row % 2 == 0 ? 0 : 1); col < 12; ++col) {
				
				// determine if we should render our game tile for this cell
				boolean shouldRender = (col + colorOffset) % 2 == 0 ? false : true;
				if(!shouldRender) {
					continue;
				}
				
				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;
				
				// Create the game tile and add it to our view
				GameTileView view = new GameTileView();
				view.setController(new GameTileController(view, player));
				view.render();
								
				_gamePanel.add(view, gbc);
			}
		}
		
		add(_gamePanel);
	}

	@Override protected void registerListeners() {		
	}
}