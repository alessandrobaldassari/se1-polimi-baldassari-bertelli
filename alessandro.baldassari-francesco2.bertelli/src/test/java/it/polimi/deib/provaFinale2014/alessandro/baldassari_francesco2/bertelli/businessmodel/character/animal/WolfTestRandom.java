package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import java.util.ArrayList;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory.WolfAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Wolf;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import org.junit.BeforeClass;
import org.junit.Test;

/*
 * This jUnit test class test Wolf
 */
public class WolfTestRandom {

	/*
	 * Initializing variables test built the test environment using some static variables to bypass some technical problems with animalFactory
	 */
	static AnimalFactory animalFactory ;
	static GameMap map;
	static DummyMatchIdentifier dummyMatchIdentifier;
	static Wolf wolf;
	static ArrayList<Fence> fences = new ArrayList<Fence>();
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		dummyMatchIdentifier = new DummyMatchIdentifier();
		try {
			animalFactory = AnimalFactory.newAnimalFactory(dummyMatchIdentifier);
		} catch (SingletonElementAlreadyGeneratedException e) {
			e.printStackTrace();
		}
		try {
			wolf = (Wolf) animalFactory.newWolf();
		} catch (WolfAlreadyGeneratedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			map = GameMapFactory.getInstance().newInstance(dummyMatchIdentifier);
			map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().addAnimal(wolf);
			wolf.moveTo(map.getRegionByType(RegionType.SHEEPSBURG).iterator().next());
		} catch (SingletonElementAlreadyGeneratedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	/*
	 * Testing escape() method. Only one border road occupied. Sometimes the test fails because random
	 */
	@Test (expected = CharacterDoesntMoveException.class)
	public void escape() throws CharacterDoesntMoveException {
		LinkedList <Road> borderRoads;
		for(int i=0; i<5; i++)
			fences.add(new Fence(FenceType.NON_FINAL));
		borderRoads = (LinkedList<Road>) map.getRegionByType(RegionType.SHEEPSBURG).iterator().next().getBorderRoads();
		int i = 0;
		for(Road road : borderRoads){
			if(i != 0)
			road.setElementContained(fences.remove(0));
			i++;
		}
		wolf.escape();		
	}
		
}
