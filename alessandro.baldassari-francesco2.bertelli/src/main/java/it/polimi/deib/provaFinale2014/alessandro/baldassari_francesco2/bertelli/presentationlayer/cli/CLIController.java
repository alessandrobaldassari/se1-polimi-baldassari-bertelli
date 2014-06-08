package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.cli;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/***/
public class CLIController extends ViewPresenter
{

	public static final String CANNOT_DO_THIS_MOVE_EXCEPTION = "Spiacenti, ma non puoi fare questa mossa." ;
	
	/**
	 * A BufferedReader object to retrieve the user's input 
	 */
	private BufferedReader reader ;
	
	/**
	 * A PrintStream object to send output to users. 
	 */
	private PrintStream writer ;
	
	/***/
	public CLIController () 
	{
		super () ;
		reader = new BufferedReader ( new InputStreamReader ( System.in ) ) ;
		writer = System.out ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void startApp () 
	{
		writer.println("Benvenuto in JSheepland.") ;
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
			writer.println ( "Prego, inserisci il nome con cui vuoi giocare : " );
			res = reader.readLine ().trim () ;
			writer.println ( "Attendi che il Server controlli se il tuo nome è ok." ) ;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNotifyMatchStart () 
	{
		writer.println ( "Tutti i giocatori sono arrivati.\nIl gioco sta per cominciare." ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameRequestAck ( boolean isOk , String notes ) 
	{
		if ( isOk ) 
			writer.println ( "Nome valido.\nAttendi gli altri giocatori!" ) ;
		else
			writer.println( notes ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onMatchWillNotStartNotification ( String msg ) 
	{
		try 
		{
			System.exit ( 0 ) ; 
			writer.println ( msg ) ;
			terminateClient () ;
		}
		catch ( WrongStateMethodCallException e ) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 * @throws IOException 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) throws IOException 
	{
		List < NamedColor > colors = CollectionsUtilities.newListFromIterable ( availableColors ) ;
		String s ;
		NamedColor res ;
		int i ;
		s = "Prego, inserisci il numero del colore che vuoi scegliere per il tuo pastore\n" ;
		i = 0 ;
		for ( Color c : colors )
		{
			s = s + i + ". " + c + "\n" ;
			i ++ ;
		}
		s = s + "-1. Esci da JSheepland." ;
		i = GraphicsUtilities.checkedIntInput ( 0 , colors.size () - 1 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != - 1 )
		{
			res = colors.get ( i ) ;
			System.out.println ( "Colore scelto : " + res.getName () ) ;			
		}
		else
		{
			down () ;
			throw new RuntimeException () ;
		}	
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 * @throws IOException 
	 */
	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > playersSheperd ) throws IOException 
	{
		List < Sheperd > sheperds ;
		Sheperd res ;
		String s ;
		int i ;
		sheperds = CollectionsUtilities.newListFromIterable ( playersSheperd ) ;
		s = "Scegli uno dei tuoi pastori per questo turno:\n " ;
		i = 0 ;
		for ( Sheperd sh : sheperds )
		{
			s = s + i + ". + " + sh + "\n" ;
			i ++ ;
		}
		i = GraphicsUtilities.checkedIntInput ( 0 , 1 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != -1 )
			res = sheperds.get( i ) ;
		else
		{
			down () ;
			throw new RuntimeException () ;
		}
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
			msg = "Tua carta : " + s + "\n" + "Vuoi venderla ? ( 1 : s / 2 : n )" ;
			i = GraphicsUtilities.checkedIntInput ( 1 , 2 , -1 , -2 , msg , "Scelta non valida" , writer , reader ) ;
			if ( i == 1 )
			{
				s.setSellable ( true ) ;
				msg = "Quanto vuoi chiedere per questa carta ? " ;
				j = GraphicsUtilities.checkedIntInput ( 1 , 5 , -1 , -2 , msg , "Scelta non valida" , writer , reader ) ;
				s.setSellingPrice ( j ) ;
				res.add(s);
			}
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > sellableCards ) throws IOException 
	{
		List < SellableCard > l ;
		SellableCard res ;
		String s ;
		int i ;
		l = CollectionsUtilities.newListFromIterable ( sellableCards ) ;
			s = "Scegli la carta che vuoi comprare:" ;
			s = s + "-1. Non voglio comprare alcuna carta." ;
	 		for ( i = 0 ; i < l.size() ; i ++ )
				s = s + i + ". " + l.get ( i ) ;
			i = Integer.parseInt ( reader.readLine () ) ;
		i = GraphicsUtilities.checkedIntInput ( 0 , l.size () - 1 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i == -1 )
			res = null ;
		else
			res = l.get ( i ) ;
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
		s = "Situazione della mappa di gioco :\n" ;
		s = s +m + "\n" ;
		s = s + "Quale mossa vuoi effetture?\n" ;
		s = s + "1. Uccidere un ovino in una regione\n" ;
		s = s + "2. Comperare una carta dalla banca\n" ;
		s = s + "3. Fare una accoppiamento\n" ;
		s = s + "4. Muovere una pecora.\n" ;
		s = s + "5. Muovere il pastore\n" ;
		i = GraphicsUtilities.checkedIntInput ( 1 , 5 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != -1 )
		{
			switch ( i ) 
			{
				case 1 :
					res = killing(f, m);
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
		}
		return res ;
	}

	private GameMove killing ( MoveFactory f , GameMap m ) throws IOException 
	{
		String s ;
		GameMove res ;
		Region r1, r2;
		List <Ovine> killableAnimals = new LinkedList<Ovine>();
		int j;
		s = "Elenco degli ovini che può abbattere";
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
			s = s + j + ". " +ovine + "\n";
			j++;
		}
		s = s + "Chi vuoi abbattere?";
		j = GraphicsUtilities.checkedIntInput ( 0 , killableAnimals.size() - 1 , -1 , -2 , s , "Scelta errata." , writer , reader ) ;
		try 
		{
			res = f.newBreakDownMove(killableAnimals.get(j));
		} 
		catch (MoveNotAllowedException e1) 
		{
			writer.println ( CANNOT_DO_THIS_MOVE_EXCEPTION ) ;
			res = null ;
		}		
		return res ;
	}
	
	/***/
	private GameMove buyCard ( MoveFactory f , GameMap m ) throws IOException 
	{
		List < RegionType > regs ;
		String s ;
		GameMove res ;
		int i ;
		s = "Quale carta vuoi comprare ( regione ) ?\n" ;
		regs = new LinkedList < RegionType > ()  ;
		i = 0 ;
		for ( RegionType rt : RegionType.values () ) 
			if ( rt != RegionType.SHEEPSBURG )
			{
				s = s + i + ". " + rt ;
				regs.add ( rt );
				i ++ ;	
			}
		i = GraphicsUtilities.checkedIntInput ( 0 , regs.size() - 1 , -1 , -2 , s , "Scelta non valida." , writer , reader) ;
		if ( i != -1 )
			try 
			{
				res = f.newBuyCard ( regs.get ( i ) ) ;
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( CANNOT_DO_THIS_MOVE_EXCEPTION ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}

	/***/
	private GameMove mate ( MoveFactory f , GameMap m ) throws IOException 
	{
		GameMove res ;
		String s ;
		int i ;
		s = "Regioni dove puoi compire l'accoppiamento:" ;
		s = s + "0 . " + f.getAssociatedSheperd().getPosition().getFirstBorderRegion() + "\n";
		s = s + "1 . " + f.getAssociatedSheperd().getPosition().getSecondBorderRegion() + "\n";
		i = GraphicsUtilities.checkedIntInput ( 0 , 1 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ; 
		if ( i != -1 )
			try 
			{
				res = f.newMate ( i == 0 ? f.getAssociatedSheperd().getPosition().getFirstBorderRegion() : f.getAssociatedSheperd().getPosition().getSecondBorderRegion() ) ;
			}
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( CANNOT_DO_THIS_MOVE_EXCEPTION ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}
	
	/***/
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
		s = "Elenco degli ovini che puoi spostare\n" ;
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
			s = s + i + ". " +ovine + "\n";
			i ++ ;
		}
		s = s + "Chi vuoi muovere?" ;
		i = GraphicsUtilities.checkedIntInput ( 0 , movableAnimals.size () - 1 , -1 , -2 , s , "Scelta non valida." , writer , reader ) ;
		if ( i != -1 )
			try 
			{
				selReg =  CollectionsUtilities.contains ( sh.getPosition().getFirstBorderRegion ().getContainedAnimals (), movableAnimals.get ( i )) ? sh.getPosition().getSecondBorderRegion() : sh.getPosition().getFirstBorderRegion() ;
				res = f.newMoveSheep(movableAnimals.get ( i ) , selReg );
			} 
			catch (MoveNotAllowedException e1) 
			{
				writer.println ( CANNOT_DO_THIS_MOVE_EXCEPTION ) ;
				res = null ;
			}
		else
			res = null ;
		return res ;
	}
	
	/***/
	private GameMove moveSheperd ( MoveFactory f , GameMap m ) throws IOException 
	{
		List < Road > l ;
		String s ;
		GameMove res ;
		int i ;
		l = CollectionsUtilities.newListFromIterable ( m.getFreeRoads () ) ;
		s =  "Strade ok :" ;
		for ( i = 0 ; i < l.size() ; i ++ )
			s = s + i+1 + " : strada : " + l.get(i) ;
		s = s + "In quale strada vuoi andare ?" ;
		i = GraphicsUtilities.checkedIntInput ( 1 , l.size() , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != -1 )
			try
			{
				res = f.newMoveSheperd ( l.get( i - 1 ) ) ;
			}
			catch (MoveNotAllowedException e) 
			{
				writer.println ( CANNOT_DO_THIS_MOVE_EXCEPTION ) ;
				res = null ;
			}
		else
			res = null ;
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
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitRoadForSheperd(Iterable<Road> availableRoads) throws IOException 
	{
		Road res ;
		List < Road > l ;
		String s ;
		int i ;
		l = CollectionsUtilities.newListFromIterable  ( availableRoads ) ;
		i = 0 ;
		s = "Scegli la regione di partenza per il tuo pastore tra quelle disponibili:" ;
		for ( i = 0 ; i < l.size () ; i ++ )
		{
			s = s + i + ". " + l.get ( i ) ;
			i ++ ;
		}
		s = s + "-1. Esci da JSheepland" ;
		i = GraphicsUtilities.checkedIntInput ( 0 , l.size () - 1, -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != -1 )
			res = l.get ( i ) ;
		else
		{
			down () ;
			throw new RuntimeException () ;
		}
		return res ;
	}
	
	/**
	 * Shut down this component.
	 * 
	 * @throws IOException always.
	 */
	private void down () throws IOException 
	{
		try 
		{
			terminateClient();
			throw new IOException () ;
		} 
		catch ( WrongStateMethodCallException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
	}
	
}
