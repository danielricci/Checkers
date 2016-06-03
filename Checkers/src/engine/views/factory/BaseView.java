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

import java.util.Vector;

import javax.swing.JPanel;

import engine.controllers.IController;
import engine.views.IView;

@SuppressWarnings("serial")
public abstract class BaseView extends JPanel implements IView {

	private final Vector<IController> _controllers = new Vector<IController>();
	
	protected BaseView(IController... controllers) {			
		for(IController controller : controllers) {
			boolean found = false;
			for(IController _controller : _controllers) {
				if(_controller.getClass() == controller.getClass()) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				_controllers.add(controller);
			}
		}
	}

	protected final <T extends IController> IController getController(Class<T> controllerClass) {	
		IController myController = null;
		for(IController controller : _controllers) {
			if(controller.getClass() == controllerClass) {
				myController = controller;
				break;
			}
		}
		return myController;
	}
	
	@Override public abstract void render();
}