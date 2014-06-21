package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import java.rmi.RemoteException;
import java.util.Collections;

import org.junit.Test;

public class RMIClientBrokerImplAndMessageTest 
{

	private RMIClientBroker r1 ;
	
	private RMIClientBroker r2 ;
	
	@Test
	public void ctorAndGetMessage () 
	{
		try 
		{
			r1 = new RMIClientBrokerImpl();
			assertTrue ( r1.getNextMessage() == null ) ;
			assertTrue ( r1.getRMIName().startsWith ( RMIClientBrokerImpl.OBJECT_BASE_NAME ) ) ;
			r2 = new RMIClientBrokerImpl();
			assertFalse ( r2.getRMIName().compareToIgnoreCase ( r1.getRMIName() ) == 0 ) ;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void setGetMessage () 
	{
		r1 = new RMIClientBrokerImpl();
		Message m;
		m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST , Collections.EMPTY_LIST ) ;
		assertTrue ( m.getUID() > 0 ) ;
		assertTrue ( m.getOperation() ==  GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST  ) ;
		assertTrue ( CollectionsUtilities.iterableSize( m.getParameters()) == 0 ) ;
		assertTrue ( ! m.toString ().isEmpty() ) ;
		Message m2 = Message.newInstance ( GameProtocolMessage.DO_MOVE_REQUESTING_REQUEST , Collections.EMPTY_LIST ) ;
		assertTrue ( m.equals(m) );
		assertFalse ( m.equals(m2) );
		assertFalse ( m.equals(new Integer(2)) );
 		try 
		{
			r1.putNextMessage(m);
			assertTrue ( m.equals ( r1.getNextMessage() ) ) ;
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
		} catch (AnotherCommandYetRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void boolTests () 
	{
		try 
		{
			r1 = new RMIClientBrokerImpl();
			assertTrue ( r1.isClientReady() && ! r1.isServerReady() ) ;
			assertFalse ( r1.isServerReady() && ! r1.isClientReady() ) ;
			r1.setClientReady();
			assertTrue ( r1.isClientReady() && ! r1.isServerReady() ) ;
			assertFalse ( r1.isServerReady() && ! r1.isClientReady() ) ;
			r1.setServerReady();
			assertTrue ( r1.isServerReady() && ! r1.isClientReady() ) ;
			assertFalse ( r1.isClientReady() && ! r1.isServerReady() ) ;
			r1.setClientReady();
			assertTrue ( r1.isClientReady() && ! r1.isServerReady() ) ;
			assertFalse ( r1.isServerReady() && ! r1.isClientReady() ) ;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
