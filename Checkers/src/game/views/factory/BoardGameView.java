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
import game.controllers.factory.PlayerController;
import game.controllers.factory.TileController;
import game.external.EngineHelper;
import game.models.GameModel;
import game.models.PlayerModel;
import game.models.TileModel;
import game.models.TileModel.NeighborPosition;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		PlayerController controller = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		controller.populatePlayers(this);
	}

	@Override public void update(Observable obs, Object arg) {
		
		BoardGameController boardGameController = (BoardGameController) ControllerFactory.getController(ControllerType.BoardGameController);
		TileModel tileModel = (TileModel)obs;
		
		for(GameModel.Operation operation : tileModel.getOperations()) {
			switch(operation) {
			case PlayerPieceSelected:
				boardGameController.addTileModelOnSelected(tileModel);
				break;
			case PlayerPieceMoveCancel:
				boardGameController.processTileCancel(tileModel);
				break;
			case PlayerPieceMoveAccepted:
				boardGameController.processTileMove(tileModel);
				PlayerController controller = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
				controller.nextPlayer();
				break;
			}
		}
	};
	
	@Override public void render() {
	
		_gamePanel.setBackground(Color.BLACK);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		PlayerController playerController = (PlayerController) ControllerFactory.getController(ControllerType.PlayerController);
		BoardGameController boardGameController = (BoardGameController) ControllerFactory.getController(ControllerType.BoardGameController);

		int boardDimensions = boardGameController.getBoardDimensions();
		
		Vector<Vector<TileModel>> tiles = new Vector<Vector<TileModel>>();
		for (int row = 0; row < boardDimensions; ++row) {
			
			PlayerModel player = null;
			if(EngineHelper.isBetweenOrEqual(row, 0, 4)) {
				player = playerController.getPlayer(0);
			} else if(EngineHelper.isBetweenOrEqual(row, 7, 11)) {
				player = playerController.getPlayer(1);
			}
			
			Vector<TileModel> tilesRow = new Vector<TileModel>();

			for (int col = 0, colorOffset = (row % 2 == 0 ? 0 : 1); col < 12; ++col) {
				
				// determine if we should render our game tile for this cell
				boolean shouldRender = (col + colorOffset) % 2 == 0 ? false : true;
				if(!shouldRender) {
					continue;
				}
				
				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;

				TileView view = new TileView();
				TileModel tile = boardGameController.populateTile(player, view, this);
				view.setController(new TileController(tile));
				view.render();
				
				_gamePanel.add(view, gbc);			
				tilesRow.add(tile);
			}

			if(!tiles.isEmpty()) {
				// Get the last row that has been rendered and link them together by 
				// reference each others top and bottom.  Once this block gets executed
				// they will be able to reference each other as neighbors
				Vector<TileModel> previous = tiles.get(tiles.size() - 1);
				for(int index = 0, oddRow = index, evenRow = index + 1; 
					index < previous.size(); 
					++index, oddRow = index - 1, evenRow = Math.min(index + 1, previous.size() - 1)) 
				{
					if(row % 2 == 0) {
						
						TileModel[] neighbors = new TileModel[] {
							previous.get(index),
							evenRow == index ? previous.get(index) : previous.get(evenRow)
						};
					
						tilesRow.get(index).setNeighbors(
							NeighborPosition.TOP,
							neighbors
						);
						
						for(TileModel neighbor : neighbors) {
							neighbor.setNeighbors(NeighborPosition.BOTTOM, tilesRow.get(index));
						}
					}
					else {
						
						TileModel[] neighbors = new TileModel[] {
							previous.get(index),
							oddRow == index ? previous.get(index) : previous.get(oddRow)
						};
						
						tilesRow.get(index).setNeighbors(
							NeighborPosition.TOP,
							neighbors
						);
						
						for(TileModel neighbor : neighbors) {
							neighbor.setNeighbors(NeighborPosition.BOTTOM, tilesRow.get(index));
						}
					}
				}
			}
			tiles.add(tilesRow);
		}
		
		add(_gamePanel);
	}
}