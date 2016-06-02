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

package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import engine.views.IView;
import engine.views.factory.ViewFactory;
import engine.views.factory.ViewFactory.ViewType;

@SuppressWarnings("serial")
public final class WindowManager extends JFrame {
	
	private static WindowManager _instance;	
	
	private WindowManager() {
		super("Checkers");
		
		Dimension _windowSize = new Dimension(800, 800);
		setSize(_windowSize);
		setResizable(false);
				
		// Set the location of the window to be in middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(
			screenSize.width / 2 - _windowSize.width / 2,
			screenSize.height / 2 - _windowSize.height / 2
		);

		SetWindowedInstanceListeners();
		SetWindowedInstanceMenu();
	}
	
	public static WindowManager getInstance() {
		if(_instance == null) {
			_instance = new WindowManager();
		}
		return _instance;
	}
	
	private void SetWindowedInstanceListeners() {
		
		// Needed to manually handle closing of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Add a listener to whenever the window is closed
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				int response= JOptionPane.showConfirmDialog(null, "Are you sure that you wish to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
	}
	
	private void SetWindowedInstanceMenu() {
		JMenuBar menu = new JMenuBar();
		PopulateFileMenu(menu);
		setJMenuBar(menu);
	}
	
	private void PopulateFileMenu(JMenuBar menu) {

		// Create the file menu 
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
			        
        // Set the event handler
        JMenuItem fileMenuNew = new JMenuItem(new AbstractAction("New") {   	
			@Override public void actionPerformed(ActionEvent event) {	
        		int response= JOptionPane.showConfirmDialog(null, "Starting a new game will cancel any current game in progress, are you sure?", "New Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
	        		getContentPane().removeAll();
        			IView mainWindowView = ViewFactory.getView(ViewType.MainWindowView);
        			mainWindowView.render();
        			add((Component) mainWindowView);
        			validate();						
				}
			}	
        });

        // Set the shortcut
        fileMenuNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileMenuNew);
        fileMenu.addSeparator();
        
        // Set the event handler
        JMenuItem fileMenuExit = new JMenuItem(new AbstractAction("Exit") {
        	@Override public void actionPerformed(ActionEvent event) {
        		_instance.dispatchEvent(new WindowEvent(_instance, WindowEvent.WINDOW_CLOSING));
			}	
		});
       
        fileMenu.add(fileMenuExit);
        menu.add(fileMenu);
	}	
}