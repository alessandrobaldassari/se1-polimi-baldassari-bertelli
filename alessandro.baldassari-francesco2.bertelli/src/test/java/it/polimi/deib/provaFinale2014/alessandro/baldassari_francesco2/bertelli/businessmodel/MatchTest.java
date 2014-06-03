package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class MatchTest 
{

	private Match m ;
	
	@Before
	public void setUp () 
	{
		Identifiable < Match > i ;
		Constructor < Match > c ;
		try 
		{
			i = new DummyMatchIdentifier ( 0 ) ;
			c = Match.class.getDeclaredConstructor ( GameMap.class , Bank.class ) ;
			c.setAccessible ( true ) ;
			m = c.newInstance ( GameMapFactory.getInstance().newInstance ( i ) , BankFactory.getInstance().newInstance ( i ) ) ;
		}
		catch (NoSuchMethodException e) 
		{
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{
			e.printStackTrace();
		}
		catch (SingletonElementAlreadyGeneratedException e) 
		{
			e.printStackTrace();
		}
 	}
	
	@Test
	public void getNumberOfPlayers () 
	{
		assertTrue ( m.getNumberOfPlayers () == 0 ) ;
	}
	
}