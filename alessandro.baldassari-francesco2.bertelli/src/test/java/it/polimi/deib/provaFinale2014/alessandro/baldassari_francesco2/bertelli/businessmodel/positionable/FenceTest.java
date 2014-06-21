package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceAlreadyPlacedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FenceTest 
{

	 GameMap map;
	 ObjectIdentifier < Match > dummyMatchIdentifier;
	 Fence fenceNonFinal = new Fence(FenceType.NON_FINAL);
	 Fence fenceFinal = new Fence(FenceType.FINAL);
	
	@Before
	public void setUpBeforeClass()
	{
		
		do 
		{
			try 
			{
				dummyMatchIdentifier = MatchIdentifier.newInstance();
				map = GameMapFactory.getInstance().newInstance ( dummyMatchIdentifier ) ;
				fenceNonFinal.place(map.getRoadByUID(1));
				fenceFinal.place(map.getRoadByUID(2));
			}
			catch (SingletonElementAlreadyGeneratedException e) 
			{
				fail () ;
			}
			catch ( FenceAlreadyPlacedException e ) 
			{
				fail () ;
			}
		}
		while ( map == null ) ;
	}
	
	@Test
	public void equalsTest () 
	{
		assertFalse ( fenceFinal.equals ( new Integer ( 2 ) ) ) ;
		assertTrue ( fenceFinal.equals ( fenceFinal ) ) ;
		assertFalse ( fenceNonFinal.equals(fenceFinal) );
	}
	
	@Test ( expected = IllegalArgumentException.class )
	public void FenceExc()
	{
		new Fence ( null ) ;
	}

	@Test
	public void isFinal(){
		assertTrue(fenceFinal.isFinal());
		assertFalse(fenceNonFinal.isFinal());
	}
	
	@Test
	public void isPlaced(){
		Fence fence = new Fence(FenceType.FINAL);
		assertTrue(fenceFinal.isPlaced());
		assertFalse(fence.isPlaced());
	}
	
	@Test (expected = FenceAlreadyPlacedException.class)
	public void place() throws FenceAlreadyPlacedException{
		Fence fence = new Fence(FenceType.NON_FINAL);
		fence.place(map.getRoadByUID(3));
		assertTrue(fence.getPosition().getUID() == 3);
		assertTrue(fence.isPlaced());
		fence.place(map.getRoadByUID(1));
	}
	
	@Test
	public void toStringT () 
	{
		assertFalse ( fenceFinal.toString() == null ) ;
		assertTrue ( ! fenceFinal.toString ().isEmpty() ) ;
	}
	
}
