package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.guimap.SocketGUIMapClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.GameView.GameMapViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

/**
 * This is the Component that manage all the GUI infrastructure.
 * It is a Controller in the sense of the MVC pattern : it shows windows, handle gui events and so on. 
 */
public class GUIController extends ViewPresenter 
	implements LoginViewObserver , SheperdColorRequestViewObserver , GameMapViewObserver
{
	
	/**
	 * Identifier for the WaitingView. 
	 */
	private static final String WAITING_VIEW_KEY = "WAITING" ;
	
	/**
	 * Identifier for the LoginView. 
	 */
	private static final String LOGIN_VIEW_KEY = "LOGIN" ;
	
	/**
	 * Identifier for the SheperdColorView
	 */
	private static final String SHEPERD_COLOR_VIEW = "SHEPERD_COLOR" ;
	
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
	 * Synch variable for the name input process. 
	 */
	private final AtomicReference < String > name ;
		
	/**
	 * Synch variable for the color input process. 
	 */
	private final AtomicReference < NamedColor > color ;
	
	private boolean wantExit ;
	
	/***/
	public GUIController () 
	{
		super () ;
		executorService = Executors.newCachedThreadPool() ;
		views = new HashMap < String , Window > () ;
		currentShownWindow = null ;
		name = new AtomicReference < String > () ;
		color = new AtomicReference < NamedColor > () ;
		name.set ( null ) ;
		color.set ( null ) ;
		wantExit = false ;
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
				showView ( waitingView ) ;
			} 
		} );
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void onTermination () throws IOException 
	{
		generationNotification ( PresentationMessages.BYE_MESSAGE ) ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				unshowView ( currentShownWindow ) ;
				for ( Window w : views.values () )
				{
					w.setVisible ( false ) ;
					w.dispose () ;
				}	
			} 				
		} );
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
				loginView.prepareView () ;
				name.set ( null ) ;
				showView ( loginView ) ;
			} 
		} );
		synchronized ( name )
		{
			while ( name.get () == null )
				try 
				{
					name.wait () ;
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
		}
		if ( wantExit == false )
		{
			res = name.get () ;
			SwingUtilities.invokeLater ( new Runnable () 
			{ 
				public void run () 
				{
					WaitingView waitingView ;
					waitingView = ( WaitingView ) getView ( WAITING_VIEW_KEY ) ;
					waitingView.setText ( PresentationMessages.NAME_VERIFICATION_MESSAGE ) ;
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
	public void onNameRequestAck ( boolean isOk , final String notes ) 
	{
		Runnable r ;
		if ( isOk )
		{
			generationNotification ( PresentationMessages.NAME_ACCEPTED_MESSAGE + "\n" + notes );
			SwingUtilities.invokeLater ( new Runnable () 
			{ 
				public void run () 
				{
					WaitingView waitingView ;
					waitingView = ( WaitingView ) getView ( WAITING_VIEW_KEY ) ;
					waitingView.setText ( PresentationMessages.WAITING_FOR_OTHER_PLAYERS_MESSAGE ) ;
				} 
			} ) ;
		}
		else
			generationNotification ( PresentationMessages.NAME_REJECTED_MESSAGE + "\n" + notes );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNotifyMatchStart () 
	{
		generationNotification ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
		SwingUtilities.invokeLater ( new Runnable () 
		{
			public void run () 
			{
				GameView gameView ;
				unshowView ( currentShownWindow );
				gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
				currentShownWindow = gameView ;
				showView ( gameView ) ;
			}
		} ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMatchWillNotStartNotification ( final String msg ) 
	{
		generationNotification ( msg ) ;
		stopApp () ;
	}
	

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( final Iterable < NamedColor > availableColors ) 
	{
		NamedColor res ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				SheperdColorView sheperdColorView ;
				sheperdColorView = ( SheperdColorView ) getView ( SHEPERD_COLOR_VIEW ) ;
				sheperdColorView.setColors(availableColors ) ;
				color.set ( null ) ;
				wantExit = false ;
				showView ( sheperdColorView ) ;
			} 
		} );
		synchronized ( color )
		{
			while ( color.get () == null ) 
				try 
				{
					color.wait();
				}
				catch (InterruptedException e) 
				{					
					e.printStackTrace();
				}
		}
		if ( wantExit == false )
		{
			res = color.get () ;
		}
		else
		{
			res = null ;
		}
		return color.get() ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void generationNotification ( String msg ) 
	{
		SwingUtilities.invokeLater ( new NotificationShowingRunnable ( msg ) ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onGUIConnectorOnNotification ( Serializable guiConnector ) 
	{
		GameView gameView ;
		while ( getView ( GAME_VIEW_KEY ) == null ) ;
		SocketGUIMapClient c ;
		try {
			gameView = (GameView) getView ( GAME_VIEW_KEY ) ;
			c = new SocketGUIMapClient ( ( Integer ) guiConnector ) ;
			executorService.submit(c);
			gameView.setGameMapObservable ( c ) ;
			gameView.setInputMode ( null ) ;
		}
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameEntered ( String enteredName ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				LoginView loginView ;
				loginView = (LoginView) getView ( LOGIN_VIEW_KEY ) ;
				unshowView ( loginView ) ;
			} 
		} );
		synchronized ( name )
		{	
			name.set ( enteredName ) ;
			name.notifyAll () ;
			wantExit = false ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantToEnterName () 
	{
		synchronized ( name )
		{
			wantExit = true ;
			name.notifyAll () ;
		}
		stopApp () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onColorChoosed ( NamedColor selectedColor ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				SheperdColorView s ;
				s = (SheperdColorView) getView ( SHEPERD_COLOR_VIEW ) ;
				unshowView ( s ) ;
			} 
		} );
		synchronized ( color )
		{
			color.set ( selectedColor ) ;
			color.notifyAll () ;
			wantExit = false ;
		}
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantChooseColor () 
	{
		synchronized  ( color ) 
		{
			wantExit = true ;
			color.notifyAll () ;
		}
		stopApp () ;
	}
	
	/**
	 * AS THE SUPER'S ONE 
	 */
	@Override
	public void onRegionSelected ( int regionUID ) 
	{
		System.out.println ( regionUID ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onRoadSelected ( int roadUID ) 
	{
		System.out.println ( roadUID ) ;
	}
	@Override
	public void onSheperdSelected(int sheperdId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimalSelected(int animalId) {
		// TODO Auto-generated method stub
		
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

	
	
	/**
	 * Finds in the views map an object with the key equals to the parameter.
	 * 
	 * @param key the key.
	 * @return the value associated with the key key, false if this key is not in the map.
	 */
	private Window getView ( String key ) 
	{
		return views.get ( key ) ;
	}
	
	/**
	 *  
	 */
	private void showView ( Window view ) 
	{
		view.setVisible ( true ) ;
	}
	
	/***/
	private void unshowView ( Window view ) 
	{
		view.setVisible ( false ) ;
	}
	
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
			views.put ( LOGIN_VIEW_KEY , new LoginView ( GUIController.this ) ) ;
			views.put ( SHEPERD_COLOR_VIEW , new SheperdColorView ( GUIController.this ) ) ;
			views.put ( GAME_VIEW_KEY , new GameView () ) ;
			views.get ( "WAITING" ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( LOGIN_VIEW_KEY ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( SHEPERD_COLOR_VIEW ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			( ( GameView ) views.get ( GAME_VIEW_KEY ) ).setExtendedState ( Frame.MAXIMIZED_BOTH ) ;
		}
		
	}
	
	private class NotificationShowingRunnable implements Runnable
	{
		private Notifier notifier ;
		
		private String message ;
		
		public NotificationShowingRunnable ( String message ) 
		{
			notifier = new Notifier () ;
			this.message = message ;
		}
		
		@Override
		public void run () 
		{
			notifier.notify ( message ) ;
		}
		
	}
	
	private class Notifier 
	{
		
		public void notify ( String messsage ) 
		{
			WaitingView waitingView ;
			GameView gameView ;
			if ( currentShownWindow instanceof WaitingView )
			{
				waitingView = ( WaitingView ) currentShownWindow ;
				waitingView.addNotification(messsage);
			}
			else
			{
				gameView = ( GameView ) currentShownWindow ;
				gameView.addNotification(messsage);
			}
		}
		
	}
	
}
