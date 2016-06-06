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

package game.controllers.factory;

import java.util.Vector;

import game.controllers.IController;

public class ControllerFactory {

	private static final Vector<IController> _controllers = new Vector<IController>(); 
	
	public enum ControllerType {
		BoardGameController,
		PlayerController
	}
	
	protected ControllerFactory() {
	}
	
	public static IController getController(ControllerType controllerType) {
		
		IController controller = null;
		switch(controllerType) {
			case BoardGameController: 
			{
				if((controller = getController(BoardGameController.class)) != null) {
					return controller;
				}
				controller = new BoardGameController();
				break;
			}
			case PlayerController:
			{
				if((controller = getController(PlayerController.class)) != null) {
					return controller;
				}
				controller = new PlayerController();
				break;
			}
		}
				
		assert controller != null : "Error: Cannot create a controller of the specified type " + controllerType.toString();
		_controllers.add(controller);
		
		return controller;
	}
	
	private static <T extends IController> IController getController(Class<T> controllerClass) {
		for(IController controller : _controllers) {
			if(controller.getClass() == controllerClass) {
				return controller;
			}
		}
		
		return null;
	}
}
