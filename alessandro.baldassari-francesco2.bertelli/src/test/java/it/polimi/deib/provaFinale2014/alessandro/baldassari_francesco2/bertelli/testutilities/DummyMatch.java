package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DummyMatch 
{
	
	public List < Sheperd > sheperds ;
	
	public List < Animal > animals ;
	
	public List < Player > players ;
	
	public Match match ;
	
	public Bank bank ;
	
	public AnimalFactory animalFactory ;
	
	public DummyMatch () 
	{
		Identifiable < Match > matchIdentifier ;
		GameMap gameMap ;
		int ind ;
		ind = 0 ;
		do 
		{
			try 
			{
				matchIdentifier = new DummyMatchIdentifier ( ind ) ;
				gameMap = GameMapFactory.getInstance ().newInstance ( matchIdentifier ) ;
				animalFactory = AnimalFactory.newAnimalFactory ( matchIdentifier ) ;
				bank = BankFactory.getInstance().newInstance ( matchIdentifier ) ;	
				match = new Match ( gameMap , bank ) ;
			} 
			catch ( SingletonElementAlreadyGeneratedException e ) 
			{
				ind ++ ;
			} 
		}
		while ( bank == null ) ;
	}

	public void initializePlayersAndSheperds () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		byte i ;
		players = new ArrayList < Player > () ;
		sheperds = new ArrayList < Sheperd > () ;
		players.add( new DummyPlayer ( "P1" ) ) ;
		players.add ( new DummyPlayer ( "P2" ) ) ;
		players.add ( new DummyPlayer ( "P3" ) ) ;
		sheperds.add ( new Sheperd ( "3" , new NamedColor ( 2550 , 0 , 0 , "red" ) , players.get (0) ) ) ;
		sheperds.add ( new Sheperd ( "1" , new NamedColor ( 2550 , 0 , 0 , "red" ) , players.get (1) ) ) ;
		sheperds.add ( new Sheperd ( "2" , new NamedColor ( 2550 , 0 , 0 , "red" ) , players.get (2) ) ) ; 
		i = 0 ;
		for ( Player p : players )
		{
			p.setInitialCard ( bank.takeInitialCard ( RegionType.values() [ i ] ) );
			p.receiveMoney ( 5 ) ;
			i ++ ;
		}
		
	}
	
	public void initializeAnimals () throws WolfAlreadyGeneratedException, BlackSheepAlreadyGeneratedException 
	{
		animals = new ArrayList < Animal > () ;
		animals.add( animalFactory.newWolf () ) ;
		animals.add ( animalFactory.newBlackSheep () ) ;
		for ( byte b = 0 ; b < 4 ; b ++ )
			animals.add ( animalFactory.newAdultOvine ( AdultOvineType.RAM )  ) ;
		for ( byte b = 0 ; b < 4 ; b ++ )
			animals.add (animalFactory.newAdultOvine ( AdultOvineType.SHEEP )  ) ;
	}
	
}
