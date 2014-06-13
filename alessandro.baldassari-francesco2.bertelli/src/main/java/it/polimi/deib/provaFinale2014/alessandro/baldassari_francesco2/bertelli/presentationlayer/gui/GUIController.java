package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.choosecardsview.CardsChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameMapViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.GameViewInputMode;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview.LoginView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview.MoveChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview.SheperdColorChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview.SheperdColorChooseViewObserver;
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
	
	// implement in the game map make selectable only the available roads.
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException 
	{
		Road res ;
		generationNotification ( PresentationMessages.CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE ) ;
		index.set(null); 
		gameView.setInputMode ( GameViewInputMode.ROADS ) ;
		// highlight effects
		waitForAtomicVariable(index);
		res = null ;
		for ( Road road : availableRoads )
			if ( road.getUID () == index.get () )
			{
				res = road ;
				break ;
			}
		if ( res == null )
			throw new IOException () ;
		return res ;
	}		
	
	// make selectable only the available sheperds
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > sheperds ) throws IOException
	{
		Sheperd res ;
		generationNotification ( PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE ) ;
		index.set(null); 
		gameView.setInputMode ( GameViewInputMode.SHEPERDS ) ;
		waitForAtomicVariable ( index ) ;
		res = null ;
		for ( Sheperd sheperd : sheperds )
			if ( sheperd.getUID () == index.get () )
			{
				res = sheperd ;
				break ;
			}
		if ( res == null )
			throw new IOException () ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GameMove onDoMove ( MoveFactory moveFactory , GameMap gameMap ) 
	{
		GameMove res ;
		GameMoveType move ;
		RegionType type ;
		Region region ;
		Road road ;
		Animal animal ;
		boolean endCycle ;
		// first ask the user what move he wants to do.
		res = null ;
		do
		{
			userWantsToChangeMove = false ;
			move = MoveChooseView.showDialog () ;
			switch ( move )
			{
				case BREAK_DOWN :
					endCycle = false ;
					// let the user choose where do the break down.
					generationNotification ( "Scegli la regione dove perpetrare il misfatto..." );
					gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
					index.set ( null ) ;
					waitForAtomicVariable ( index ) ;
					if ( this.userWantsToChangeMove == false )
					{	
						region = gameMap.getRegionByUID ( index.get () ) ;
						index.set ( null ) ;
						generationNotification ( "Seleziona, all'interno della regione che hai scelto, l'animale da abbattere!" );
						gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
						waitForAtomicVariable ( index ) ;
						if ( this.userWantsToChangeMove == false )
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
								JOptionPane.showMessageDialog ( null , PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE ) ;
								userWantsToChangeMove = true ;
							}
						}	
					}
			break ;
			case BUY_CARD :
				// prompt the User with a List of Regions to choose.
				type = ( RegionType ) JOptionPane.showInputDialog ( gameView , "Scegli la regione di cui vuoi acquistare una carta dalla Banca!" + Utilities.CARRIAGE_RETURN + "Annulla per cambiare mossa" , PresentationMessages.APP_NAME , JOptionPane.QUESTION_MESSAGE , null , RegionType.allTheTypesExceptSheepsburg().toArray (), RegionType.HILL ) ;
				if ( type != null )
					try 
					{
						res = moveFactory.newBuyCard ( type ) ;
					}
					catch (MoveNotAllowedException e1) 
					{
						e1.printStackTrace();
						userWantsToChangeMove = true ;
					}
			break ;
			case MATE :
				gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
				generationNotification ( "Scegli la regione dove vuoi provare a far eseguire l'accoppiamento." );
				index.set ( null ) ;
				waitForAtomicVariable ( index ) ;
				if ( this.userWantsToChangeMove == false )		
				{	region = gameMap.getRegionByUID ( index.get () ) ;
					try 
					{
						res = moveFactory.newMate ( region ) ;
					}
					catch (MoveNotAllowedException e)
					{
						generationNotification ( "Sorry, ma questa mossa non può avvenire!\n"+e.getMessage () + "\nScegli un'altra regione!" ) ; 
						userWantsToChangeMove = true ;
					}		
				}
			break ;
			case MOVE_SHEEP :
				gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
				generationNotification ( "Scegli la regione da dove spostare l'ovino." ) ;
				index.set ( null ) ;
				waitForAtomicVariable ( index ) ;
				if ( this.userWantsToChangeMove  == false )
				{
					region = gameMap.getRegionByUID ( index.get() ) ;
					generationNotification ( "Ok, ora scegli un ovino da muovere" ) ;
					gameView.setInputMode ( GameViewInputMode.ANIMALS ) ;
					index.set ( null ) ;
					waitForAtomicVariable ( index ) ;
					if ( this.userWantsToChangeMove == false )
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
							res = moveFactory.newMoveSheep ( ( Ovine ) animal , region ) ;
						}
						catch (MoveNotAllowedException e) 
						{
							e.printStackTrace();
							userWantsToChangeMove = true ;
						}
					}	
				}
			break ;
			case MOVE_SHEPERD :
					gameView.setInputMode ( GameViewInputMode.REGIONS ) ;
					index.set(null) ;
					waitForAtomicVariable ( index ) ;
					if ( this.userWantsToChangeMove == false )
					{
						road = gameMap.getRoadByUID ( index.get () ) ;
						try
						{
							res = moveFactory.newMoveSheperd ( road ) ;
						}
						catch (MoveNotAllowedException e)
						{
							e.printStackTrace();
							userWantsToChangeMove = false ;
						}
					}
				break ;
				default :
					throw new RuntimeException () ;
			}
		}
		while ( userWantsToChangeMove == true ) ;
		return res ;	
	}
	
	// non gli passare le carte, passagli una rappresentazione ! -> card view !
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > playerCards ) 
	{
		Collection < SellableCard > res ;
		Collection < Card > in ;
		Iterable < Card > out ;
		res = new LinkedList < SellableCard > () ;
		in = new LinkedList < Card > () ;
		for ( SellableCard s : playerCards )
			in.add ( s ) ;
		generationNotification ( "Scegli le carte che vuoi vendere" );
		out = CardsChooseView.showDialog ( in , true ) ;
		for ( Card c : out )
			res.add ( ( SellableCard ) c ) ;
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
		SocketGUIMapClient c ;
		try 
		{
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
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > acquirables ) 
	{
		return null ;
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onGameConclusionNotification(String cause) throws IOException 
	{
		
	}
	
	/***/
	private void processGameViewIntResReceived ( int value , GameViewInputMode rightMode ) 
	{
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
