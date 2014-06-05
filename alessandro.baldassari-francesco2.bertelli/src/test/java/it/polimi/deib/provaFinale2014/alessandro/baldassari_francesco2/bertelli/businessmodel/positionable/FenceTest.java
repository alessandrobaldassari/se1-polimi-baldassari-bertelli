package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceAlreadyPlacedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
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
		dummyMatchIdentifier = new DummyMatchIdentifier ( 0 );

		try 
		{
			map = GameMapFactory.getInstance().newInstance ( dummyMatchIdentifier ) ;
		}
		catch (SingletonElementAlreadyGeneratedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fenceNonFinal.place(map.getRoadByUID(1));
		} catch (FenceAlreadyPlacedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fenceFinal.place(map.getRoadByUID(2));
		} catch (FenceAlreadyPlacedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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