package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import org.junit.Before;
import org.junit.Test;

public class MapUtilitiesTest 
{

	private GameMap m ;
	
	private AnimalFactory an ;
	
	public void setUp () throws SingletonElementAlreadyGeneratedException 
	{
		do
		{
			ObjectIdentifier < Match > id ;
			id = MatchIdentifier.newInstance();
			m = GameMapFactory.getInstance().newInstance ( id );
			an = AnimalFactory.newAnimalFactory(id) ;
		
		}
		while ( m == null ) ;
	}
	
	@Test
	public void areAdjacentsRoadRegion () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Region region1 ;
		Road road1 ;
		Road road2 ;
		region1 = m.getRegionByUID( 5 ) ;
		road1 = m.getRoadByUID ( 4 ) ;
		road2 = m.getRoadByUID ( 42 ) ;
		assertTrue ( MapUtilities.areAdjacents ( road1 , region1 ) ) ;
		assertFalse ( MapUtilities.areAdjacents ( road2 , region1 ) ) ;
	}
	
	@Test
	public void areAdRoadRoad () throws SingletonElementAlreadyGeneratedException 
	{
		setUp();
		Road r1 ;
		Road r2 ;
		Road r3 ;
		r1 = m.getRoadByUID ( 2 ) ;
		r2 = m.getRoadByUID( 4 ) ;
		r3 = m.getRoadByUID ( 34 ) ;
		assertTrue ( MapUtilities.areAdjacents ( r1,r2) ) ;
		assertFalse ( MapUtilities.areAdjacents ( r1 , r3 ) ) ;
	}
	
	@Test
	public void findBlackSheepAtStart () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		BlackSheep b ;
		try 
		{
			b = (BlackSheep) an.newBlackSheep () ;
			b.moveTo ( m.getRegionByUID ( 10 ) ) ;
			m.getRegionByUID ( 10 ).addAnimal ( b ) ;
			b = MapUtilities.findBlackSheepAtStart ( m ) ;
			assertTrue ( b instanceof BlackSheep ) ;
			assertTrue ( b.getPosition().getType().equals ( RegionType.SHEEPSBURG ) ) ;
			assertTrue ( b.getType().equals ( AdultOvineType.SHEEP ) ) ;
			m.getRegionByUID(10).removeAnimal(b); 
			b.moveTo ( m.getRegionByUID ( 8 ) );
			m.getRegionByUID ( 8 ).addAnimal(b); 
			b = null ;
			try 
			{
				b = MapUtilities.findBlackSheepAtStart ( m ) ;
				fail () ;
			}
			catch ( WrongStateMethodCallException e1 ) 
			{
				assertTrue ( b == null ) ;
			}
		}
		catch ( WrongStateMethodCallException e ) 
		{
			fail () ;
		}
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail ( "System error" ) ;
		}
	}
	
	@Test
	public void findWolfAtStart () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Wolf b ;
		try 
		{
			b = (Wolf) an.newWolf();
			b.moveTo ( m.getRegionByUID ( 10 ) ) ;
			m.getRegionByUID ( 10 ).addAnimal ( b ) ;
			b = MapUtilities.findWolfAtStart ( m ) ;
			assertTrue ( b instanceof Wolf ) ;
			assertTrue ( b.getPosition().getType().equals ( RegionType.SHEEPSBURG ) ) ;
			assertTrue ( b.getPositionableElementType() == PositionableElementType.WOLF)  ;
			b.moveTo ( m.getRegionByUID ( 8 ) );
			m.getRegionByUID(10).removeAnimal(b); 
			m.getRegionByUID ( 8 ).addAnimal ( b ) ;
			b = null ;
			try 
			{
				b = MapUtilities.findWolfAtStart ( m ) ;
				fail () ;
			}
			catch ( WrongStateMethodCallException e1 ) 
			{
				assertTrue ( b == null ) ;
			}
		}
		catch ( WrongStateMethodCallException e ) 
		{
			fail () ;
		} 
		catch (WolfAlreadyGeneratedException e) 
		{
			fail ( "System error" ) ;
		}
	}
	
	@Test
	public void extractAdultOvinesExceptBlackSheep () throws SingletonElementAlreadyGeneratedException 
	{
		setUp();
		Collection < Animal > in ;
		Collection < AdultOvine > out ;
		try 
		{
			in = new ArrayList < Animal > () ;
			Animal ao1 = an.newWolf();
			Animal ao2 = an.newBlackSheep();
			Animal ao3 = an.newAdultOvine ( AdultOvineType.RAM ) ;
			Animal ao4 = an.newAdultOvine ( AdultOvineType.SHEEP ) ;
			Animal ao5 = an.newLamb(0, (AdultOvine)ao3,(AdultOvine)ao4);
			in.add(ao1);
			in.add(ao2);
			in.add(ao3);
			in.add(ao4);
			in.add(ao5);
			out = CollectionsUtilities.newCollectionFromIterable ( MapUtilities.extractAdultOvinesExceptBlackSheep(in) ) ;
			assertTrue ( out.size() == 2 ) ;
			assertTrue ( out.contains ( ao3 ) && out.contains ( ao4  ) ) ;
			assertFalse ( out.contains ( ao1 ) ) ;
			assertFalse ( out.contains ( ao2 ) ) ;
			assertFalse ( out.contains ( ao5 ) ) ;
		}
		catch (WolfAlreadyGeneratedException e) 
		{
			fail () ;
		}
		catch (BlackSheepAlreadyGeneratedException e) 
		{
			fail () ;
		}
	}
	
	@Test
	public void lookForAnOvine () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Collection < Animal > in ;
		AdultOvine res ;
		in = new ArrayList < Animal > () ;
		Animal ao1 = an.newWolf();
		Animal ao2 = an.newBlackSheep();
		Animal ao3 = an.newAdultOvine ( AdultOvineType.RAM ) ;
		Animal ao4 = an.newAdultOvine ( AdultOvineType.SHEEP ) ;
		Animal ao5 = an.newLamb(0, (AdultOvine)ao3,(AdultOvine)ao4);
		in.add(ao1);	
		in.add(ao2);
		in.add(ao3);
		in.add(ao4);
		in.add(ao5);
		res = MapUtilities.lookForAnOvine ( MapUtilities.extractAdultOvinesExceptBlackSheep ( in ) , AdultOvineType.SHEEP ) ;
		assertTrue ( res.equals ( ao4 ) ) ;
	}
	
	/***
	@Test
	public void ovineCount () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Region r ;
		r = m.getRegionByUID(5) ;
		r.addAnimal ( an.newWolf() ) ; 
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		r.addAnimal ( an.newBlackSheep() ) ;
		assertTrue ( MapUtilities.ovineCount ( r ) == 3 ) ;
	}
	***/
	
	@Test
	public void getOtherAdjacentDifferentFrom () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		assertTrue ( MapUtilities.getOtherAdjacentDifferentFrom ( m.getRoadByUID ( 29 ) , m.getRegionByUID( 8 ) ).equals ( m.getRegionByUID ( 9 ) ) ) ;
	}
	
	@Test
	public void findAnimalByUID () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Region r ;
		r = m.getRegionByUID(4) ;
		Iterable < Animal > i = CollectionsUtilities.newCollectionFromIterable ( r.getContainedAnimals() ) ;
		for ( Animal a : i )
			r.removeAnimal(a); 
		Animal a = an.newWolf();
		r.addAnimal ( a ) ; 
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		r.addAnimal ( an.newBlackSheep() ) ;
		assertTrue ( MapUtilities.findAnimalByUID ( r , a.getUID() ).equals(a) ) ;
		assertTrue ( MapUtilities.findAnimalByUID ( r , 100000 ) == null ) ;
	}
	
	/***
	@Test
	public void lookForAType () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Region r ;
		r = m.getRegionByUID ( 2 ) ;
		Animal a = an.newWolf();
		r.addAnimal ( a ) ; 
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		r.addAnimal ( an.newAdultOvine ( AdultOvineType.RAM ) ) ;
		assertTrue ( MapUtilities.lookForAType(r.getContainedAnimals(), PositionableElementType.WOLF).equals(a) ) ; 
		assertTrue ( MapUtilities.lookForAType ( r.getContainedAnimals() , PositionableElementType.SHEEP ) == null ) ;
	}
	***/
	
	/**
	@Test
	public void retrieveAdjacentPlayers () throws SingletonElementAlreadyGeneratedException 
	{
		setUp ();
		Road r1 ;
		Sheperd s1 ;
		Sheperd s2 ;
		Sheperd s3 ;
		Collection out ;
		s1 = new Sheperd ( "A" , new NamedColor ( 0 , 0 , 0 , "red" ) , new DummyPlayer ( "Ax" ) ) ;
		s2 = new Sheperd ( "B" , new NamedColor ( 0 , 0 , 0 , "red" ) , new DummyPlayer ( "By" ) ) ;
		s3 = new Sheperd ( "C" , new NamedColor ( 0 , 0 , 0 , "red" ) , new DummyPlayer ( "Cz" ) ) ;
		r1 = m.getRoadByUID(5);
		r1.setElementContained ( s1 ) ;
		m.getRoadByUID ( 2 ).setElementContained(s2); 
		m.getRoadByUID ( 25 ).setElementContained(s3);
		out = MapUtilities.retrieveAdjacentPlayers ( r1 ) ;
		assertTrue ( out.size() == 1 ) ;
		assertTrue ( out.contains(s2.getOwner()) ) ;
		assertFalse ( out.contains(s1.getOwner()) ) ;
		assertFalse ( out.contains ( s3.getOwner() ) ) ;
	}***/
	
	@Test
	public void generateAllowedSet () throws SingletonElementAlreadyGeneratedException 
	{
		setUp () ;
		Collection in ;
		Collection out ;
		in = new ArrayList () ;
		out = MapUtilities.generateAllowedSet ( in ) ;
		assertTrue ( out.size() == 3 ) ;
		in.add ( an.newAdultOvine(AdultOvineType.RAM) ) ;
		in.add ( an.newAdultOvine ( AdultOvineType.SHEEP ) ) ;
		out = MapUtilities.generateAllowedSet ( in ) ;
		assertTrue ( out.size () == 1 ) ;
		assertTrue ( out.contains ( PositionableElementType.LAMB ) ) ;
		assertFalse ( out.contains ( PositionableElementType.RAM ) ) ;
	}
	
}
