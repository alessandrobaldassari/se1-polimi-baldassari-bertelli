package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.gui.RMIGUIMapClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview.CardsMarketView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.GameMapViewInputMode;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.GameMapViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview.LoginView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview.MoveChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.ovinechooseview.OvineChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview.RegionTypeChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview.SheperdColorChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.threading.ThreadUtilities;

/**
 * This is the Component that manage all the GUI infrastructure.
 * It is a Controller in the sense of the MVC pattern : it shows windows, handle gui events and so on. 
 */
public class GUIController extends ViewPresenter implements GameMapViewObserver 
{
	
	/**
	 * A WaitingView instance to relax the User. 
	 */
	private WaitingView waitingView ;
	
	/**
	 * The effective GameView of the Game. 
	 */
	private GameView gameView ;
	
	/**
	 * A reference for the current shown window. 
	 */
	private Window currentShownWindow ;
	
	/**
	 * Synch variable for inputs that returns an Integer value. 
	 */
	private final AtomicReference < Integer > index ;
	
	/**
	 * Boolean flag that indicates the User wants to change move. 
	 */
	private boolean userWantsToChangeMove ;
			
	/***/
	public GUIController () 
	{
		super () ;
		waitingView = new WaitingView () ;
		gameView = new GameView () ;
		currentShownWindow = null ;
		index = new AtomicReference < Integer > () ;		
		index.set ( null ) ;
		userWantsToChangeMove = false ;
	}
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void startApp () 
	{
		currentShownWindow = waitingView ;
		waitingView.setText ( PresentationMessages.WELCOME_MESSAGE + Utilities.CARRIAGE_RETURN + PresentationMessages.SERVER_CONNECTION_MESSAGE ) ;
		GraphicsUtilities.showUnshowWindow ( waitingView , false , true ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 */
	@Override
	protected void onTermination () throws IOException 
	{
		System.out.println ( "GUICONTROLLER - ON_TERMINATION : INIZIO" ) ;
		// notify the user the Game is going to be closed.
		generationNotification ( PresentationMessages.BYE_MESSAGE ) ;
		GraphicsUtilities.showUnshowWindow ( currentShownWindow , true , false ) ;
		// destroy all the windows
		SwingUtilities.invokeLater ( new WindowsDisposer () ) ;
	}	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String onNameRequest () 
	{
		String res ;
		res = LoginView.showDialog () ;
		waitingView.setText ( PresentationMessages.NAME_VERIFICATION_MESSAGE ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameRequestAck ( boolean isOk , final String notes ) 
	{
		System.out.println ( "GUI_CONTROLLER - ON_NAME_REQUEST_ACK : BEGIN." ) ;
		if ( isOk )
		{
			generationNotification ( PresentationMessages.NAME_ACCEPTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes );
			waitingView.setText ( PresentationMessages.WAITING_FOR_OTHER_PLAYERS_MESSAGE ) ;
		}
		else
			generationNotification ( PresentationMessages.NAME_REJECTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes );
		System.out.println ( "GUI_CONTROLLER - ON_NAME_REQUEST_ACK : FINISHED." ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNotifyMatchStart () 
	{
		synchronized ( this )
		{
			generationNotification ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
			GraphicsUtilities.showUnshowWindow ( currentShownWindow , true , false ) ;
			currentShownWindow = gameView ;
			GraphicsUtilities.showUnshowWindow ( gameView , false , true ) ;
			notifyAll () ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMatchWillNotStartNotification ( String msg ) 
	{
		generationNotification ( msg ) ;
		stopApp () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) 
	{
		System.out.println ( "GUI_CONTROLLER - ON_SHEPERD_COLOR_REQUEST : INIZIO" ) ;
		NamedColor res ;
		generationNotification ( PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE ) ;
		res = SheperdColorChooseView.showDialog ( availableColors ) ;
		generationNotification ( "Colore scelto : " + res ) ;
		System.out.println ( "GUI_CONTROLLER - ON_SHEPERD_COLOR_REQUEST : FINE" ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException 
	{
		Collection < Integer > roadIndexes ;
		Road res ;
		roadIndexes = new LinkedList < Integer > () ;
		for ( Road road : availableRoads )
			roadIndexes.add ( road.getUID() ) ;
		generationNotification ( PresentationMessages.CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE ) ;
		index.set ( null ) ; 
		gameView.setInputMode ( GameMapViewInputMode.ROADS , roadIndexes ) ;
		ThreadUtilities.waitForAtomicVariable ( index ) ;
		gameView.setInputMode ( null , null ) ;
		res = null ;
		for ( Road road : availableRoads )
			if ( road.getUID () == index.get () )
			{
				res = road ;
				break ;
			}
		index.set(null); 
		generationNotification ( "Strada scelta correttamente!" ) ;
		System.out.println ( "GUI-CONTROLLER - CHOOSE_INIT_ROAD_FOR_SHEPERD : " + res ) ;
		if ( res == null )
			throw new IOException () ;
		return res ;
	}		
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > sheperds ) throws IOException
	{
		Collection < Integer > rightIndexes ;
		Sheperd res ;
		rightIndexes = new LinkedList < Integer > () ;
		index.set(null); 
		for ( Sheperd s : sheperds )
			rightIndexes.add ( s.getUID () ) ;
		generationNotification ( PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE ) ;
		gameView.setInputMode ( GameMapViewInputMode.SHEPERDS , rightIndexes ) ;
		ThreadUtilities.waitForAtomicVariable ( index ) ;
		gameView.setInputMode ( null , null ) ;
		res = null ;
		for ( Sheperd sheperd : sheperds )
			if ( sheperd.getUID () == index.get () )
			{
				res = sheperd ;
				break ;
			}
		index.set ( null ) ;
		generationNotification ( "Ok, per questo turno usi sempre lui !!!" ) ;
		System.out.println ( "GUI-CONTROLLER - CHOOSE_SHEPERD_FOR_A_TURN : " + res ) ;
		if ( res == null )
			throw new IOException () ;
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException 
	 * @throws WrongStateMethodCallException */
	private MoveSelection breakDownMoveManagement ( GameMap gameMap , MoveSelector selector ) throws MoveNotAllowedException, WrongStateMethodCallException 
	{
		Set < PositionableElementType > set ;
		MoveSelection res = null ;
		PositionableElementType p ;
		Collection < Integer > rightIndexes ;
		Region region ;
		Animal animal ;
		set = new HashSet < PositionableElementType > () ;
		rightIndexes = new ArrayList < Integer > () ;
		for ( Region r : selector.getAvailableRegionsForBreakdown () )
			rightIndexes.add ( r.getUID() ) ;
		if ( ! rightIndexes.isEmpty() )
		{
			// let the user choose where do the break down.
			generationNotification ( "Scegli la regione dove perpetrare il misfatto ( tra quelle vicine al tuo pastore scelto )..." ) ;
			gameView.setInputMode ( GameMapViewInputMode.REGIONS , rightIndexes ) ;
			index.set ( null ) ;
			ThreadUtilities.waitForAtomicVariable ( index ) ;
			gameView.setInputMode(null,null);
			if ( userWantsToChangeMove == false )
			{	
				region = gameMap.getRegionByUID ( index.get () ) ;
				index.set ( null ) ;
				set.add ( PositionableElementType.RAM ) ;
				set.add ( PositionableElementType.SHEEP ) ;
				set.add ( PositionableElementType.LAMB ) ;
				for ( Animal a : region.getContainedAnimals () )
					if ( set.contains ( a.getPositionableElementType() ) )
						set.remove ( a.getPositionableElementType() ) ;
				// gestire la finestra che visualizza gli ovini
				p = OvineChooseView.showDialog ( set ) ;
				animal = null ;
				for ( Animal a : region.getContainedAnimals() )
					if ( a.getPositionableElementType() == p )
					{
						animal = a ;
						break ;
					}
				if ( animal != null )
				{
					selector.selectBreakdown(animal);
					res = selector.getSelectedMove() ;
				}
				else
					res = null ;
			}
		}
		else
		{
			generationNotification( "Scusa, ma questa mossa non la puoi fare." ) ;
			res = null ;
			userWantsToChangeMove = true ;
		}
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException 
	 * @throws WrongStateMethodCallException */
	private MoveSelection buyCardManagement ( MoveSelector selector ) throws MoveNotAllowedException, WrongStateMethodCallException 
	{
		MoveSelection res ;
		Collection < Couple < RegionType , Integer > > allowedMoves ;
		RegionType type ;
		type = RegionTypeChooseView.showDialog ( selector.getAvailableRegionsForBuyCard() , selector.getAvailableMoney () ).getFirstObject () ; 
		if ( type != null )
		{
			selector.selectBuyCard ( type ) ;
			res = selector.getSelectedMove();
		}	
		else
		{
			res = null ;
			userWantsToChangeMove = true ;
		}
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException 
	 * @throws WrongStateMethodCallException */
	private MoveSelection mateManagement ( GameMap gameMap , MoveSelector selector ) throws MoveNotAllowedException, WrongStateMethodCallException 
	{
		Collection < Integer > rightIndexes ;
		MoveSelection res ;
		Region region ;
		rightIndexes = new ArrayList < Integer > () ;
		for ( Region r : selector.getAvailableRegionsForMate() )
			rightIndexes.add ( r.getUID() ) ;
		if ( ! rightIndexes.isEmpty() )
		{
			gameView.setInputMode ( GameMapViewInputMode.REGIONS , rightIndexes ) ;
			generationNotification ( "Scegli la regione dove vuoi provare a far eseguire l'accoppiamento ( vicina a te )." );
			index.set ( null ) ;
			ThreadUtilities.waitForAtomicVariable ( index ) ;
			gameView.setInputMode(null,null);
			if ( userWantsToChangeMove == false )		
			{	
				region = gameMap.getRegionByUID ( index.get () ) ;
				selector.selectMate ( region ) ;
				res = selector.getSelectedMove();
			}
			else
				res = null ;
		}
		else
		{
			generationNotification ( "Sorry, ma non ci sono regioni dove tu possa fare accoppiamento !\nScegli un'altra mossa" );
			res = null ;
			userWantsToChangeMove = true ;
		}
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException 
	 * @throws WrongStateMethodCallException */
	private MoveSelection moveSheepManagement ( GameMap gameMap , MoveSelector selector ) throws MoveNotAllowedException, WrongStateMethodCallException 
	{
		Set < PositionableElementType > set ;
		Collection < Integer > rightIndexes ;
		Region region ;
		MoveSelection res ;
		PositionableElementType p ;
		Animal animal ;
		set = new HashSet < PositionableElementType > () ;
		rightIndexes = new ArrayList < Integer > () ;
		for ( Region r : selector.getAvailableRegionsForMoveSheep() )
			rightIndexes.add ( r.getUID() );
		// if there are regions available for this move...
		if ( ! rightIndexes.isEmpty() )
		{
			gameView.setInputMode ( GameMapViewInputMode.REGIONS , rightIndexes ) ;
			generationNotification ( "Scegli la regione da dove spostare l'ovino ( tra quelle vicine al pastore che hai scelto)." ) ;
			index.set ( null ) ;
			ThreadUtilities.waitForAtomicVariable ( index ) ;
			if ( userWantsToChangeMove  == false )
			{
				region = gameMap.getRegionByUID ( index.get() ) ;
				generationNotification ( "Ok, ora scegli un ovino da muovere ( all'interno della regione che hai scelto)." ) ;
				region = gameMap.getRegionByUID ( index.get () ) ;
				index.set ( null ) ;
				set.add ( PositionableElementType.RAM ) ;
				set.add ( PositionableElementType.SHEEP ) ;
				set.add ( PositionableElementType.LAMB ) ;
				for ( Animal a : region.getContainedAnimals () )
					if ( set.contains ( a.getPositionableElementType() ) )
						set.remove ( a.getPositionableElementType() ) ;
				p = OvineChooseView.showDialog ( set ) ;
				animal = null ;
				for ( Animal a : region.getContainedAnimals() )
					if ( a.getPositionableElementType() == p )
					{
						animal = a ;
						break ;
					}
				if ( animal != null )
				{
					selector.selectBreakdown(animal);
					res = selector.getSelectedMove () ;
				}
				else
					res = null ;
			}
			else
				res = null ;
		}
		else
		{
			generationNotification ( "Sorry, ma non ci sono regioni da cui tu possa spostare pecore !\nScegli un'altra mossa" );
			res = null ;
			userWantsToChangeMove = true ;
		}
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException 
	 * @throws WrongStateMethodCallException */
	private MoveSelection moveSheperdManagement ( GameMap gameMap , MoveSelector selector ) throws MoveNotAllowedException, WrongStateMethodCallException 
	{
		Collection < Integer > rightIndexes ;
		Road road ;
		MoveSelection res ;
		rightIndexes = new ArrayList < Integer > () ;
		for ( Road r : selector.getAvailableRoadsForMoveSheperd() )
			rightIndexes.add ( r.getUID() ) ;
		index.set(null) ;
		gameView.setInputMode ( GameMapViewInputMode.ROADS , rightIndexes ) ;
		ThreadUtilities.waitForAtomicVariable ( index ) ;
		gameView.setInputMode ( null ,null ) ;
		if ( userWantsToChangeMove == false )
		{
			road = gameMap.getRoadByUID ( index.get () ) ;
			selector.selectMoveSheperd ( road ) ;
			res = selector.getSelectedMove();
		}
		else
			res = null ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public MoveSelection onDoMove ( MoveSelector selector , GameMap gameMap ) 
	{
		MoveSelection res ;
		GameMoveType move ;
		// first ask the user what move he wants to do.
		res = null ;
		System.out.println ( "GUI_CONTROLLER - ON_DO_MOVE : INIZIO" ) ;
		do
		{
			userWantsToChangeMove = false ;
			move = null ;
			// controlli a priori sulle mosse che si possono fare ?
			move = MoveChooseView.showDialog () ;
			System.out.println ( "GUI_CONTROLLER - DO_MOVE : MOVE RECEIVED = " + move ) ;
			if ( move != null )
			{
				try {
				switch ( move )
				{
					case BREAK_DOWN :
						res = breakDownMoveManagement ( gameMap , selector );
					break ;
					case BUY_CARD :
						res = buyCardManagement ( selector ) ;
					break ;
					case MATE :
						res = mateManagement ( gameMap , selector ) ;
					break ;
					case MOVE_SHEEP :
						res = moveSheepManagement ( gameMap , selector ) ;
						break ;
					case MOVE_SHEPERD :
						res = moveSheperdManagement ( gameMap , selector ) ;
					break ;
					default :
						throw new RuntimeException () ;
				}
				}
				catch ( WrongStateMethodCallException w ) 
				{
					res = null ;
					userWantsToChangeMove = true ;
				}
				catch (MoveNotAllowedException e) 
				{
					userWantsToChangeMove = false ;
					res = null ;
				}
			}
		}
		while ( userWantsToChangeMove == true ) ;
		return res ;	
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > playerCards ) 
	{
		Iterable < SellableCard > res ;
		System.out.println ( "GUI_CONTROLLER - onChooseCardsEligibleForSelling - INIZIO" ) ;
		System.out.println ( "GUI_CONTROLLER - onChooseCardsEligibleForSelling - playerCards = " + playerCards ) ;
		res = new LinkedList < SellableCard > () ;
		generationNotification ( "Scegli le carte che vuoi vendere" );
		res = CardsMarketView.showDialog ( playerCards , true , -1 ) ;
		System.out.println ( "GUI_CONTROLLER - onChooseCardsEligibleForSelling - FINE" ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > acquirables , Integer playerMoney ) 
	{
		System.out.println ( "GUI_CONTROLLER - ON_CHOOSE_CARDS_TO_BUY - INIZIO" ) ;
		System.out.println ( "GUI_CONTROLLER - ON_CHOOSE_CARDS_TO_BUY : acquirables = " + acquirables ) ;
		Iterable < SellableCard > res ;
		generationNotification ( "Scegli le carte che vuoi vendere" );
		res = CardsMarketView.showDialog ( acquirables , false , playerMoney ) ;
		System.out.println ( "GUI_CONTROLLER - ON_CHOOSE_CARDS_TO_BUY - FINE" ) ;
		return res ;		
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
		RMIGUIMapClient c ;
		try 
		{
			c = new RMIGUIMapClient ( ( String ) guiConnector ) ;
			c.connect ( this ) ;
			c.setGameMapObserver ( gameView.getGameMapObserver () );
			c.setPlayerObserver ( gameView.getPlayerObserver () );
			gameView.setGameMapViewObserver ( this ) ;
			SheeplandClientApp.getInstance().executeRunnable(c); 
			gameView.setInputMode ( null , null ) ;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	// EVENT HANDLERS
	
	/**
	 * AS THE SUPER'S ONE 
	 */
	@Override
	public void onRegionSelected ( Integer regionUID ) 
	{
		processGameViewIntResReceived ( regionUID , GameMapViewInputMode.REGIONS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onRoadSelected ( Integer roadUID ) 
	{
		processGameViewIntResReceived ( roadUID , GameMapViewInputMode.ROADS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onSheperdSelected ( Integer sheperdId ) 
	{
		processGameViewIntResReceived ( sheperdId , GameMapViewInputMode.SHEPERDS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onAnimalSelected ( Integer animalId ) 
	{
		processGameViewIntResReceived ( animalId , GameMapViewInputMode.ANIMALS ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onWantToChangeMove () 
	{
		index.set ( -1 ) ;
		userWantsToChangeMove = true ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onGameConclusionNotification(String cause) throws IOException 
	{
		JOptionPane.showMessageDialog ( null , cause , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE ) ;
		stopApp () ;
	}
	
	/***/
	private void processGameViewIntResReceived ( Integer value , GameMapViewInputMode rightMode ) 
	{
		System.out.println ( "GUI_CONTROLLER - processGameViewIntResReceived :\n value = " + value + "\nRight mode = " + rightMode + "\nActual mode : " + gameView.getInputMode() ) ;
		if ( gameView.getInputMode () == rightMode )
		{
			gameView.setInputMode ( null , null ) ;
			setIndexVariable ( value ) ;
		}
	}
	
	/**
	 * @param value the value for the index variable 
	 */
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
	
	/**
	 * 
	 */
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
	
	/***/
	private class Notifier 
	{
	
		/***/
		public void notify ( String message ) 
		{
			if ( currentShownWindow != null )
				synchronized ( currentShownWindow )
				{
					NotificationContainer notificationContainer ;
					notificationContainer = ( NotificationContainer ) currentShownWindow ;
					System.out.println ( "NOTIFIER : NOTIFICATION CONTAINER RETRIEVED : " + currentShownWindow ) ;
					notificationContainer.addNotification ( message ) ;
					currentShownWindow.notifyAll () ;
					System.out.println ( "NOTIFIER : FINISH " ) ;
				}
		}
		
	}

	/**
	 * This class dispose of the Windows running in the Game when it finishes. 
	 */
	private class WindowsDisposer implements Runnable 
	{
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			waitingView.dispose();
			gameView.dispose();
		}
		
	}
	
}
