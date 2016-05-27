package mainline;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import mainline.controllers.BoardGameController;
import mainline.controllers.MainWindowController;

@SuppressWarnings("serial")
public final class WindowInstance extends JFrame {
	
	private static WindowInstance _instance = null;

	private final ArrayList<Object> _controllers = new ArrayList<Object>();
	private final JMenuBar _menu = new JMenuBar();
	private Dimension _windowSize = new Dimension(400, 400);
	
	private WindowInstance() {
		super("Checkers");
		setSize(_windowSize);
		setMinimumSize(new Dimension(400, 400));
		setUndecorated(false);
		
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		
		// Set the location of the window to be in middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(
			screenSize.width / 2 - _windowSize.width / 2,
			screenSize.height / 2 - _windowSize.height / 2
		);

		SetWindowedInstanceListeners();
		SetWindowedInstanceMenu();
	}
	
	public void registerController(Object object) {
		if(!_controllers.contains(object)) {
			_controllers.add(object);
		}
	}
	
	public Object getController(String type) {
		for(Object controller : _controllers) {
			String split[] = controller.getClass().getName().split("\\.");
			if(split[split.length - 1].equals(type)) {
				return controller;
			}
		}
		return null;
	}

	public static WindowInstance getInstance() {
		if(_instance == null) {
			_instance = new WindowInstance();
		}
		return _instance;
	}

	
	
	private void SetWindowedInstanceListeners() {
		
		// Needed to manually handle closing of the window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				WindowInstance windowInstance = (WindowInstance)e.getSource();
				Dimension newDimensions = windowInstance.getSize();
				
				if(newDimensions.width == _windowSize.width && newDimensions.height == _windowSize.height)
				{
					return;
				}
					
				// Resize the window uniformly
				if(newDimensions.width != _windowSize.width)
				{
					_windowSize = new Dimension(newDimensions.width, newDimensions.width);
				}
				else
				{
					_windowSize = new Dimension(newDimensions.height, newDimensions.height);
				}

				windowInstance.setSize(_windowSize); 
				System.out.println(_windowSize.getSize());
				//windowInstance.repaint();
			}
		});			
		
		
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
		 PopulateFileMenu(_menu);
		 PopulateOptionsMenu(_menu);
		 setJMenuBar(_menu);
	}
	
	private void PopulateFileMenu(JMenuBar menu) {

		// Create the file menu 
		JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
			        
        // Set the event handler
        JMenuItem fileMenuNew = new JMenuItem(new AbstractAction("New") { 
        	
			@Override
        	public void actionPerformed(ActionEvent event) {	
        		int response= JOptionPane.showConfirmDialog(null, "Starting a new game will cancel any current game in progress, are you sure?", "New Game", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.YES_OPTION) {
	
					if(_controllers.size() > 0)
					{
						BoardGameController controller = (BoardGameController)getController("BoardGameController");
						controller.reload();						
					}
					else
					{
		        		// Clears all references to our controller that this
		        		// instance may hold
		        		_controllers.clear();
		        		
		        		// Removes any lingering panels without having to worry
		        		// about who owns what
		        		getContentPane().removeAll();
		        		
		        		// Create a new controller and start the game
		        		MainWindowController controller = new MainWindowController(getInstance());
		        		registerController(controller);
		        		
		        		controller.startGame();
		        		
	        			validate();						
					}
	
				}
			}	
        });

        // Set the shortcut
        fileMenuNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(fileMenuNew);
        fileMenu.addSeparator();
        
        // Set the event handler
        JMenuItem fileMenuExit = new JMenuItem(new AbstractAction("Exit") {
        	
        	@Override
        	public void actionPerformed(ActionEvent event) {
        		_instance.dispatchEvent(new WindowEvent(_instance, WindowEvent.WINDOW_CLOSING));
			}	
		});
        fileMenu.add(fileMenuExit);
        menu.add(fileMenu);
	}	
	
	private void PopulateOptionsMenu(JMenuBar menu) {
		// Create the file menu 
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic('O');
        
        // Set the event handler
        JMenuItem optionsMenuRepository = new JMenuItem(new AbstractAction("Reset Score") {
        	
        	@Override
			public void actionPerformed(ActionEvent event) {
        		BoardGameController controller = ((BoardGameController)getController("BoardGameController"));
        		if(controller != null)
        		{
        			controller.resetScore();			
        		}
			}	
        });
        optionsMenu.add(optionsMenuRepository);
        
        menu.add(optionsMenu);
	}
}