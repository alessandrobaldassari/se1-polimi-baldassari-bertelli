package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.gui.RMIGUIMapClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview.CardsMarketView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameMapViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameViewInputMode;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview.LoginView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview.MoveChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview.RegionTypeChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview.SheperdColorChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

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
	 * Synch variable for inputs that returns an Integer value. 
	 */
	private final AtomicReference < Integer > index ;
	
	/**
	 * A component to manage thread issues. 
	 */
	private final ExecutorService executorService ;
	
	/**
	 * A reference for the current shown window. 
	 */
	private Window currentShownWindow ;
	
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
		index = new AtomicReference < Integer > () ;		
		index.set ( null ) ;
		currentShownWindow = null ;
		executorService = Executors.newCachedThreadPool() ;
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
		System.out.println ( "notify match start" ) ;
		generationNotification ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
		GraphicsUtilities.showUnshowWindow ( currentShownWindow , true , false ) ;
		currentShownWindow = gameView ;
		GraphicsUtilities.showUnshowWindow ( gameView , false , true ) ;
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
		res = SheperdColorChooseView.showDialog ( availableColors ) ;
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
		gameView.setInputMode ( GameViewInputMode.ROADS ) ;
		gameView.beginHighlightVisualization ( roadIndexes ) ;
		waitForAtomicVariable ( index ) ;
		gameView.setInputMode ( null ) ;
		res = null ;
		for ( Road road : availableRoads )
			if ( road.getUID () == index.get () )
			{
				res = road ;
				break ;
			}
		index.set(null); 
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
		System.out.println ( "GUI-CONTROLLER - CHOOSE_SHEPERD_FOR_A_TURN : INIZIO " ) ;
		generationNotification ( PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE ) ;
		index.set(null); 
		rightIndexes = new LinkedList < Integer > () ;
		for ( Sheperd s : sheperds )
			rightIndexes.add ( s.getUID () ) ;
		gameView.beginHighlightVisualization ( rightIndexes ) ;
		gameView.setInputMode ( GameViewInputMode.SHEPERDS ) ;
		System.out.println ( "GUI-CONTROLLER - CHOOSE_SHEPERD_FOR_A_TURN : PRIMA DELLA BARRIERA DI ATTESA." ) ;
		waitForAtomicVariable ( index ) ;
		System.out.println ( "GUI-CONTROLLER - CHOOSE_SHEPERD_FOR_A_TURN : DOPO LA BARRIERA DI ATTESA." ) ;
		res = null ;
		for ( Sheperd sheperd : sheperds )
			if ( sheperd.getUID () == index.get () )
			{
				res = sheperd ;
				break ;
			}
		index.set ( null ) ;
		System.out.println ( "GUI-CONTROLLER - CHOOSE_SHEPERD_FOR_A_TURN : " + res ) ;
		if ( res == null )
			throw new IOException () ;
		return res ;
	}
	
	/***/
	private MoveSelection breakDownMoveManagement ( GameMap gameMap , MoveSelector selector ) 
	{
		MoveSelection res = null ;
		Collection < Integer > rightIndexes ;
		Region region ;
		Animal animal ;
		rightIndexes = new ArrayList < Integer > () ;
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getFirstBorderRegion().getUID() );
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getSecondBorderRegion().getUID() );
		// let the user choose where do the break down.
		generationNotification ( "Scegli la regione dove perpetrare il misfatto ( tra quelle vicine al tuo pastore scelto )..." ) ;
		gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
		gameView.beginHighlightVisualization ( rightIndexes );
		index.set ( null ) ;
		waitForAtomicVariable ( index ) ;
		if ( userWantsToChangeMove == false )
		{	
			region = gameMap.getRegionByUID ( index.get () ) ;
			index.set ( null ) ;
			generationNotification ( "Seleziona, all'interno della regione che hai scelto, l'animale da abbattere!" );
			rightIndexes.clear () ;
			for ( Animal a : region.getContainedAnimals () )
				rightIndexes.add ( a.getUID () ) ;
			gameView.beginHighlightVisualization ( rightIndexes ) ;
			gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
			waitForAtomicVariable ( index ) ;
			if ( userWantsToChangeMove == false )
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
						res = selector.newBreakdown ( animal ) ;
					}
					catch (Exception e) 
					{
						JOptionPane.showMessageDialog ( null , PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE ) ;
						userWantsToChangeMove = true ;
					}
			}	
		}
		return res ;
	}
	
	/***/
	private MoveSelection buyCardManagement ( MoveSelector selector ) 
	{
		MoveSelection res ;
		Map < RegionType , Integer > bankSummary ;
		Collection < Couple < RegionType , Integer > > allowedMoves ;
		RegionType type ;
		int userMoney ;
		// prompt the User with a List of Regions to choose.
		bankSummary = selector.getBankPriceCards();
		allowedMoves = new ArrayList < Couple < RegionType , Integer > > () ;
		// right prices fix !
		userMoney = selector.getAssociatedSheperd ().getOwner ().getMoney () ;
		for ( RegionType r : bankSummary.keySet() )
			if ( bankSummary.get ( r ) <= userMoney )
				allowedMoves.add ( new Couple < RegionType , Integer > ( r , 0 ) ) ;
		type = RegionTypeChooseView.showDialog ( allowedMoves , userMoney ).getFirstObject () ; 
		if ( type != null )
			try 
			{
				res = selector.newBuyCard ( type ) ;
			}
			catch (MoveNotAllowedException e1) 
			{
				e1.printStackTrace();
				generationNotification ( "Sorry, ma questa mossa non può avvenire!\n"+e1.getMessage () + "\nScegli un'altra regione!" ) ; 				
				userWantsToChangeMove = true ;
				res = null ;
			}
		else
		{
			res = null ;
			userWantsToChangeMove = true ;
		}
		return res ;
	}
	
	/***/
	private MoveSelection mateManagement ( GameMap gameMap , MoveSelector selector ) 
	{
		Collection < Integer > rightIndexes ;
		MoveSelection res ;
		Region region ;
		rightIndexes = new ArrayList < Integer > () ;
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getFirstBorderRegion().getUID() );
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getSecondBorderRegion().getUID() );
		gameView.beginHighlightVisualization ( rightIndexes ) ;
		gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
		generationNotification ( "Scegli la regione dove vuoi provare a far eseguire l'accoppiamento." );
		index.set ( null ) ;
		waitForAtomicVariable ( index ) ;
		if ( userWantsToChangeMove == false )		
		{	
			region = gameMap.getRegionByUID ( index.get () ) ;
			try 
			{
				res = selector.newMate ( region ) ;
			}
			catch (MoveNotAllowedException e)
			{
				generationNotification ( "Sorry, ma questa mossa non può avvenire!\n"+e.getMessage () + "\nScegli un'altra regione!" ) ; 
				userWantsToChangeMove = true ;
				res = null ;
			}		
		}
		else
			res = null ;
		return res ;
	}
	
	/***/
	private MoveSelection moveSheepManagement ( GameMap gameMap , MoveSelector selector ) 
	{
		Collection < Integer > rightIndexes ;
		Region region ;
		MoveSelection res ;
		Animal animal ;
		rightIndexes = new LinkedList < Integer > () ;
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getFirstBorderRegion().getUID() );
		rightIndexes.add ( selector.getAssociatedSheperd().getPosition().getSecondBorderRegion().getUID() );
		gameView.beginHighlightVisualization ( rightIndexes ) ;
		gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
		generationNotification ( "Scegli la regione da dove spostare l'ovino ( tra quelle vicine al pastore che hai scelto)." ) ;
		index.set ( null ) ;
		waitForAtomicVariable ( index ) ;
		if ( userWantsToChangeMove  == false )
		{
			region = gameMap.getRegionByUID ( index.get() ) ;
			generationNotification ( "Ok, ora scegli un ovino da muovere ( all'interno della regione che hai scelto)." ) ;
			rightIndexes.clear () ;
			for ( Animal a : region.getContainedAnimals () )
				if ( a instanceof Ovine )
					rightIndexes.add ( a.getUID () ) ;
			gameView.beginHighlightVisualization ( rightIndexes ) ;
			gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
			index.set ( null ) ;
			waitForAtomicVariable ( index ) ;
			if ( userWantsToChangeMove == false )
			{
				// find the ovine with the index selected by the User.
				animal = null ;
				for ( Animal a : region.getContainedAnimals () )
					if ( a.getUID () == index.get () )
					{
						animal = a ;
						break ;
					}
				try 
				{
					res = selector.newMoveSheep ( ( Ovine ) animal , region ) ;
				}
				catch (MoveNotAllowedException e) 
				{
					e.printStackTrace();
					userWantsToChangeMove = true ;
					res = null ;
				}
			}
			else
				res = null ;
		}
		else
			res = null ;
		return res ;
	}
	
	/***/
	private MoveSelection moveSheperdManagement ( GameMap gameMap , MoveSelector selector ) 
	{
		Collection < Integer > rightIndexes ;
		Road road ;
		MoveSelection res ;
		rightIndexes = new ArrayList < Integer > () ;
		for ( Road r : gameMap.getFreeRoads () )
			rightIndexes.add ( r.getUID() ) ;
		gameView.beginHighlightVisualization ( rightIndexes ) ;
		gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
		index.set(null) ;
		waitForAtomicVariable ( index ) ;
		if ( this.userWantsToChangeMove == false )
		{
			road = gameMap.getRoadByUID ( index.get () ) ;
			try
			{
				res = selector.newMoveSheperd ( road ) ;
			}
			catch (MoveNotAllowedException e)
			{
				e.printStackTrace();
				userWantsToChangeMove = false ;
				res = null ;
			}
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
			generationNotification ( PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE );
			userWantsToChangeMove = false ;
			// controlli a priori sulle mosse che si possono fare ?
			move = MoveChooseView.showDialog () ;
			System.out.println ( "GUI_CONTROLLER - DO_MOVE : MOVE RECEIVED = " + move ) ;
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
		res = new LinkedList < SellableCard > () ;
		generationNotification ( "Scegli le carte che vuoi vendere" );
		res = CardsMarketView.showDialog ( playerCards , true , -1 ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > acquirables , Integer playerMoney ) 
	{
		Iterable < SellableCard > res ;
		generationNotification ( "Scegli le carte che vuoi vendere" );
		res = CardsMarketView.showDialog ( acquirables , false , playerMoney ) ;
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
			gameView.setGameMapObservable ( c ) ;
			gameView.setGameMapViewObserver ( this ) ;
			executorService.submit ( c ) ;
			gameView.setInputMode ( null ) ;
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
	private void processGameViewIntResReceived ( Integer value , GameViewInputMode rightMode ) 
	{
		System.out.println ( "GUI_CONTROLLER - processGameViewIntResReceived :\n value = " + value + "\nRight mode = " + rightMode + "\nActual mode : " + gameView.getInputMode() ) ;
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
	
	/***/
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
