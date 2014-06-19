package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.cli;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerWantsToExitGameException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;

/**
 * The CLI GUI implementation 
 */
public class CLIController extends ViewPresenter
{
	
	/**
	 * The time to wait after that shutdown the program. 
	 */
	public static final long DOWN_DELAY = 5 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/**
	 * A BufferedReader object to retrieve the user's input 
	 */
	private BufferedReader reader ;
	
	/**
	 * A PrintStream object to send output to users. 
	 */
	private PrintStream writer ;
	
	/**
	 * A component to manage thread issues. 
	 */
	private Executor executorService ;
	
	private String myName ;
	
	/***/
	public CLIController () 
	{
		super () ;
		reader = new BufferedReader ( new InputStreamReader ( System.in ) ) ;
		writer = System.out ;
		executorService = Executors.newCachedThreadPool () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void startApp () 
	{
		writer.println ( PresentationMessages.WELCOME_MESSAGE ) ;
		writer.println ( PresentationMessages.SERVER_CONNECTION_MESSAGE ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void onTermination () throws IOException 
	{
		writer.println ( PresentationMessages.BYE_MESSAGE ) ;
		reader.close () ;
		try 
		{
			Thread.sleep ( DOWN_DELAY ) ;
		}
		catch ( InterruptedException e ) 
		{
			throw new RuntimeException () ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String onNameRequest () 
	{
		String res = null ;
		try 
		{
			writer.println ( PresentationMessages.NAME_REQUEST_MESSAGE ) ;
			res = reader.readLine ().trim () ;
			
			while ( res == null || res.compareTo ( Utilities.EMPTY_STRING ) == 0 )
			{	
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN ) ;
				writer.println ( PresentationMessages.NAME_REQUEST_MESSAGE ) ;
				res = reader.readLine ().trim () ;
				myName = res ;
			}
			writer.println ( PresentationMessages.NAME_VERIFICATION_MESSAGE ) ;
		} 
		catch (IOException e) 
		{
			res = null ;
			stopApp () ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameRequestAck ( boolean isOk , String notes ) 
	{
		String msg ;
		if ( isOk ) 
			msg = PresentationMessages.NAME_ACCEPTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes ;
		else
			msg = PresentationMessages.NAME_REJECTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes ;
		writer.println ( msg );
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNotifyMatchStart () 
	{
		writer.println ( myName + " : " +  PresentationMessages.MATCH_STARTING_MESSAGE ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) throws IOException 
	{
		List < NamedColor > colors ;
		String s ;
		NamedColor res ;
		int i ;
		colors = CollectionsUtilities.newListFromIterable ( availableColors ) ;
		s = myName + " : " +  PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE + Utilities.CARRIAGE_RETURN ;
		i = 0 ;
		for ( Color c : colors )
		{
			s = s + i + ". " + c + Utilities.CARRIAGE_RETURN ;
			i ++ ;
		}
		i = GraphicsUtilities.checkedIntInputWithoutEscape ( 0 , colors.size () - 1 , -1 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		res = colors.get ( i ) ;
		System.out.println ( "Colore scelto : " + res.getName () ) ;			
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMatchWillNotStartNotification ( String msg ) 
	{
		writer.println ( myName + " : " +  PresentationMessages.MATCH_WILL_NOT_START_MESSAGE + "\n" + msg ) ;
		executorService.execute ( new DownAction () ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException 
	{
		List < Road > availableRoadsList ;
		Road res ;
		String s ;
		int i ;
		availableRoadsList = CollectionsUtilities.newListFromIterable  ( availableRoads ) ;
		i = 0 ;
		s = myName + " : " + PresentationMessages.CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE + Utilities.CARRIAGE_RETURN ;
		for ( i = 0 ; i < availableRoadsList.size () ; i ++ )
			s = s + i + ". " + availableRoadsList.get ( i ++ ) + Utilities.CARRIAGE_RETURN ;
		s = s + "-1. Esci da JSheepland" + Utilities.CARRIAGE_RETURN ;
		i = GraphicsUtilities.checkedIntInputWithoutEscape ( 0 , availableRoadsList.size ()-1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		res = availableRoadsList.get ( i ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > playersSheperd ) throws IOException 
	{
		List < Sheperd > sheperds ;
		Sheperd res ;
		String s ;
		int i ;
		sheperds = CollectionsUtilities.newListFromIterable ( playersSheperd ) ;
		s = myName + " : " +  PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE + Utilities.CARRIAGE_RETURN ;
		i = 0 ;
		for ( Sheperd sh : sheperds )
		{
			s = s + i + ".  " + sh + Utilities.CARRIAGE_RETURN ;
			i ++ ;
		}
		i = GraphicsUtilities.checkedIntInputWithoutEscape ( 0 , sheperds.size () - 1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		res = sheperds.get( i ) ;
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public MoveSelection onDoMove ( MoveSelector f , GameMap m  ) throws IOException 
	{
		MoveSelection res = null ;
		String s ;
		int i ;
		boolean repeat ;
		do
		{
			s = myName + " : \nTuoi soldi : " + f.getAvailableMoney();
			s = s + "Situazione della mappa di gioco" + Utilities.CARRIAGE_RETURN ;
			s = s + m + Utilities.CARRIAGE_RETURN ;
			writer.println ( s ) ;
			s = PresentationMessages.DO_MOVE_MESSAGE + Utilities.CARRIAGE_RETURN ;
			i = 1 ;
			for ( GameMoveType g : GameMoveType.values() )
			{
				if ( f.isMoveAllowed ( g ) )
					s = s + i + g.getHumanName () + Utilities.CARRIAGE_RETURN ;
				i ++ ;
			}
			s = s + "-1. Esci da JSheepland" ;
			i = GraphicsUtilities.checkedIntInputWithEscape ( 1 , 5 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
			if ( i != -1 )
			{
				try 
				{
					switch ( i ) 
					{
						case 1 :
							killing ( f , m ) ;
						break ;
						case 2 :
							buyCard ( f , m ) ;
						break ;
						case 3 :
							mate ( f , m ) ;
						break ;
						case 4 :
							moveOvine ( f , m ) ;
						break ;
						case 5 :
							moveSheperd ( f , m ) ;
						break ;
						default :
							res = null ;
						break ;
					}
					try 
					{
						res = f.getSelectedMove () ;
						repeat = false ;
					}
					catch (WrongStateMethodCallException e) 
					{
						repeat = true ;
						e.printStackTrace();
					}
				}
				catch ( PlayerWantsToExitGameException p  )
				{
					repeat = true ;
				}
			}
			else
			{
				repeat = false ;
				res = null ; 
				stopApp () ;
			}
		}
		while ( repeat ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > sellableCards ) throws IOException 
	{
		List < SellableCard > res ;
		res = new LinkedList < SellableCard > () ;
		String msg ;
		int i ;
		int j ;
		if ( CollectionsUtilities.iterableSize ( sellableCards ) > 0 )
		{
			msg = myName + " : scegli le carte che vuoi vendere" ; 
			writer.println ( msg ) ;
			for ( SellableCard s : sellableCards )
			{
				msg = "Tua carta : " + s + Utilities.CARRIAGE_RETURN + "Vuoi venderla ? ( 1 : s / 2 : n )" ;
				i = GraphicsUtilities.checkedIntInputWithoutEscape ( 1 , 2 , -1 , msg , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
				if ( i == 1 )
				{
					s.setSellable ( true ) ;
					msg = "Quanto vuoi chiedere per questa carta ? " + Utilities.CARRIAGE_RETURN ;
					j = GraphicsUtilities.checkedIntInputWithoutEscape ( 1 , 5 , -1 , msg , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
					s.setSellingPrice ( j ) ;
					res.add ( s ) ;
				}
				else
					s.setSellable ( false ) ;
			}
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > sellableCards , Integer playerMoney ) throws IOException 
	{
		List < SellableCard > availableCards ;
		List < SellableCard > res ;
		String s ;
		int i ;
		availableCards = CollectionsUtilities.newListFromIterable ( sellableCards ) ;
		res = new LinkedList < SellableCard > () ;
		int sum  ;
		boolean exit ;
		if ( CollectionsUtilities.iterableSize ( sellableCards ) > 0 )
		{
			do
			{
				sum = 0 ;
				do
				{
					s = myName + " : \nTuoi soldi : " + playerMoney + Utilities.CARRIAGE_RETURN +  "Scegli una carta che vuoi comprare:" ;
					s = s + "-1. Non voglio comprare alcuna carta." ;
					for ( i = 0 ; i < availableCards.size() ; i ++ )
						s = s + i + ". " + availableCards.get ( i ) ;
					i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , availableCards.size () - 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
					if ( i != -1 )
					{
						res.add ( availableCards.get ( i ) ) ;
						try 
						{
							sum = sum + availableCards.get ( i ).getSellingPrice () ;
						}
						catch (NotSellableException e) 
						{	
							e.printStackTrace();
						}
						catch (SellingPriceNotSetException e) 
						{
							e.printStackTrace();
						}
					}
				}
				while ( i != -1 ) ;
				if ( sum > playerMoney )
				{
					writer.println ( "Sorry, ma non hai abbastanza soldi !" ) ;
					i = GraphicsUtilities.checkedIntInputWithoutEscape ( 1 , 2 , -1 , "Vuoi riselezionare le carte ( 1 ) o non comperarne alcuna ( 2 ) ?" , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer, reader ) ;
					if ( i == 1 )
						exit = false ;
					else
						exit = true ;
					res.clear();
				}
				else
					exit = true ;
			}
			while ( ! exit ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void generationNotification ( String msg ) 
	{
		writer.println ( msg ) ;
	}
	
	/**
	 * This method manages the killing action. 
	 * @throws PlayerWantsToExitGameException 
	 */
	private void killing ( MoveSelector f , GameMap m ) throws IOException, PlayerWantsToExitGameException  
	{ 
		List <Ovine> killableAnimals ;
		String s ;
		killableAnimals = new LinkedList<Ovine>();
		int j;
		s = "Elenco degli ovini che puoi abbattere : " + Utilities.CARRIAGE_RETURN;
		for ( Region region : f.getAvailableRegionsForBreakdown () )
			for ( Animal animal : region.getContainedAnimals() )
				if( animal instanceof Ovine && ! ( animal instanceof BlackSheep ) )
					killableAnimals.add ( ( Ovine ) animal ) ;
		j=0;
		for ( Ovine ovine : killableAnimals )
		{
			s = s + j + ". " +ovine + Utilities.CARRIAGE_RETURN;
			j++;
		}
		s = s + "Chi vuoi abbattere ? " + Utilities.CARRIAGE_RETURN + "-1. Cambia mossa";
		j = GraphicsUtilities.checkedIntInputWithEscape ( 0 , killableAnimals.size() - 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		if ( j != -1 )
			try 
			{
				f.selectBreakdown ( killableAnimals.get ( j ) ) ;
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra!" ) ;
			}
		else
			throw new PlayerWantsToExitGameException ( "" ) ;
	}
	
	/**
	 * This message  
	 * @throws PlayerWantsToExitGameException 
	 */
	private void buyCard ( MoveSelector f , GameMap m ) throws IOException, PlayerWantsToExitGameException 
	{
		String s ;
		int i ;
		s = "Quale carta vuoi comprare ( regione ) ?" + Utilities.CARRIAGE_RETURN ;
		i = 0 ;
		for ( RegionType rt : f.getAvailableRegionsForBuyCard().keySet() ) 
		{
			s = s + i + ". " + rt + " [ " + f.getAvailableRegionsForBuyCard().get ( rt ) + " denari ] " + Utilities.CARRIAGE_RETURN ;
			i ++ ;
		}	
		s = s + "-1. Cambia mossa" + Utilities.CARRIAGE_RETURN ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , i , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader) ;
		if ( i != -1 )
			try 
			{
				f.selectBuyCard ( CollectionsUtilities.newListFromIterable ( f.getAvailableRegionsForBuyCard().keySet () ).get ( i ) ) ;
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
			}
		else
			throw new PlayerWantsToExitGameException () ;
	}

	/**
	 * This message manage the mate action. 
	 * @throws PlayerWantsToExitGameException 
	 */
	private void mate ( MoveSelector f , GameMap m ) throws IOException, PlayerWantsToExitGameException 
	{
		String s ;
		int i ;
		s = "Regioni dove puoi compire l'accoppiamento : " + Utilities.CARRIAGE_RETURN ;
		i = 0 ;
		for ( Region r : f.getAvailableRegionsForMate () )
		{
			s = s + i + " . " + r + Utilities.CARRIAGE_RETURN;
			i ++ ;
		}
		s = s + "-1. Scegli un'altra mossa." ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ; 
		if ( i != -1 )
			try 
			{
				f.selectMate ( CollectionsUtilities.newListFromIterable ( f.getAvailableRegionsForMate() ).get ( i ) ) ;
			}
			catch ( MoveNotAllowedException e1 ) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
			}
		else
			throw new PlayerWantsToExitGameException();
	}
	
	/**
	 * This method manage the moveOvine action. 
	 * @throws PlayerWantsToExitGameException 
	 */
	private void moveOvine ( MoveSelector f , GameMap m ) throws IOException, PlayerWantsToExitGameException 
	{
		List < Ovine > movableAnimals ;
		String s ;
		int i ;
		movableAnimals = new LinkedList<Ovine>();
		s = "Elenco degli ovini che puoi spostare" + Utilities.CARRIAGE_RETURN ;
		for ( Region r : f.getAvailableRegionsForMoveSheep () )
			for ( Animal animal : r.getContainedAnimals() )
				if ( animal instanceof Ovine )
					movableAnimals.add ( ( Ovine ) animal ) ;
		i = 0 ;
		for ( Ovine ovine : movableAnimals )
		{
			s = s + i + ". " + ovine + Utilities.CARRIAGE_RETURN;
			i ++ ;
		}
		s = s + "Chi vuoi muovere ? " + Utilities.CARRIAGE_RETURN ;
		s = s + "-1. Voglio fare un'altra mosssa" ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , movableAnimals.size () - 1 , -1 , -2 , s , "Scelta non valida." , writer , reader ) ;
		if ( i != -1 )
			try 
			{
				f.selectMoveSheep ( movableAnimals.get ( i ) , movableAnimals.get ( i ).getPosition () );
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
			}
		else
			throw new PlayerWantsToExitGameException () ;
	}
	
	/**
	 * This method manages the moveSheperd action. 
	 * @throws PlayerWantsToExitGameException 
	 */
	private void moveSheperd ( MoveSelector f , GameMap m ) throws IOException, PlayerWantsToExitGameException 
	{
		String s ;
		int i ;
		s =  "Strade ok : " + Utilities.CARRIAGE_RETURN ;
		i = 0 ;
		for ( Road r : f.getAvailableRoadsForMoveSheperd () )
		{
			s = s + i + " : strada : " + r + Utilities.CARRIAGE_RETURN ;
			i ++ ;
		}
			s = s + "In quale strada vuoi andare ? " + Utilities.CARRIAGE_RETURN ;
		s = s + "-1. Altra mossa" ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 1 , i - 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		if ( i != -1 )
			try
			{
				f.selectMoveSheperd ( CollectionsUtilities.newListFromIterable ( f.getAvailableRoadsForMoveSheperd() ).get ( i ) ) ;
			}
			catch (MoveNotAllowedException e) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
			}
		else
			throw new PlayerWantsToExitGameException();
	}

	@Override
	public void onGameConclusionNotification ( String cause ) throws IOException 
	{
		writer.println ( cause ) ;
		stopApp () ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * Obviously, the CLI does not do anything with this functionality.
	 */
	@Override
	public void onGUIConnectorOnNotification ( Serializable guiConnector ) {}
	
	// INNNER CLASSES
	
	/**
	 * Runnable that implements the ShutDownApp action. 
	 */
	private class DownAction implements Runnable 
	{

		/**
		 * AS THE SUPER'S ONE.
		 */
		@Override
		public void run () 
		{
			stopApp () ;
		}
		
	}

	
	
}
