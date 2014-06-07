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

	/***/
	private BufferedReader reader ;
	
	/***/
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
	public void startApp () {}
	
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
		s = "Prego, inserisci il numero del colore che vuoi scegliere per il tuo pastore" ;
		i = 1 ;
		for ( Color c : colors )
		{
			s = s + i + ". " + c.toString () ;
			i ++ ;
		}
		s = s + "-1. Esci da JSheepland." ;
		i = GraphicsUtilities.checkedIntInput ( 1 , colors.size() , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != - 1 )
		{
			res = colors.get ( i - 1 ) ;
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
		s = "Scegli uno dei tuoi pastori per questo turno:" ;
		s = s + "Scegli uno dei tuoi pastori per questo turno:\n1. Il primo\n2. Il secondo" ;
		i = GraphicsUtilities.checkedIntInput ( 1 , 2 , -1 , -2 , s , "Scelta non valida" , writer , reader ) ;
		if ( i != -1 )
			res = sheperds.get(i);
		else
		{
			down () ;
			throw new RuntimeException () ;
		}
		return sheperds.get ( i - 1 ) ;
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
		char c ;
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
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > sellableCards ) 
	{
		List < SellableCard > l ;
		SellableCard res ;
		int i ;
		l = CollectionsUtilities.newListFromIterable ( sellableCards ) ;
		try 
		{
			writer.println ( "Scegli la carta che vuoi comprare:" ) ;
			writer.println ( "0. Non voglio comprare alcuna carta." ) ;
	 		for ( i = 0 ; i < l.size() ; i ++ )
				writer.println ( l.get ( i ) ) ;
			i = Integer.parseInt ( reader.readLine () ) ;
		}
		catch (NumberFormatException e) {
			i = 0;
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			i = 0 ;
			e.printStackTrace();
		}
		if ( i == 0 )
			res = null ;
		else
			res = l.get ( i - 1 ) ;
		return res ;
	}

	@Override
	public GameMove onDoMove ( MoveFactory f , GameMap m  ) 
	{
		GameMove res = null ;
		int i = 0 ;
		writer.println ( "Situazione della mappa di gioco : " ) ;
		writer.println ( m ) ;
		writer.println ( "Quale mossa vuoi effetture?" ) ;
		writer.println ( "1. Uccidere un ovino in una regione" ) ;
		writer.println ( "2. Comperare una carta dalla banca" ) ;
		writer.println ( "3. Fare una accoppiamento" ) ;
		writer.println ( "4. Muovere una pecora." ) ;
		writer.println ( "5. Muovere il pastore" ) ;
		try 
		{
			i = Integer.parseInt ( reader.readLine () ) ;
		}
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		switch ( i ) 
		{
			case 1 :
				Region r1, r2;
				List <Ovine> killableAnimals = new LinkedList<Ovine>();
				int j;
				writer.println("Elenco degli ovini che può abbattere");
				r1 = f.getAssociatedSheperd().getPosition().getFirstBorderRegion();
				r2 = f.getAssociatedSheperd().getPosition().getSecondBorderRegion();
				for(Animal animal : r1.getContainedAnimals())
					if(animal instanceof Ovine)
						killableAnimals.add((Ovine) animal);
				for(Animal animal : r2.getContainedAnimals())
					if(animal instanceof Ovine)
						killableAnimals.add((Ovine) animal);
				j=0;
				for(Ovine ovine : killableAnimals){
					writer.println(j + " " +ovine.toString());
					j++;
				}
				writer.println("Chi vuoi abbattere?");
			try {
				j = Integer.parseInt(reader.readLine().trim());
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				f.newBreakDownMove(killableAnimals.get(j));
			} catch (MoveNotAllowedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			break ;
			case 2 :
				int k = 0;
				for(RegionType rt : RegionType.values()){
					writer.println(k + " " + rt);
					k++;	
				}
			try 
			{
				f.newBuyCard(RegionType.values()[k]);
			} 
			catch (MoveNotAllowedException e1) 
			{
				e1.printStackTrace();
			}
			break ;
			case 3 :
				int u ;
				writer.println("Regioni dove puoi compire l'accoppiamento:") ;
				writer.println("0 . " + f.getAssociatedSheperd().getPosition().getFirstBorderRegion());
				writer.println("1 . " + f.getAssociatedSheperd().getPosition().getSecondBorderRegion());
			u = 0 ;
			try {
				u = Integer.parseInt ( reader.readLine ().trim() );
			} catch (NumberFormatException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				f.newMate ( u == 0 ? f.getAssociatedSheperd().getPosition().getFirstBorderRegion() : f.getAssociatedSheperd().getPosition().getSecondBorderRegion() ) ;
			} catch (MoveNotAllowedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break ;
			case 4 :
				Region r3, r4;
				List <Ovine> movableAnimals = new LinkedList<Ovine>();
				int h;
				writer.println("Elenco degli ovini che può abbattere");
				r3 = f.getAssociatedSheperd().getPosition().getFirstBorderRegion();
				r4 = f.getAssociatedSheperd().getPosition().getSecondBorderRegion();
				for(Animal animal : r3.getContainedAnimals())
					if(animal instanceof Ovine)
						movableAnimals.add((Ovine) animal);
				for(Animal animal : r4.getContainedAnimals())
					if(animal instanceof Ovine)
						movableAnimals.add((Ovine) animal);
				h=0;
				for(Ovine ovine : movableAnimals){
					writer.println(h + " " +ovine.toString());
					h++;
				}
				writer.println("Chi vuoi muovere?");
			try {
				h = Integer.parseInt(reader.readLine().trim());
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				f.newMoveSheep(movableAnimals.get(h), CollectionsUtilities.contains(f.getAssociatedSheperd().getPosition().getFirstBorderRegion().getContainedAnimals(), movableAnimals.get(h)) ? f.getAssociatedSheperd().getPosition().getSecondBorderRegion() : f.getAssociatedSheperd().getPosition().getFirstBorderRegion());
			} catch (MoveNotAllowedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break ;
			case 5 :
				List < Road > l ;
				l = CollectionsUtilities.newListFromIterable ( m.getFreeRoads () ) ;
				writer.println ( "Strade ok :" ) ;
				for ( i = 0 ; i < l.size() ; i ++ )
					writer.println( i+1 + " : strada : " + l.get(i).toString()) ;
				writer.println( "In quale strada vuoi andare ?" ) ;
				try 
				{
					i = Integer.parseInt ( reader.readLine () ) ;
					res = f.newMoveSheperd ( l.get(i) ) ;
				} 
				catch (MoveNotAllowedException e) 
				{
					res = null ;
					e.printStackTrace();
				} 	
				catch ( NumberFormatException e ) 
				{
					res = null ;
					e.printStackTrace();
				} 
				catch ( IOException e ) 
				{
					res = null ;
					e.printStackTrace();
				}
			break ;
			default :
				res = null ;
			break ;
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
	
	/***/
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
