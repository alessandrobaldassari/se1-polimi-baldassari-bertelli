package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collections;

import org.junit.Test;

public class RMIClientBrokerImplTest 
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
