package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableFrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/***/
public class LoginView extends JDialog 
{

	/***/
	private LoginViewPanel loginViewPanel ;
	
	/***/
	public LoginView ( LoginViewObserver observer ) 
	{
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
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
		setAlwaysOnTop ( true ) ;
	}
	
	/***/
	public void prepareView () 
	{
		loginViewPanel.prepareView () ;
	}
	
	/***/
	interface LoginViewObserver extends Observer
	{
		
		/***/
		public void onNameEntered ( String enteredName ) ;
		
		/***/
		public void onDoNotWantToEnterName () ;
		
	}
	
}

/***/
class LoginViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < LoginViewObserver >
{

	private static final String EMPTY_NAME_ERROR_MESSAGE = "Un nome vuoto non va bene..." ;
	
	private static final String BACKGROUND_IMAGE_FILE_PATH = "sheepland_cover.jpg" ; ;
	
	/***/
	private Image backgroundImage ;
	
	/***/
	private JLabel textLabel ;
	
	/***/
	private JLabel errorLabel ;
	
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
	}
	
	/***/
	public void prepareView ()
	{
		nameField.setText ( Utilities.EMPTY_STRING ) ;
		errorLabel.setText ( Utilities.EMPTY_STRING ) ;
	}
	
	/***/
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight () , this ) ;
	}
	
	/***/
	@Override
	protected void createComponents ()  
	{
		try 
		{
			backgroundImage = GraphicsUtilities.getImage ( BACKGROUND_IMAGE_FILE_PATH ) ;
			textLabel = new JLabel () ;
			nameField = new JTextField () ;
			errorLabel = new JLabel () ;
			enterButton = new JButton () ;
			exitButton = new JButton () ;
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
	}
	
	/***/
	@Override
	protected void manageLayout () 
	{
		Insets insets ; 
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		layoutComponent ( textLabel , 0 , 0 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( nameField , 0 , 1 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( nameField , 0 , 2 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( enterButton , 0 , 3 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( exitButton , 1 , 3 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		textLabel.setText ( PresentationMessages.NAME_REQUEST_MESSAGE ) ;
		textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
		errorLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
		errorLabel.setForeground ( Color.RED ) ;
	}
	
	/***/
	@Override
	protected void bindListeners () 
	{
		Action okAction ;
		Action exitAction ;
		okAction = new OkAction ( "ENTRA" ) ;
		exitAction = new ExitAction ( "ESCI" )  ;
		enterButton.setAction ( okAction ) ;
		exitButton.setAction ( exitAction ) ;
	}
	
	/***/
	@Override
	protected void injectComponents () 
	{
		add ( textLabel ) ;
		add ( nameField ) ;
		add ( errorLabel ) ;
		add ( enterButton ) ;
		add ( exitButton ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	private class OkAction extends AbstractAction 
	{

		/***/
		public OkAction ( String frontEndText )  
		{
			super ( frontEndText ) ;
		}
		
		/***/
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				if ( nameField.getText ().compareToIgnoreCase ( Utilities.EMPTY_STRING ) == 0 )
					errorLabel.setText ( EMPTY_NAME_ERROR_MESSAGE ) ;
				else
					notifyObservers ( "onNameEntered" , nameField.getText () ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
		
	}
	
	/***/
	private class ExitAction extends AbstractAction 
	{

		/***/
		public ExitAction ( String frontEndText ) 
		{
			super ( frontEndText ) ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				notifyObservers ( "onDoNotWantToEnterName" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
				
	}
	
}