package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.factory;

import static org.junit.Assert.assertTrue;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.WrongMatchStateMethodCallException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.executor.MoveExecutor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyPlayer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import org.junit.Before;
import org.junit.Test;

public class MoveFactoryTest {

	private MoveExecutor mf ;
	
	private Sheperd sh ;
	
	private TurnNumberClock tnc ;
	
	@Before
	public void setUp () 
	{
		sh = new Sheperd("p", new NamedColor ( 255 , 0 , 0 , "red" ) , new DummyPlayer ( "q" ));
		tnc = new DummytTNC();
		mf = new MoveExecutor( sh , tnc , new DummyLambEvolver() ) ; 
	}
	
	@Test
	public void getSheperd () 
	{
		assertTrue ( mf.getAssociatedSheperd().equals(sh) ) ;
	}
	
	private class DummytTNC implements TurnNumberClock 
	{

		@Override
		public int getTurnNumber() throws WrongMatchStateMethodCallException 
		{
			return 0;
		}}
	
	private class DummyLambEvolver implements LambEvolver 
	{

		@Override
		public void evolve(Lamb lamb) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
