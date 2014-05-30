package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;

/**
 * RMI-based implementation of the ClientHandler interface. 
 */
public class RMIClientHandler implements ClientHandler 
{
	
	/**
	 * The prefix name each RMIClientHandler will have. 
	 */
	public static final String LOGICAL_ABSTRACT_RMI_CLIENT_HANDLER = "SHEEPLAND_RMI_CLIENT_HANDLER_#_" ;
	
	/**
	 * A RMIClientBroker object this Handler will use to synchronize with the Client. 
	 */
	private RMIClientBroker rmiClientBroker;

	/**
	 * @param rmiClientBroker the value for the rmiClientBroker field
	 * @throws IllegalArgumentException if the parameter is null. 
	 */
	public RMIClientHandler ( RMIClientBroker rmiClientBroker ) 
	{
		if ( rmiClientBroker != null )
			this.rmiClientBroker = rmiClientBroker ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String requestName () throws IOException 
	{
		Message m ;

		String res ;
		System.out.println ( "RMI CLIENT HANDLER : BEFORE GO INTO CORE METHOD" ) ;
		while ( rmiClientBroker.isClientReady () == false ) ;
		System.out.println ( "RMI CLIENT HANDLER : AFTER GO INTO CORE METHOD" ) ;
		try 
		{
			m = Message.newInstance ( ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_REQUEST , Collections.EMPTY_LIST ) ;
			rmiClientBroker.putNextMessage ( m ) ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
		}
		rmiClientBroker.setServerReady () ;
		while ( rmiClientBroker.isClientReady () == false ) ;
		m = rmiClientBroker.getNextMessage () ;
		if ( m.getOperation () == ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_RESPONSE )
		{
			res = ( String ) m.getParameters().iterator().next () ;
		}
		else
		{
			throw new IOException ();
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void notifyMatchWillNotStart(String message) throws IOException 
	{
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Color requestSheperdColor(Iterable<Color> availableColors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void chooseCardsEligibleForSelling ( Iterable<SellableCard > sellablecards ) throws IOException 
	{	
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer) throws IOException 
	{
		return null;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src )throws IOException 
	{
		return null;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GameMove doMove(MoveFactory gameFactory, GameMap gameMap)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void genericNotification(String message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose() throws IOException 
	{
		
	}
	
}
