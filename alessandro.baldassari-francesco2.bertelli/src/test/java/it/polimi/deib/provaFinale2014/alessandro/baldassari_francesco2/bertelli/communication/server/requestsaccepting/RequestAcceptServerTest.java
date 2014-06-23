package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepting;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.ConnectionLoosingServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching.MatchLauncherServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching.MatchPlayerAdder;

public class RequestAcceptServerTest 
{

	@Test ( expected = IllegalArgumentException.class )
	public void ctorKo () 
	{
		new DummyRequestAcceptServer(null);
	}
	
	@Test
	public void ctorOk () 
	{
		RequestAccepterServer r ;
		r = new DummyRequestAcceptServer ( new MatchLauncherServer ( new ConnectionLoosingServer () ) ) ;
		assertTrue ( r != null ) ;
		
	}
	/**
	@Test
	public void run () 
	{
		RequestAccepterServer r ;
		try 
		{
			r = new DummyRequestAcceptServer ( new MatchLauncherServer ( new ConnectionLoosingServer () ) ) ;
			r.run();
			Thread.sleep(1000); 
			r.shutDown();
			assertTrue ( r != null ) ;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	**/
	
	
}

class DummyRequestAcceptServer extends RequestAccepterServer {

	protected DummyRequestAcceptServer
	(
			MatchPlayerAdder matchAdderCommunicationController) {
		super(matchAdderCommunicationController);
	}

	@Override
	protected void acceptRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void technicalStart() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void lifeLoopImplementation() {
		// TODO Auto-generated method stub
		
	}}
