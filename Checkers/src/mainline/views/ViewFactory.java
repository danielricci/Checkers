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

package mainline.views;

import java.util.Vector;

public abstract class ViewFactory {

	private static final Vector<IView> _views = new Vector<IView>(); 
	
	public enum ViewType {
		BoardGameView,
		MainWindowView
	}
	
	public static IView getView(ViewType viewType) {
		
		IView view = null;
		switch(viewType) {
			case BoardGameView: 
			{
				if((view = getView(BoardGameView.class)) != null) {
					return view;
				}
				view = BoardGameView.Create();
				break;
			}
			case MainWindowView:
			{
				if((view = getView(MainWindowView.class)) != null) {
					return view;
				}
				view = MainWindowView.Create();
				break;
			}
		}
		
		assert view != null : "Error: Cannot create a view of the specified type " + viewType.toString();
		_views.add(view);
		
		return view;
	}
	
	private static <T extends IView> IView getView(Class<T> viewClass) {
		for(IView view : _views) {
			if(view.getClass() == viewClass) {
				return view;
			}
		}
		
		return null;
	}
}