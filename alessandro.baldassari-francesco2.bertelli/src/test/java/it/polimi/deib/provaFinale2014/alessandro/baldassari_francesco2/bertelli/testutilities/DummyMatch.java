package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.AutomaticPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
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
	
	public DummyMatch () throws SingletonElementAlreadyGeneratedException, WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		Identifiable < Match > matchIdentifier ;
		GameMap gameMap ;
		AnimalFactory animalFactory ;
		Region re ;
		Road ro ;
		matchIdentifier = new DummyMatchIdentifier ( 0 ) ;
		gameMap = GameMapFactory.getInstance ().newInstance ( matchIdentifier ) ;
		animalFactory = AnimalFactory.newAnimalFactory ( matchIdentifier ) ;
		byte i ;
		bank = BankFactory.getInstance().newInstance ( matchIdentifier ) ;	
		players = new ArrayList < Player > () ;
		sheperds = new ArrayList < Sheperd > () ;
		animals = new ArrayList < Animal > () ;
		players.add( new AutomaticPlayer ( "P1" ) ) ;
		players.add ( new AutomaticPlayer ( "P2" ) ) ;
		players.add ( new AutomaticPlayer ( "P3" ) ) ;
		sheperds.add ( new Sheperd ( "3" , Color.red , players.get (2) ) ) ;
		sheperds.add ( new Sheperd ( "1" , Color.red , players.get (1) ) ) ;
		sheperds.add ( new Sheperd ( "2" , Color.red , players.get (0) ) ) ; 
		animals.add( animalFactory.newWolf () ) ;
		animals.add ( animalFactory.newBlackSheep () ) ;
		for ( byte b = 0 ; b < 4 ; b ++ )
			animals.add ( animalFactory.newAdultOvine ( "" , AdultOvineType.RAM )  ) ;
		for ( byte b = 0 ; b < 4 ; b ++ )
			animals.add (animalFactory.newAdultOvine ( "" , AdultOvineType.SHEEP )  ) ;
		i = 0 ;
		for ( Player p : players )
		{
			p.setInitialCard ( bank.takeInitialCard ( RegionType.values() [ i ] ) );
			p.receiveMoney ( 5 ) ;
			i ++ ;
		}
		for ( Sheperd s : sheperds )
		{
			ro = gameMap.getRoadByUID ( Math.round ( ( float ) ( Math.random () * 41 ) ) + 1 ) ;
			ro.setElementContained ( s ) ;
			s.moveTo ( ro );
		}
		for ( Animal a : animals )
		{
			re = gameMap.getRegionByUID ( Math.round ( ( float ) ( Math.random () * 18 ) ) + 1 ) ;
			re.addAnimal ( a ) ;
			a.moveTo ( re );
		}
		match = new Match ( gameMap , bank ) ;
	}

}
