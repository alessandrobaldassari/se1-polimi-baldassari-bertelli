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

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

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
			msg =PresentationMessages.NAME_ACCEPTED_MESSAGE + Utilities.CARRIAGE_RETURN + notes ;
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
		writer.println ( PresentationMessages.MATCH_STARTING_MESSAGE ) ;
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
		s = PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE + Utilities.CARRIAGE_RETURN ;
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
		writer.println ( PresentationMessages.MATCH_WILL_NOT_START_MESSAGE + "\n" + msg ) ;
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
		s = PresentationMessages.CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE + Utilities.CARRIAGE_RETURN ;
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
		s = PresentationMessages.CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE + Utilities.CARRIAGE_RETURN ;
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
	public GameMove onDoMove ( MoveFactory f , GameMap m  ) throws IOException 
	{
		GameMove res = null ;
		String s ;
		int i ;
		boolean repeat ;
		do
		{
			s = "Situazione della mappa di gioco" + Utilities.CARRIAGE_RETURN ;
			s = s + m + Utilities.CARRIAGE_RETURN ;
			writer.println ( s ) ;
			s = PresentationMessages.DO_MOVE_MESSAGE + Utilities.CARRIAGE_RETURN ;
			s = s + "1. Uccidere un ovino in una regione" + Utilities.CARRIAGE_RETURN ;
			s = s + "2. Comperare una carta dalla banca" + Utilities.CARRIAGE_RETURN ;
			s = s + "3. Fare una accoppiamento" + Utilities.CARRIAGE_RETURN ;
			s = s + "4. Muovere una pecora." + Utilities.CARRIAGE_RETURN ;
			s = s + "5. Muovere il pastore" + Utilities.CARRIAGE_RETURN ;
			s = s + "-1. Esci da JSheepland" ;
			i = GraphicsUtilities.checkedIntInputWithEscape ( 1 , 5 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
			if ( i != -1 )
			{
				switch ( i ) 
				{
					case 1 :
						res = killing ( f , m ) ;
					break ;
					case 2 :
						res = buyCard ( f , m ) ;
					break ;
					case 3 :
						res = mate ( f , m ) ;
					break ;
					case 4 :
						res = moveOvine ( f , m ) ;
					break ;
					case 5 :
						res = moveSheperd ( f , m ) ;
					break ;
					default :
						res = null ;
					break ;
				}
				if ( res == null )
					repeat = true ;
				else
					repeat = false ;
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
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > sellableCards ) throws IOException 
	{
		List < SellableCard > availableCards ;
		List < SellableCard > res ;
		String s ;
		int i ;
		availableCards = CollectionsUtilities.newListFromIterable ( sellableCards ) ;
		res = new LinkedList < SellableCard > () ;
		do{
			s = "Scegli una carta che vuoi comprare:" ;
			s = s + "-1. Non voglio comprare alcuna carta." ;
			for ( i = 0 ; i < availableCards.size() ; i ++ )
				s = s + i + ". " + availableCards.get ( i ) ;
			i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , availableCards.size () - 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
			if ( i != -1 )
				res.add ( availableCards.get ( i ) ) ;
		}
		while ( i != -1 ) ;
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
	 */
	private GameMove killing ( MoveFactory f , GameMap m ) throws IOException 
	{ 
		List <Ovine> killableAnimals ;
		String s ;
		GameMove res ;
		Region r1, r2;
		killableAnimals = new LinkedList<Ovine>();
		int j;
		s = "Elenco degli ovini che puoi abbattere : " + Utilities.CARRIAGE_RETURN;
		r1 = f.getAssociatedSheperd().getPosition().getFirstBorderRegion();
		r2 = f.getAssociatedSheperd().getPosition().getSecondBorderRegion();
		for ( Animal animal : r1.getContainedAnimals () )
			if( animal instanceof Ovine )
				killableAnimals.add ( ( Ovine ) animal ) ;
		for ( Animal animal : r2.getContainedAnimals () )
			if ( animal instanceof Ovine )
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
				res = f.newBreakDownMove(killableAnimals.get(j));
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra!" ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}
	
	/**
	 * This message  
	 */
	private GameMove buyCard ( MoveFactory f , GameMap m ) throws IOException 
	{
		List < RegionType > regs ;
		String s ;
		GameMove res ;
		int i ;
		s = "Quale carta vuoi comprare ( regione ) ?" + Utilities.CARRIAGE_RETURN ;
		regs = new LinkedList < RegionType > ()  ;
		i = 0 ;
		for ( RegionType rt : RegionType.values () ) 
			if ( rt != RegionType.SHEEPSBURG )
			{
				s = s + i + ". " + rt + Utilities.CARRIAGE_RETURN ;
				regs.add ( rt );
				i ++ ;	
			}
		s = s + "-1. Cambia mossa" + Utilities.CARRIAGE_RETURN ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , regs.size() - 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader) ;
		if ( i != -1 )
			try 
			{
				res = f.newBuyCard ( regs.get ( i ) ) ;
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}

	/**
	 * This message manage the mate action. 
	 */
	private GameMove mate ( MoveFactory f , GameMap m ) throws IOException 
	{
		GameMove res ;
		String s ;
		int i ;
		s = "Regioni dove puoi compire l'accoppiamento : " + Utilities.CARRIAGE_RETURN ;
		s = s + "0 . " + f.getAssociatedSheperd().getPosition().getFirstBorderRegion() + Utilities.CARRIAGE_RETURN;
		s = s + "1 . " + f.getAssociatedSheperd().getPosition().getSecondBorderRegion() + Utilities.CARRIAGE_RETURN;
		s = s + "-1. Scegli un'altra mossa." ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , 1 , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ; 
		if ( i != -1 )
			try 
			{
				res = f.newMate ( i == 0 ? f.getAssociatedSheperd().getPosition().getFirstBorderRegion() : f.getAssociatedSheperd().getPosition().getSecondBorderRegion() ) ;
			}
			catch ( MoveNotAllowedException e1 ) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}
	
	/**
	 * This method manage the moveOvine action. 
	 */
	private GameMove moveOvine ( MoveFactory f , GameMap m ) throws IOException 
	{
		List < Ovine > movableAnimals ;
		Region selReg ;
		GameMove res ;
		Sheperd sh ;
		Region r3 ;
		Region r4;
		String s ;
		int i ;
		movableAnimals = new LinkedList<Ovine>();
		sh = f.getAssociatedSheperd () ;
		s = "Elenco degli ovini che puoi spostare" + Utilities.CARRIAGE_RETURN ;
		r3 = sh.getPosition().getFirstBorderRegion () ;
		r4 = sh.getPosition().getSecondBorderRegion () ;
		for ( Animal animal : r3.getContainedAnimals () )
			if ( animal instanceof Ovine )
				movableAnimals.add ( ( Ovine ) animal ) ;
		for ( Animal animal : r4.getContainedAnimals () )
			if ( animal instanceof Ovine )
				movableAnimals.add ( ( Ovine ) animal ) ;
		i = 0 ;
		for ( Ovine ovine : movableAnimals )
		{
			s = s + i + ". " +ovine + Utilities.CARRIAGE_RETURN;
			i ++ ;
		}
		s = s + "Chi vuoi muovere ? " + Utilities.CARRIAGE_RETURN ;
		s = s + "-1. Voglio fare un'altra mosssa" ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 0 , movableAnimals.size () - 1 , -1 , -2 , s , "Scelta non valida." , writer , reader ) ;
		if ( i != -1 )
			try 
			{
				selReg =  CollectionsUtilities.contains ( sh.getPosition().getFirstBorderRegion ().getContainedAnimals (), movableAnimals.get ( i )) ? sh.getPosition().getSecondBorderRegion() : sh.getPosition().getFirstBorderRegion() ;
				res = f.newMoveSheep(movableAnimals.get ( i ) , selReg );
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}
	
	/**
	 * This method manages the moveSheperd action. 
	 */
	private GameMove moveSheperd ( MoveFactory f , GameMap m ) throws IOException 
	{
		List < Road > l ;
		String s ;
		GameMove res ;
		int i ;
		l = CollectionsUtilities.newListFromIterable ( m.getFreeRoads () ) ;
		s =  "Strade ok : " + Utilities.CARRIAGE_RETURN ;
		for ( i = 0 ; i < l.size() ; i ++ )
			s = s + i+1 + " : strada : " + l.get( i ) + Utilities.CARRIAGE_RETURN ;
		s = s + "In quale strada vuoi andare ? " + Utilities.CARRIAGE_RETURN ;
		s = s + "-1. Altra mossa" ;
		i = GraphicsUtilities.checkedIntInputWithEscape ( 1 , l.size() , -1 , -2 , s , PresentationMessages.INVALID_CHOOSE_MESSAGE , writer , reader ) ;
		if ( i != -1 )
			try
			{
				res = f.newMoveSheperd ( l.get( i - 1 ) ) ;
			}
			catch (MoveNotAllowedException e) 
			{
				writer.println ( PresentationMessages.INVALID_CHOOSE_MESSAGE + Utilities.CARRIAGE_RETURN + "Provane un'altra !" ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
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
