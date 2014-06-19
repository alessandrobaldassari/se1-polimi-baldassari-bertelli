package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.BlackSheepAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.BlackSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

import org.junit.Before;
import org.junit.Test;

public class MapUtilitiesTest 
{

	private GameMap m ;
	
	private AnimalFactory an ;
	
	@Before 
	public void setUp () 
	{
		int i ;
		i = 0 ;
		do
		{
			try 
			{
				ObjectIdentifier < Match > id ;
				id = MatchIdentifier.newInstance();
				m = GameMapFactory.getInstance().newInstance ( id );
				an = AnimalFactory.newAnimalFactory(id) ;
			}
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				i ++ ;
			}
		}
		while ( m == null ) ;
	}
	
	@Test
	public void areAdjacents () 
	{
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
	public void findBlackSheepAtStart () 
	{
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
	public void findWolfAtStart () 
	{
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
	
}
