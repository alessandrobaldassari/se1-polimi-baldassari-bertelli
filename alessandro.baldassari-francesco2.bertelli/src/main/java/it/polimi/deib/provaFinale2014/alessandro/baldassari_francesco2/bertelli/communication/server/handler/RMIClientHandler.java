package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

class RMIClientHandler implements ClientHandler , RMIClientBroker 
{

	/**
	 * A Queue containing the parameters about the operation pending in the communication.
	 * Shared with the client. 
	 */
	private Queue < Serializable > nextParameters ;
	
	/**
	 * A variable to know which is the operation pending in the communication. 
	 * Shared with the client.
	 */
	private ClientHandlerClientCommunicationProtocolOperation nextOperation ;
	
	/**
	 *  
	 */
	private boolean serverReady ;
	
	/**
	 *  
	 */
	private boolean clientReady ;
	
	/***/
	RMIClientHandler () 
	{
		nextParameters = new LinkedList < Serializable > () ;
		nextOperation = null ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String requestName () throws IOException 
	{
		ClientHandlerClientCommunicationProtocolOperation clientCmd ;
		String res ;
		putNextParameter ( ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_REQUEST ) ;
		setServerReady ( true ) ;
		while ( isClientReady () == false )
			try 
			{
				wait () ;
			}
			catch ( InterruptedException e ) 
			{
				e.printStackTrace();
			}
		clientCmd = ( ClientHandlerClientCommunicationProtocolOperation ) getNextParameter () ;
		if ( clientCmd == ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_RESPONSE )
		{
			
		}
		else
		{
			// error management
		}
		return res ;
	}

	/***/
	@Override
	public void notifyMatchWillNotStart(String message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/***/
	@Override
	public Color requestSheperdColor(Iterable<Color> availableColors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void chooseCardsEligibleForSelling(
			Iterable<SellableCard> sellablecards) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sheperd chooseSheperdForATurn(Iterable<Sheperd> sheperdsOfThePlayer)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellableCard chooseCardToBuy(Iterable<SellableCard> src)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameMove doMove(MoveFactory gameFactory, GameMap gameMap)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genericNotification(String message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() throws IOException 
	{
		
	}

	@Override
	public ClientHandlerClientCommunicationProtocolOperation getNextCommand () 
	{
		return nextOperation ;
	}

	@Override
	public Serializable getNextParameter () 
	{
		return nextParameters.poll () ;
	}

	@Override
	public void putNextCommand ( ClientHandlerClientCommunicationProtocolOperation op ) throws AnotherCommandYetRunningException 
	{
		if ( nextOperation != null )
			this.nextOperation = op ;
		else
			throw new AnotherCommandYetRunningException () ;
	}

	@Override
	public void putNextParameter ( Serializable parameter ) 
	{
		nextParameters.offer ( parameter ) ;
	}

	/***/
	public void setServerReady ( boolean serverReady ) {}
	
	/***/
	public boolean isServerReady () 
	{
		return serverReady ;
	}
	
	/***/
	public void setClientReady ( boolean clientReady ) 
	{
		
	}
	
	/***/
	public boolean isClientReady () 
	{
		return clientReady ;
	}
	
}
