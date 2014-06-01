package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginView extends JDialog 
{

	private LoginViewPanel loginViewPanel ;
	
	public LoginView ( LoginViewObserver observer ) 
	{
		super ( ( Frame ) null , "JSheepland - Login" , true ) ;
		GridBagLayout g ;
		Insets insets ;
		loginViewPanel = new LoginViewPanel () ;
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( loginViewPanel , g , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		loginViewPanel.addObserver ( observer ) ;
		setLayout ( g ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		add ( loginViewPanel ) ;
		setSize ( GraphicsUtilities.getVGAResolution () ) ;
		pack () ;
		setResizable ( false ) ;
	}
	
	interface LoginViewObserver extends Observer
	{
		
		public void onEnter () ;
		
		public void onExit () ;
		
	}
	
}

/***/
class LoginViewPanel extends ObservableWithGridBagLayoutPanel < LoginViewObserver >
{

	private final String BACKGROUND_IMAGE_FILE_PATH ;
	
	/***/
	private Image backgroundImage ;
	
	/***/
	private JLabel textLabel ;
	
	/***/
	private JTextField nameField ;
	
	/***/
	private JButton enterButton ;
	
	/***/
	private JButton exitButton ;
	
	/***/
	public LoginViewPanel () 
	{
		super () ;
		try 
		{
			BACKGROUND_IMAGE_FILE_PATH = "sheepland_cover.jpg" ;
			createCocmponents () ;
			manageLayout () ;
			bindListeners () ;
			injectComponents () ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
		
	}
	
	/***/
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}
	
	/***/
	private void createCocmponents () throws IOException 
	{
		textLabel = new JLabel () ;
		nameField = new JTextField () ;
		enterButton = new JButton () ;
		exitButton = new JButton () ;
		backgroundImage = GraphicsUtilities.getImage ( BACKGROUND_IMAGE_FILE_PATH ) ;
	}
	
	/***/
	private void manageLayout () 
	{
		Insets insets ; 
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		layoutComponent ( textLabel , 0 , 0 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( nameField , 0 , 1 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( enterButton , 0 , 2 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( exitButton , 1 , 2 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		textLabel.setText ( "Prego, inserisci il nome con cui vuoi giocare a JSheepland" ) ;
		textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
	}
	
	/***/
	private void bindListeners () 
	{
		Action okAction ;
		Action exitAction ;
		okAction = new OkAction ( "ENTRA" ) ;
		exitAction = new ExitAction ( "ESCI" )  ;
		enterButton.setAction ( okAction ) ;
		exitButton.setAction ( exitAction ) ;
	}
	
	/***/
	private void injectComponents () 
	{
		add ( textLabel ) ;
		add ( nameField ) ;
		add ( enterButton ) ;
		add ( exitButton ) ;
	}
	
	/***/
	private class OkAction extends AbstractAction 
	{

		public OkAction ( String frontEndText )  
		{
			super ( frontEndText ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				LoginViewPanel.this.notifyObservers ( "onEnter" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
		
	}
	
	private class ExitAction extends AbstractAction 
	{

		public ExitAction ( String frontEndText ) 
		{
			super ( frontEndText ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				LoginViewPanel.this.notifyObservers ( "onExit" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
				
	}
	
	
}