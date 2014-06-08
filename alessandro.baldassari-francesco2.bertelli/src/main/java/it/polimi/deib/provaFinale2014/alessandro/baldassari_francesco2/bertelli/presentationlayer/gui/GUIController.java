package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.MessageView.MessageViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorRequestView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

/**
 * This is the Component that manage all the GUI infrastructure.
 * It is a Controller in the sense of the MVC pattern : it shows windows, handle gui events and so on. 
 */
public class GUIController extends ViewPresenter 
	implements MessageViewObserver , LoginViewObserver , SheperdColorRequestViewObserver
{
	
	/**
	 * Identifier for the WaitingView. 
	 */
	private static final String WAITING_VIEW_KEY = "WAITING" ;
	
	/**
	 * Identifier for the MessageView. 
	 */
	private static final String MESSAGE_VIEW_KEY = "MESSAGE" ;
	
	/**
	 * Identifier for the LoginView. 
	 */
	private static final String LOGIN_VIEW_KEY = "LOGIN" ;
	
	/**
	 * Identifier for the GameView. 
	 */
	private static final String GAME_VIEW_KEY = "GAME" ;
	
	/**
	 * A Map containing the windows used in the App. 
	 */
	private final Map < String , Window > views ;
	
	/**
	 * A reference for the current shown window. 
	 */
	private Window currentShownWindow ;
	
	/**
	 * A flag that indicates if the User expressed the will to exit the program. 
	 */
	private AtomicBoolean wantExit ;
	
	/**
	 * Synch variable for the name input process. 
	 */
	private final AtomicReference < String > name ;
	
	/**
	 * Synch variable for the color input process. 
	 */
	private final AtomicReference < NamedColor > color ;
	
	/***/
	public GUIController () 
	{
		super () ;
		views = new HashMap < String , Window > () ;
		currentShownWindow = null ;
		wantExit = new AtomicBoolean () ;
		name = new AtomicReference < String > () ;
		color = new AtomicReference < NamedColor > () ;
		wantExit.set ( false ) ;
		SwingUtilities.invokeLater ( new GUIElementsCreatorRunnable () ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void startApp () 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				WaitingView waitingView ;
				while ( views.get ( WAITING_VIEW_KEY ) == null ) ;
				waitingView = ( WaitingView ) views.get ( WAITING_VIEW_KEY ) ;
				currentShownWindow = waitingView ;
				waitingView.setText ( PresentationMessages.WELCOME_MESSAGE + "\n" + PresentationMessages.SERVER_CONNECTION_MESSAGE ) ;
				waitingView.setVisible ( true ) ;
			} 
		} );
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void onTermination () throws IOException 
	{
		MessageView messageView ;
		while ( views.get ( MESSAGE_VIEW_KEY ) == null ) ;
		messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
		currentShownWindow.setVisible ( false ) ;
		currentShownWindow = messageView ;
		messageView.setMessage ( PresentationMessages.BYE_MESSAGE ) ;
		messageView.setVisible ( true ) ;
		for ( Window w : views.values () )
		{
			w.setVisible ( false ) ;
			w.dispose () ;
		}
	}	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String onNameRequest () 
	{
		String res ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				LoginView loginView ;
				while ( views.get ( LOGIN_VIEW_KEY ) == null ) ;
				loginView = ( LoginView ) views.get ( LOGIN_VIEW_KEY ) ;
				currentShownWindow.setVisible ( false ) ;
				currentShownWindow = loginView ;
				loginView.prepareView () ;
				name.set ( null ) ;
				loginView.setVisible ( true ) ;
			} 
		} );
		while ( name.get () == null ) ;
		if ( wantExit.get () == false )
		{
			res = name.get () ;
			SwingUtilities.invokeLater ( new Runnable () 
			{ 
				public void run () 
				{
					WaitingView waitingView ;
					waitingView = ( WaitingView ) views.get ( WAITING_VIEW_KEY ) ;
					currentShownWindow.setVisible ( false ) ;
					currentShownWindow = waitingView ;
					waitingView.setText ( PresentationMessages.NAME_VERIFICATION_MESSAGE ) ;
					waitingView.setVisible ( true ) ;
					name.set(null);
				} 
			} );
		}
		else
			res = null ;
		
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantToEnterName () 
	{
		wantExit.set ( true ) ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				stopApp () ;
			} 
		} );
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameRequestAck ( boolean isOk , final String notes ) 
	{
		Runnable r ;
		if ( isOk )
			r = new Runnable () 
			{ 
				public void run () 
				{
					MessageView messageView ;
					WaitingView waitingView ;
					messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
					currentShownWindow.setVisible ( false ) ;
					currentShownWindow = messageView ;
					messageView.setMessage ( PresentationMessages.NAME_ACCEPTED_MESSAGE + "\n" + notes );
					messageView.setVisible ( true ) ;
					waitingView = ( WaitingView ) views.get ( WAITING_VIEW_KEY ) ;
					waitingView.setText ( PresentationMessages.WAITING_FOR_OTHER_PLAYERS_MESSAGE ) ;
					currentShownWindow = messageView ;
					messageView.setVisible ( true ) ;
				} 
			} ;
		else
			r = new Runnable () 
			{ 
				public void run () 
				{
					MessageView messageView ;
					messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
					currentShownWindow.setVisible ( false ) ;
					currentShownWindow = messageView ;
					messageView.setMessage ( PresentationMessages.NAME_REJECTED_MESSAGE + "\n" + notes );
					messageView.setVisible ( true );
				} 
			} ;
		SwingUtilities.invokeLater ( r ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNotifyMatchStart () 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				MessageView messageView ;
				GameView gameView ;
				messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
				currentShownWindow.setVisible ( false ) ;
				currentShownWindow = messageView ;
				messageView.setMessage ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
				messageView.setVisible ( true ) ;
				gameView = ( GameView ) views.get ( GAME_VIEW_KEY ) ;
				currentShownWindow.setVisible ( false ) ;
				currentShownWindow = gameView ;
				gameView.setVisible ( true ) ;
			} 
		} ) ;
	}
	
	@Override
	public void onMatchWillNotStartNotification ( final String msg ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				MessageView messageView ;
				messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
				currentShownWindow.setVisible ( false ) ;
				messageView.setMessage ( PresentationMessages.MATCH_WILL_NOT_START_MESSAGE + "\n" + msg ) ;
				messageView.setVisible ( true ) ;
				stopApp () ;
			} 
		} ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void generationNotification ( final String msg ) 
	{
		SwingUtilities.invokeLater ( new Runnable () { 
			public void run () 
			{
				MessageView messageView ;
				Window oldWindow ;
				messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
				oldWindow = currentShownWindow ;
				currentShownWindow.setVisible ( false ) ;
				currentShownWindow = messageView ;
				messageView.setMessage ( msg ) ;
				messageView.setVisible ( true ) ;
				currentShownWindow = oldWindow ;
				currentShownWindow.setVisible ( true ) ;
			} 
		} ) ;
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMessageRead () 
	{
		MessageView messageView ;
		messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
		messageView.setVisible ( false ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameEntered ( String enteredName ) 
	{
		name.set ( enteredName ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( final Iterable < NamedColor > availableColors ) 
	{
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						//sheperdColorRequestView = new SheperdColorRequestView ( GUIController.this , availableColors ) ;
						//gameView.setVisible(false);
						//sheperdColorRequestView.setVisible ( true ) ;
					} 
				} 
									);
		while ( color.get () == null ) ;
		return color.get() ;
	}

	@Override
	public Road chooseInitRoadForSheperd(Iterable<Road> availableRoads)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}		
	
	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > playerCards ) 
	{
		return null ;
	}

	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > sheperds )
	{
		return null ;
	}

	@Override
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > acquirables ) 
	{
		return null ;
		
	}

	@Override
	public GameMove onDoMove ( MoveFactory f , GameMap m ) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onColorChoosed ( NamedColor selectedColor ) 
	{
		color.set ( selectedColor ) ;
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						
					} 
				} 
									);
	}
	
	// INNER CLASSES
	
	/**
	 * Runnable object for the StartApp event. 
	 */
	private class GUIElementsCreatorRunnable implements Runnable 
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			views.put ( WAITING_VIEW_KEY , new WaitingView () ) ;
			views.put ( MESSAGE_VIEW_KEY , new MessageView ( GUIController.this ) ) ;
			views.put ( LOGIN_VIEW_KEY , new LoginView ( GUIController.this ) ) ;
			views.put ( GAME_VIEW_KEY , new GameView () ) ;
			views.get ( "WAITING" ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( "MESSAGE" ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( "LOGIN" ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			( ( GameView ) views.get ( GAME_VIEW_KEY ) ).setExtendedState ( Frame.MAXIMIZED_BOTH ) ;
			//views.put ( "SHEPERDS_COLOR_REQUEST" , new SheperdColorRequestView ( GUIController.this ) ) ;
		}
		
	}

	
}
