package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.FilePaths;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

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
		Image backgroundImage ;
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( inputView , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;
		inputView.setShowKo ( false ) ;	
		inputView.setTitle ( PresentationMessages.NAME_REQUEST_MESSAGE ) ;
		try 
		{
			backgroundImage = GraphicsUtilities.getImage ( FilePaths.COVER_IMAGE_PATH ) ;
			inputView.setBackgroundImage( backgroundImage ) ;
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		setUndecorated(true);
 		setSize ( GraphicsUtilities.getVGAResolution () ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
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
		inputView.setContentsPanel  ( loginViewPanel ) ;		
	}
	
	/**
	 * Preperare this LoginView for showing. 
	 */
	public void prepareView () 
	{
		loginViewPanel.prepareView () ;
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
			if ( name != null && name.compareTo ( Utilities.EMPTY_STRING ) != 0 )
				try 
				{
					notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , name ) ;
				}
				catch (MethodInvocationException e) 
				{
					e.printStackTrace();
				}
			else
				inputView.setErrors ( PresentationMessages.INVALID_CHOOSE_MESSAGE ) ;
		}
	}
	
	/***/
	public static String showDialog () 
	{
		LoginView loginView ;
		AtomicReference < String > name ;
		name = new AtomicReference < String > ( null ) ;
		loginView = new LoginView ( new DefaultLoginViewObserver ( name ) ) ;
		GraphicsUtilities.showUnshowWindow ( loginView , false , true ) ;
		synchronized ( name ) 
		{
			while ( name.get() == null )
				try 
				{
					name.wait () ;
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( loginView , false , false ) ;
		return name.get() ;
	}
	
}

/**
 * This class effectively implements the LoginView panel 
 */
class LoginViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

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
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void createComponents ()  
	{
		nameField = new JTextField () ;
	}	
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ; 
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		layoutComponent ( nameField , 0 , 0 , 1 , 0 , 1 , 1 , 0 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		nameField.setBorder ( new TitledBorder ( "Il tuo nome :" ) ) ;
		setOpaque ( false ) ;
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
	}
	
	/***/
	public String getName () 
	{
		String res ; 
		res = nameField.getText () ;
		return res ;
	}
	
}