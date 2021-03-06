package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

public class WrongMatchStateMethodCallExceptionTest {

	MatchState matchState;
	ObjectIdentifier < Match > i ;
	Constructor < WrongMatchStateMethodCallException > c ;
	WrongMatchStateMethodCallException exception;
	
	@Before
	public void setUp(){
		try 
		{
			i = MatchIdentifier.newInstance();
			c = WrongMatchStateMethodCallException.class.getDeclaredConstructor ( MatchState.class ) ;
			c.setAccessible ( true ) ;
			exception = c.newInstance ( MatchState.WAIT_FOR_PLAYERS) ;
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
	}
	
	@Test
	public void getActualState() {
		assertTrue(exception.getActualState() == MatchState.WAIT_FOR_PLAYERS);
	}

}
