/**
* Daniel Ricci <2016> <thedanny09@gmail.com>
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

package game.models;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class GameModel extends Observable 
{
	private final Set<Operation> _operations = new HashSet<Operation>();
	
	public enum Operation {
		PlayerPieceSelected,
		PlayerPieceCancel,
		EmptyTileSelected, 
		EmptyTileCancel, 
		ShowGuides,
		HideGuides
	}
	
	protected GameModel(Observer... observer) {
		for(Observer obs : observer) {
			addObserver(obs);
		}
	}
	
	protected final void doneUpdating() {
		setChanged();
		notifyObservers(_operations);
		_operations.clear();
	}
	
	protected final void addOperation(Operation operation) { _operations.add(operation); }
	public final Set<Operation> getOperations() {
		return _operations;
	}
}