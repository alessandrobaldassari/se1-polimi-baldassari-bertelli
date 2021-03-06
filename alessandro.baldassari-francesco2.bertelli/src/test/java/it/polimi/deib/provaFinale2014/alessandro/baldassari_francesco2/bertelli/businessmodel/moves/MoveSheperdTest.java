package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.TooFewMoneyException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummyMatch;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

public class MoveSheperdTest 
{

	private DummyMatch d ;
	
	@Before
	public void setUp ()
	{
		d = new DummyMatch () ;
	}
	
	/**
	 * Test an Exception launched by the ctor - road not empty
	 */
	@Test
	public void ctor1 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		d.initializePlayersAndSheperds();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 5 ) ) ;
		d.match.getGameMap().getRoadByUID ( 5 ).setElementContained ( d.sheperds.get(0) ) ;
		d.sheperds.get(1).moveTo ( d.match.getGameMap().getRoadByUID ( 2 ) ) ;
		d.match.getGameMap().getRoadByUID ( 2 ).setElementContained ( d.sheperds.get(0) ) ;		
		MoveSheperd m = null ;
		try 
		{
			m = new MoveSheperd ( d.sheperds.get(0) , d.match.getGameMap().getRoadByUID ( 2 ) ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( m == null ) ;
		} 
		catch (WorkflowException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Test an Exception launched by the ctor - sheperd does not move actually.
	 * @throws NoMoreCardOfThisTypeException 
	 * @throws WriteOncePropertyAlreadSetException 
	 */
	@Test
	public void ctor2 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		d.initializePlayersAndSheperds();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 5 ) ) ;
		d.match.getGameMap().getRoadByUID ( 5 ).setElementContained ( d.sheperds.get(0) ) ;
		MoveSheperd m = null ;
		try 
		{
			m = new MoveSheperd ( d.sheperds.get(0) , d.match.getGameMap().getRoadByUID ( 5 ) ) ;
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( m == null ) ;
		} catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the few money exception post 
	 */
	@Test
	public void execute1 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException 
	{
		d.initializePlayersAndSheperds();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 5 ) ) ;
		d.match.getGameMap().getRoadByUID ( 5 ).setElementContained ( d.sheperds.get(0) ) ;
		try 
		{
			d.sheperds.get(0).getOwner().pay(5);
		}
		catch (TooFewMoneyException e1) 
		{
			fail () ;
		} catch (WrongStateMethodCallException e) {
		fail () ;
		}
		MoveSheperd m = null ;
		try 
		{
			m = new MoveSheperd ( d.sheperds.get(0) , d.match.getGameMap().getRoadByUID ( 5 ) ) ;
			m.execute ( d.match );
			fail () ;
		}
		catch (MoveNotAllowedException e) 
		{
			assertTrue ( true ) ;
		} catch (WorkflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test the right flow - he has to pay
	 */
	@Test
	public void execute2 () throws WriteOncePropertyAlreadSetException, NoMoreCardOfThisTypeException, WrongStateMethodCallException, MoveNotAllowedException, WorkflowException 
	{
		d.initializePlayersAndSheperds();
		d.sheperds.get(0).moveTo ( d.match.getGameMap().getRoadByUID ( 5 ) ) ;
		d.match.getGameMap().getRoadByUID ( 5 ).setElementContained ( d.sheperds.get(0) ) ;
		MoveSheperd m = null ;
		int initMon ;
			int rIdBefore ;
			initMon = d.sheperds.get(0).getOwner().getMoney();
			rIdBefore = d.sheperds.get(0).getPosition().getUID();
			m = new MoveSheperd ( d.sheperds.get(0) , d.match.getGameMap().getRoadByUID ( 39 ) ) ;
			m.execute(d.match); 
			assertTrue ( initMon == d.sheperds.get(0).getOwner().getMoney() + 1 );
			assertTrue ( d.sheperds.get(0).getPosition().equals ( d.match.getGameMap().getRoadByUID(39) ) ) ;
			assertTrue ( d.match.getGameMap().getRoadByUID ( 39 ).getElementContained().equals ( d.sheperds.get(0) ) ) ;
			assertTrue ( d.match.getGameMap().getRoadByUID(rIdBefore).getElementContained() instanceof Fence );
	
	}
	
	
}
