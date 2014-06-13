package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.DataFilePaths;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This View implements the User Interface for the Login phase, the process where a Player has to 
 * enter a name. 
 */
public class LoginView extends ObservableFrameworkedWithGridBagLayoutDialog < LoginViewObserver > 
{
	
	/**
	 * The effective view. 
	 */
	private InputView inputView ;
	
	/**
	 * The View where effectively input data. 
	 */
	private LoginViewPanel loginViewPanel ;
	
	/**
	 * @param observer an observer for this view.
	 */
	public LoginView ( LoginViewObserver observer ) 
	{
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		addObserver ( observer ) ;		
		System.out.println ( "LOGIN_VIEW : CONSTRUCTOR_EXECUTED" ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		inputView = new InputView () ;
		loginViewPanel = new LoginViewPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( inputView , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;
		inputView.setShowKo ( false ) ;	
		pack () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		inputView.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( inputView ) ;
		inputView.setContentsPanel ( loginViewPanel ) ;		
	}
	
	/**
	 * Class to manage the ok action. 
	 */
	private class OkAction implements Runnable 
	{
		
		private final String NAME_OF_THE_METHOD_TO_CALL = "onNameEntered" ;
		
		/**
		 * As the super's one. 
		 */
		@Override
		public void run () 
		{
			String name ;
			name = loginViewPanel.getName () ;
			if ( name != null && name.compareTo ( Utilities.EMPTY_STRING ) == 0 )
				try 
				{
					notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , name ) ;
				}
				catch (MethodInvocationException e) 
				{
					e.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( LoginView.this , PresentationMessages.INVALID_CHOOSE_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE ) ;
		}
		
	}
	
	/**
	 * Preperare this LoginView for showing. 
	 */
	public void prepareView () 
	{
		loginViewPanel.prepareView () ;
	}
	
	/**
	 * This interface defines the events a LoginViewObserver can listen to. 
	 */
	public interface LoginViewObserver extends Observer
	{
		
		/**
		 * Called when the User choosed and confirmed his name. 
		 */
		public void onNameEntered ( String enteredName ) ;
		
		/**
		 * Called when the user does not want to complete this operation. 
		 */
		public void onDoNotWantToEnterName () ;
		
	}
	
	private static AtomicReference <String> name ;
	
	public static String showDialog () 
	{
		LoginView l ;
		l = new LoginView ( new DummyObs () ) ;
		name = new AtomicReference < String > ( null ) ;
		GraphicsUtilities.showUnshowWindow ( l , false , true ) ;
		synchronized ( name ) 
		{
			while ( name.get() == null )
				try {
					name.wait () ;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( l , false , false ) ;
		return name.get();
	}
	
	private static class DummyObs implements LoginViewObserver 
	{

		@Override
		public void onNameEntered ( String enteredName ) 
		{
			synchronized (name) {
				name.set(enteredName); 
				name.notifyAll();
			}
			
		}

		@Override
		public void onDoNotWantToEnterName() {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
	public static void main ( String [] args ) 
	{
		String x ;
		x = LoginView.showDialog();
		System.out.println ( x ) ;
	}
	
}

/**
 * This class effectively implements the LoginView panel 
 */
class LoginViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/***/
	private static final String EMPTY_NAME_ERROR_MESSAGE = "Un nome vuoto non va bene..." ;
	
	/**
	 * The background image for this View. 
	 */
	private Image backgroundImage ;
	
	/**
	 * A label to show if the value entered is not ok. 
	 */
	private JLabel errorLabel ;
	
	/***/
	private JTextField nameField ;
	
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
		errorLabel.setVisible ( false ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void createComponents ()  
	{
		nameField = new JTextField () ;
		errorLabel = new JLabel () ;
		try 
		{
			backgroundImage = GraphicsUtilities.getImage ( DataFilePaths.BACKGROUND_IMAGE_FILE_PATH ) ;
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}	
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ; 
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		layoutComponent ( nameField , 0 , 1 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( errorLabel , 0 , 2 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		errorLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
		errorLabel.setForeground ( Color.RED ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void bindListeners () {}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void injectComponents () 
	{
		add ( nameField ) ;
		add ( errorLabel ) ;
	}
	
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent(g); 
		if ( backgroundImage != null )
			g.drawImage ( backgroundImage , 0 , 0 , getWidth () , getHeight() , this );
	}
	
	/***/
	public String getName () 
	{
		String res ; 
		res = nameField.getText () ;
		if ( res == null || res.compareToIgnoreCase ( Utilities.EMPTY_STRING ) == 0 )
		{
			errorLabel.setText ( EMPTY_NAME_ERROR_MESSAGE ) ;
			errorLabel.setVisible ( true ) ;
		}
		return res ;
	}
	
}
