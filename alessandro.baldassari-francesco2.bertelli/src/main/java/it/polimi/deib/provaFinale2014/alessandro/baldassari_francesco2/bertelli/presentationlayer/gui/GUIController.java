package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
	 * A component to manage thread issues. 
	 */
	private ExecutorService executorService ;
	
	/**
	 * A Map containing the windows used in the App. 
	 */
	private final Map < String , Window > views ;
	
	/**
	 * A reference for the current shown window. 
	 */
	private Window currentShownWindow ;
	
	/**
	 * A flag that indicates that the User has read the last displayed message.
	 * Useful for view synchronization. 
	 */
	private boolean messageRead ;
	
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
		executorService = Executors.newCachedThreadPool() ;
		views = new HashMap < String , Window > () ;
		currentShownWindow = null ;
		wantExit = new AtomicBoolean () ;
		name = new AtomicReference < String > () ;
		color = new AtomicReference < NamedColor > () ;
		messageRead = true ;
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
				while ( getView ( WAITING_VIEW_KEY ) == null ) ;
				waitingView = ( WaitingView ) getView ( WAITING_VIEW_KEY ) ;
				currentShownWindow = waitingView ;
				waitingView.setText ( PresentationMessages.WELCOME_MESSAGE + "\n" + PresentationMessages.SERVER_CONNECTION_MESSAGE ) ;
				showView ( waitingView , false ) ;
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
		while ( getView ( MESSAGE_VIEW_KEY ) == null ) ;
		messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
		unshowView ( currentShownWindow ) ;
		currentShownWindow = messageView ;
		messageView.setMessage ( PresentationMessages.BYE_MESSAGE ) ;
		showView ( messageView , true ) ;
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
				while ( getView ( LOGIN_VIEW_KEY ) == null ) ;
				loginView = ( LoginView ) getView ( LOGIN_VIEW_KEY ) ;
				unshowView ( currentShownWindow ) ;
				currentShownWindow = loginView ;
				loginView.prepareView () ;
				name.set ( null ) ;
				showView ( loginView , false ) ;
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
					waitingView = ( WaitingView ) getView ( WAITING_VIEW_KEY ) ;
					unshowView ( currentShownWindow ) ;
					currentShownWindow = waitingView ;
					waitingView.setText ( PresentationMessages.NAME_VERIFICATION_MESSAGE ) ;
					showView ( waitingView , false ) ;
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
					messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
					unshowView ( currentShownWindow ) ;
					currentShownWindow = messageView ;
					messageView.setMessage ( PresentationMessages.NAME_ACCEPTED_MESSAGE + "\n" + notes );
					showView ( messageView , true ) ;
					waitingView = ( WaitingView ) getView ( WAITING_VIEW_KEY ) ;
					waitingView.setText ( PresentationMessages.WAITING_FOR_OTHER_PLAYERS_MESSAGE ) ;
					currentShownWindow = waitingView ;
					showView ( waitingView , false ) ;
				} 
			} ;
		else
			r = new Runnable () 
			{ 
				public void run () 
				{
					MessageView messageView ;
					messageView = ( MessageView ) views.get ( MESSAGE_VIEW_KEY ) ;
					unshowView ( currentShownWindow ) ;
					currentShownWindow = messageView ;
					messageView.setMessage ( PresentationMessages.NAME_REJECTED_MESSAGE + "\n" + notes );
					showView ( messageView , true ) ;
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
				messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
				unshowView ( currentShownWindow ) ;
				currentShownWindow = messageView ;
				messageView.setMessage ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
				showView ( messageView , true ) ;
				gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
				currentShownWindow = gameView ;
				showView ( gameView , false ) ;
			} 
		} ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMatchWillNotStartNotification ( final String msg ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				MessageView messageView ;
				messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
				unshowView ( currentShownWindow ) ;
				messageView.setMessage ( PresentationMessages.MATCH_WILL_NOT_START_MESSAGE + "\n" + msg ) ;
				showView ( messageView , true ) ;
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
		System.out.println ( "GUI_CONTROLLER - GENERIC_NOTIFICATION : INIZIO" ) ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				/*MessageView messageView ;
				Window oldWindow ;
				messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
				oldWindow = currentShownWindow ;
				unshowView ( currentShownWindow ) ;
				currentShownWindow = messageView ;
				messageView.setMessage ( msg ) ;
				showView ( messageView , true ) ;
				currentShownWindow = oldWindow ;
				showView ( currentShownWindow , false ) ;*/
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
		messageView = ( MessageView ) getView ( MESSAGE_VIEW_KEY ) ;
		messageRead = true ;
		unshowView ( messageView ) ;
		System.out.println ( "GUI CONTROLLER - ON_MESSAGE_READ : FINISH" ) ;
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
	public Road chooseInitRoadForSheperd(Iterable<Road> availableRoads) throws IOException 
	{
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
	
	/***/
	private Window getView ( String key ) 
	{
		return views.get ( key ) ;
	}
	
	/***/
	private void showView ( Window view , boolean isMessage ) 
	{
		System.out.println ( "megmweoògme" + messageRead ) ;
		if ( messageRead == false )
		{
			System.out.println ( "GUI CONTROLLER - SHOW_VIEW : DELAYING THE SHOW." ) ;
			executorService.submit ( new ViewShowSchedulingRunnable ( view , isMessage ) ) ;
		}
		else
		{
			if ( isMessage )
				messageRead = false  ;
			view.setVisible ( true ) ;
		}
	}
	
	/***/
	private void unshowView ( Window view ) 
	{
		view.setVisible ( false ) ;
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
	
	private class ViewShowSchedulingRunnable implements Runnable 
	{
		
		private Window target ;
		
		private boolean isMessage ;
		
		public ViewShowSchedulingRunnable ( Window target , boolean isMessage ) 
		{
			if ( target != null )
			{
				this.target = target ;
				this.isMessage = isMessage ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		@Override
		public void run () 
		{
			while ( messageRead == false ) ;
			System.out.println ( "triggo" ) ;
			SwingUtilities.invokeLater( new Runnable () 
			{ 
				public void run () 
				{
					showView ( target , isMessage ) ;
				} 
			} ); 
		}
		
	}
	
}
