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
import java.util.Observable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import game.controllers.factory.BoardGameController;
import game.controllers.factory.ControllerFactory;
import game.controllers.factory.ControllerFactory.ControllerType;
import game.controllers.factory.DiagonalTileController;
import game.controllers.factory.PlayerController;
import game.external.EngineHelper;
import game.models.DiagonalTileModel;
import game.models.PlayerModel;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		PlayerController controller = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		controller.populatePlayers(this);
	}

	@Override public void update(Observable obs, Object arg) {
	};
	
	@Override public void render() {
	
		_gamePanel.setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		PlayerController playerController = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		BoardGameController boardGameController = (BoardGameController) ControllerFactory.getController(ControllerType.BoardGameController);

		Vector<Vector<DiagonalTileModel>> tiles = new Vector<Vector<DiagonalTileModel>>();
		for (int row = 0; row < 12; ++row) {
			
			PlayerModel.Team team = null;
			if(EngineHelper.isBetweenOrEqual(row, 0, 4)) {
				team = PlayerModel.Team.PlayerX;				
			} else if(EngineHelper.isBetweenOrEqual(row, 7, 11)) {
				team = PlayerModel.Team.PlayerY;
			}
			
			PlayerModel player = playerController.getPlayer(team);
			Vector<DiagonalTileModel> tilesRow = new Vector<DiagonalTileModel>();

			for (int col = 0, colorOffset = (row % 2 == 0 ? 0 : 1); col < 12; ++col) {
				
				// determine if we should render our game tile for this cell
				boolean shouldRender = (col + colorOffset) % 2 == 0 ? false : true;
				if(!shouldRender) {
					continue;
				}
				
				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;

				// TODO --------- REFACTOR / SEPERATION OF CONCERNS
				DiagonalTileView view = new DiagonalTileView();
				DiagonalTileModel tile = boardGameController.populateTile(player, view, this);
				view.setController(new DiagonalTileController(tile));
				view.render();
				
				// Add our view to the game panel w.r.t the grid-constraints
				_gamePanel.add(view, gbc);
				

				/* Populate neighbor row association
				if(!tilesRow.isEmpty()) {
					tilesRow.get(tilesRow.size() - 1).setNeighbor(NeighborPosition.RIGHT, tile);
					tile.setNeighbor(NeighborPosition.LEFT, tilesRow.get(tilesRow.size() - 1));
				}
				*/
				tilesRow.add(tile);
				// TODO END REFACTOR / SEPERATION OF CONCERNS
			}

			if(!tiles.isEmpty()) {
				// Get the last row that has been rendered and link them together by 
				// reference each others top and bottom.  Once this block gets executed
				// they will be able to reference each other as neighbors
				Vector<DiagonalTileModel> previous = tiles.get(tiles.size() - 1);
				for(int i = 0; i < previous.size(); ++i) {
					// TODO - we need to fill this in now!
					//tilesRow.get(i).setNeighbor(NeighborPosition.TOP, previous.get(i));
					//previous.get(i).setNeighbor(NeighborPosition.BOTTOM, tilesRow.get(i));
				}
			}
			tiles.add(tilesRow);
		}
		
		add(_gamePanel);
	}

	@Override protected void registerListeners() {		
	}
}