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

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.guimap.SocketGUIMapClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.MoveChooseView.MoveChooseViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

/**
 * This is the Component that manage all the GUI infrastructure.
 * It is a Controller in the sense of the MVC pattern : it shows windows, handle gui events and so on. 
 */
public class GUIController extends ViewPresenter 
	implements LoginViewObserver , SheperdColorRequestViewObserver , MoveChooseViewObserver , GameMapViewObserver
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
	private static final String SHEPERD_COLOR_KEY = "SHEPERD_COLOR" ;
	
	/**
	 * Identifier for the MoveChooseView. 
	 */
	private static final String MOVE_CHOOSE_KEY = "MOVE_CHOOSE" ;
	
	/**
	 * Identifier for the GameView. 
	 */
	private static final String GAME_VIEW_KEY = "GAME" ;
	
	/**
	 * A Map containing the windows used in the App. 
	 */
	private final Map < String , Window > views ;
	
	/**
	 * Synch variable for the name input process. 
	 */
	private final AtomicReference < String > name ;
		
	/**
	 * Synch variable for the color input process. 
	 */
	private final AtomicReference < NamedColor > color ;
	
	/**
	 * Synch variable for the move input process 
	 */
	private final AtomicReference < GameMoveType > move ;
	
	/**
	 * Synch variable for inputs that returns an Integer value. 
	 */
	private final AtomicReference < Integer > index ;
	
	/**
	 * Synch variable for inputs that returns a Card value. 
	 */
	private final AtomicReference < Card > card ;
	
	/**
	 * A component to manage thread issues. 
	 */
	private final ExecutorService executorService ;
	
	/**
	 * A reference for the current shown window. 
	 */
	private Window currentShownWindow ;
	
	/**
	 * A boolean flag that, if true, indicates that the user wants to exit the App. 
	 */
	private boolean wantExit ;
	
	/***/
	public GUIController () 
	{
		super () ;
		views = new HashMap < String , Window > () ;
		name = new AtomicReference < String > () ;
		color = new AtomicReference < NamedColor > () ;
		index = new AtomicReference < Integer > () ;
		move = new AtomicReference < GameMoveType > () ;
		card = new AtomicReference < Card > () ;
		name.set ( null ) ;
		color.set ( null ) ;
		index.set ( null ) ;
		move.set ( null ) ;
		currentShownWindow = null ;
		executorService = Executors.newCachedThreadPool() ;
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
		waitForAtomicVariable ( name ) ;
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
			generationNotification ( PresentationMessages.NAME_REJECTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes );
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
				sheperdColorView = ( SheperdColorView ) getView ( SHEPERD_COLOR_KEY ) ;
				sheperdColorView.setColors(availableColors ) ;
				color.set ( null ) ;
				wantExit = false ;
				showView ( sheperdColorView ) ;
			} 
		} );
		waitForAtomicVariable ( color ) ;
		if ( wantExit == false )
		{
			res = color.get () ;
		}
		else
		{
			res = null ;
		}
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException 
	{
		Road res ;
		GameView gameView ;
		generationNotification ( PresentationMessages.CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE ) ;
		gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
		index.set(null); 
		gameView.setInputMode ( GameViewInputMode.ROADS ) ;
		synchronized ( index )
		{
			while ( index.get () == null )
				try 
				{
					index.wait () ;
				}
				catch ( InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		if ( index.get () >= 0 )
		{
			res = null ;
			for ( Road road : availableRoads )
				if ( road.getUID () == index.get () )
				{
					res = road ;
					break ;
				}
		}
		else
			res = null ;
		System.out.println ( "RES : " + res ) ;
		return res ;
	}		
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > sheperds )
	{
		Sheperd res ;
		GameView gameView ;
		generationNotification ( PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE ) ;
		gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
		index.set(null); 
		gameView.setInputMode ( GameViewInputMode.SHEPERDS ) ;
		synchronized ( index )
		{
			while ( index.get () == null )
				try 
				{
					index.wait () ;
				}
				catch ( InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		if ( index.get () >= 0 )
		{
			res = null ;
			for ( Sheperd sheperd : sheperds )
				if ( sheperd.getUID () == index.get () )
				{
					res = sheperd ;
					break ;
				}
			if ( res == null )
			{	
				generationNotification ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Sicuro di avere scelto uno dei tuoi pastori ?!" + Utilities.CARRIAGE_RETURN + "Non imbrogliare, scegli tra i tuoi !" );
				res = onChooseSheperdForATurn ( sheperds ) ;
			}
		}
		else
			res = null ;
		System.out.println ( "RES : " + res ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GameMove onDoMove ( MoveFactory moveFactory , GameMap gameMap ) 
	{
		GameMove res ;
		Region region ;
		Road road ;
		Animal animal ;
		boolean endCycle ;
		// first ask the user what move he wants to do.
		move.set ( null ) ;
		res = null ;
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				MoveChooseView moveChooseView ;
				moveChooseView = ( MoveChooseView ) getView ( MOVE_CHOOSE_KEY ) ;
				move.set ( null ) ;
				wantExit = false ;
				showView ( moveChooseView ) ;
			} 
		} );
		waitForAtomicVariable ( move ) ;
		if ( wantExit == false )
		{
			unshowView ( getView ( MOVE_CHOOSE_KEY ) ) ;
			GameView gameView ;
			gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
			// allow the user the do his move
			switch ( move.get() )
			{
				case BREAK_DOWN :
					endCycle = false ;
					while ( endCycle == false )
					{	// let the user choose where do the break down.
						generationNotification ( "Scegli la regione dove perpetrare il misfatto..." );
						gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
						index.set ( null ) ;
						wantExit = false ;
						waitForAtomicVariable ( index ) ;
						if ( wantExit == false )
						{
							region = gameMap.getRegionByUID ( index.get () ) ;
							if ( CollectionsUtilities.iterableSize( region.getContainedAnimals () ) > 0 )
							{
								index.set ( null ) ;
								wantExit = false ;
								generationNotification ( "Seleziona, all'interno della regione che hai scelto, l'animale da abbattere!" );
								gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
								waitForAtomicVariable ( index ) ;
								if ( wantExit == false )
								{
									animal = null ;
									for ( Animal a : region.getContainedAnimals () )
										if ( a.getUID () == index.get () )
										{
											animal = a ;
											break ;
										}
									if ( animal != null )
										try 
										{
											moveFactory.newBreakDownMove ( animal ) ;
										}
										catch (MoveNotAllowedException e) 
										{
											e.printStackTrace();
										}
									else
									{
										res = null ;
										endCycle = true ;
									}
								}
							else
							{
								res  = null ;
								endCycle = true ;
							}
						}
						else
						{
							res = null ;
							endCycle = true ;
						}
						}
					}
				break ;
				case BUY_CARD :
					// 
				break ;
				case MATE :
					endCycle = false ;
					while ( ! endCycle )
					{
						gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
						generationNotification ( "Scegli la regione dove vuoi provare a far eseguire l'accoppiamento." );
						index.set ( null ) ;
						wantExit = false ;
						waitForAtomicVariable ( index ) ;
						if ( wantExit == false ) 
						{						
								region = gameMap.getRegionByUID ( index.get () ) ;
								try 
								{
									res = moveFactory.newMate ( region ) ;
									endCycle = true ;
								}
								catch (MoveNotAllowedException e)
								{
									generationNotification ( "Sorry, ma questa mossa non può avvenire!\n"+e.getMessage () + "\nScegli un'altra regione!" ) ; 
									endCycle = false ;
								}
							
						}
					}
				break ;
				case MOVE_SHEEP :
					endCycle = false ;
					while ( ! endCycle )
					{
						gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
						generationNotification ( "Scegli la regione da dove spostare l'ovino." ) ;
						index.set ( null ) ;
						wantExit = false ;
						waitForAtomicVariable ( index ) ;
						if ( wantExit == false )
						{
							region = gameMap.getRegionByUID ( index.get() ) ;
							if ( GameMap.areAdjacents ( moveFactory.getAssociatedSheperd().getPosition () , region ) )
							{
								generationNotification ( "Ok, ora scegli un ovino da muovere" ) ;
								gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
								index.set ( null ) ;
								wantExit = false ;
								waitForAtomicVariable ( index ) ;
								if ( ! wantExit )
								{
									animal = null ;
								}
								else
								{
									res = null ;
									endCycle = true ;
								}
							}
							else
							{
								generationNotification ( "Sorry, ma non puoi agire su questa regione!\nDevi selezionare una regione adiacente al Pastore che stai usando!" ) ;
								endCycle = false ;
							}
						}
						else
						{
							res = null ;
							endCycle = true ;
						}
					}
				break ;
				case MOVE_SHEPERD :
					endCycle = false ;
					while ( ! endCycle )
					{
						gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
						index.set(null) ;
						wantExit = false ;
						waitForAtomicVariable ( index ) ;
						if ( ! wantExit )
						{
							road = gameMap.getRoadByUID ( index.get () ) ;
							if ( road.getElementContained () == null )
							{
								try
								{
									res = moveFactory.newMoveSheperd ( road ) ;
									endCycle = true ;
								}
								catch (MoveNotAllowedException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								generationNotification ( "Sorry, ma la regione è occupata, non puoi andarci!\nScegline una libera!" ) ;
								endCycle = false ;
							}
						}
						else
						{
							res = null ;
							endCycle = true ;
						}
					}
				break ;
				default :
					throw new RuntimeException () ;
			}
		}
		else
		{
			res = null ;
		}
		return res ;	
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > playerCards ) 
	{
		return null ;
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
				s = (SheperdColorView) getView ( SHEPERD_COLOR_KEY ) ;
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
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMoveChoosed ( GameMoveType move ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				MoveChooseView moveChooseView ;
				moveChooseView = (MoveChooseView) getView ( SHEPERD_COLOR_KEY ) ;
				unshowView ( moveChooseView ) ;
			} 
		} );
		synchronized ( move )
		{
			this.move.set ( move ) ;
			this.color.notifyAll () ;
			wantExit = false ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantChooseMove() 
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * AS THE SUPER'S ONE 
	 */
	@Override
	public void onRegionSelected ( Integer regionUID ) 
	{
		processGameViewIntResReceived ( regionUID , GameViewInputMode.REGIONS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onRoadSelected ( Integer roadUID ) 
	{
		processGameViewIntResReceived ( roadUID , GameViewInputMode.ROADS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onSheperdSelected ( Integer sheperdId ) 
	{
		processGameViewIntResReceived ( sheperdId , GameViewInputMode.SHEPERDS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onAnimalSelected ( Integer animalId ) 
	{
		processGameViewIntResReceived ( animalId , GameViewInputMode.ANIMALS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onDoNotWantToMakeAnySelection () {}
	
	@Override
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > acquirables ) 
	{
		return null ;
		
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
	
	/***/
	private void processGameViewIntResReceived ( int value , GameViewInputMode rightMode ) 
	{
		GameView gameView ;
		gameView = ( GameView ) getView ( GAME_VIEW_KEY ) ;
		if ( gameView.getInputMode () == rightMode )
		{
			gameView.setInputMode ( null ) ;
			setIndexVariable ( value ) ;
		}
	}
	
	/***/
	private void setIndexVariable ( int value ) 
	{
		synchronized ( index )
		{
			if ( value >= 0 )
				index.set ( value ) ;
			else
				index.set ( -1 ) ;
			index.notifyAll () ;
		}
	}
	
	/***/
	private void waitForAtomicVariable ( AtomicReference toWaitFor )
	{
		synchronized ( toWaitFor )
		{
			while ( toWaitFor.get () == null )
				try 
				{
					toWaitFor.wait () ;
				} 
				catch ( InterruptedException e )
				{
					e.printStackTrace () ;
				}
		}
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
			views.put ( SHEPERD_COLOR_KEY , new SheperdColorView ( GUIController.this ) ) ;
			views.put ( MOVE_CHOOSE_KEY , new MoveChooseView ( GUIController.this ) ) ;
			views.put ( GAME_VIEW_KEY , new GameView () ) ;
			views.get ( WAITING_VIEW_KEY ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( LOGIN_VIEW_KEY ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( SHEPERD_COLOR_KEY ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
			views.get ( MOVE_CHOOSE_KEY ).setSize ( GraphicsUtilities.getVGAResolution () ) ;
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
