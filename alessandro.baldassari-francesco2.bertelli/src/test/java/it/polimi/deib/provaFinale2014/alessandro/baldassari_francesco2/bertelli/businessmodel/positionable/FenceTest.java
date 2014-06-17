package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceAlreadyPlacedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FenceTest 
{

	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	static GameMap map;
	static Identifiable < Match > dummyMatchIdentifier;
	static Fence fenceNonFinal = new Fence(FenceType.NON_FINAL);
	static Fence fenceFinal = new Fence(FenceType.FINAL);
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		int i ;
		i = 0 ; 
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
				i ++ ;
			}
			catch ( FenceAlreadyPlacedException e ) 
			{
				throw new RuntimeException ( e ) ;
			}
		}
		while ( map == null ) ;
	}
	
	@Test
	public void Fence(){
		assertTrue(fenceNonFinal.equals(fenceNonFinal));
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
	public void equals(){
		assertTrue(fenceFinal.equals(fenceFinal));
		assertFalse(fenceFinal.equals(fenceNonFinal));
	}
	
}
