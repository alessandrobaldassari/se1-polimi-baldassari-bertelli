package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
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
		waitForTheClient () ;
		try 
		{
			m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_REQUEST , Collections.EMPTY_LIST ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ;
			waitForTheClient () ;
			m = rmiClientBroker.getNextMessage () ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE )
			{
				System.out.println ( "RMI_CLIENT_HANDLER" ) ;
				res = ( String ) m.getParameters().iterator().next () ;
			}
			else
				throw new IOException ();
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void notifyNameChoose ( boolean isNameOk , String note ) throws IOException
	{
		Collection < Serializable > c ;
		Message m ;
		try 
		{
			c = new ArrayList < Serializable > ( 2 ) ;
			waitForTheClient () ;
			c.add ( isNameOk ) ;
			c.add ( note ) ;
	 		m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE_RESPONSE , c ) ; 
	 		rmiClientBroker.putNextMessage ( m ) ;
		}
		catch ( RemoteException e ) 
		{
			e.printStackTrace () ;
		}
		catch (AnotherCommandYetRunningException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void notifyMatchStart () throws IOException 
	{
		Message m ;
		try 
		{
			waitForTheClient () ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.MATCH_STARTING_NOTIFICATION , Collections.EMPTY_LIST ) ;
			rmiClientBroker.putNextMessage ( m ) ;
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		catch (AnotherCommandYetRunningException e) 
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void notifyMatchWillNotStart ( String message ) throws IOException 
	{
		Message m ;
		try 
		{
			waitForTheClient () ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.MATCH_WILL_NOT_START_NOTIFICATION , Collections.singleton ( ( Serializable ) message ) ) ;
			rmiClientBroker.putNextMessage ( m ) ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor requestSheperdColor ( Iterable < NamedColor > availableColors ) throws IOException 
	{
		Message m ;
		NamedColor res ;
		try 
		{
			res = null ;
			waitForTheClient () ;
			System.out.println ( "RMI CLIENT HANDLER - REQUEST SHEPERD COLOR : CREANDO IL MESSAGGIO" ) ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) availableColors ) ) ;
			System.out.println ( "RMI CLIENT HANDLER - REQUEST SHEPERD COLOR : CARICANDO IL MESSAGGIO" ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ;
			System.out.println ( "RMI CLIENT HANDLER - REQUEST SHEPERD COLOR : ATTENDENDO LA RISPOSTA" ) ;
			waitForTheClient () ;
			System.out.println ( "RMI CLIENT HANDLER - REQUEST SHEPERD COLOR : RISPOSTA RICEVUTA" ) ;
			m = rmiClientBroker.getNextMessage () ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE )
			{
				System.out.println ( "RMI CLIENT HANDLER - REQUEST SHEPERD COLOR - RISPOSTA RICEVUTA : COLORE " + ( NamedColor ) m.getParameters ().iterator ().next () ) ;
				res = ( NamedColor ) m.getParameters ().iterator ().next () ;
			}
			else 
				throw new RuntimeException () ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > chooseCardsEligibleForSelling ( Iterable < SellableCard > sellableCards ) throws IOException 
	{	
		Iterable < SellableCard > res ;
		Message m ;
		try 
		{
			waitForTheClient () ;
			System.out.println ( "RMI CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : CREANDO IL MESSAGGIO" ) ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) sellableCards ) ) ;
			System.out.println ( "RMI CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : CARICANDO IL MESSAGGIO" ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ;
			System.out.println ( "RMI CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : ATTENDENDO LA RISPOSTA" ) ;
			waitForTheClient () ;
			System.out.println ( "RMI CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : RISPOSTA RICEVUTA" ) ;
			m = rmiClientBroker.getNextMessage () ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE )
			{
				System.out.println ( "RMI CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING - RISPOSTA RICEVUTA " ) ;
				res = ( Iterable < SellableCard > ) m.getParameters ().iterator ().next () ;
			}
			else 
				throw new RuntimeException () ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer ) throws IOException 
	{
		Sheperd res = null ;
		Message m ;
		try 
		{
			waitForTheClient();
			m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) sheperdsOfThePlayer ) ) ;	
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ;
			waitForTheClient () ;
			m = rmiClientBroker.getNextMessage () ;
			System.out.println ( "RMI_CLIENT_HANDLER : CHOOSE SHEPERD FOR A TURN : MESSAGE RECEIVED : " + m.getOperation() ) ;
			if ( m.getOperation() == ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
			{
				System.out.println ( "RMI_CLIENT_HANDLER" ) ;
				res = (Sheperd) m.getParameters ().iterator().next () ;
			}
			else
				throw new IOException () ;
		}
		catch (AnotherCommandYetRunningException e) 
		{
			e.printStackTrace();
		}
		System.out.println ( "RMI_CLIENT_HANDLER : CHOOSE SHEPERD FOR A TURN : FINISH : " ) ;
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src )throws IOException 
	{
		Message m ;
		SellableCard res = null ;
		try 
		{
			waitForTheClient () ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) src ) ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ; 
			m = rmiClientBroker.getNextMessage () ;
			if ( m.getOperation() == ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE )
			{
				System.out.println () ;
				res = (SellableCard) m.getParameters().iterator().next();
			}
			else
				throw new RuntimeException () ;
		}
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GameMove doMove ( MoveFactory gameFactory, GameMap gameMap ) throws IOException 
	{
		Collection < Serializable > params ;
		Message m ;
		GameMove res = null ;
		try 
		{
			params = new ArrayList < Serializable > ( 2 ) ;
			waitForTheClient () ;
			params.add ( gameFactory ) ;
			params.add ( gameMap ) ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_REQUEST , params ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ; 
			waitForTheClient () ;
			m = rmiClientBroker.getNextMessage () ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE )
			{
				System.out.println ( "" ) ;
				res = (GameMove) m.getParameters().iterator().next() ;
			}
			else
				throw new RuntimeException () ;
		} 
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void genericNotification ( String message ) throws IOException 
	{
		Message m ;
		try 
		{
			waitForTheClient () ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.GENERIC_NOTIFICATION_NOTIFICATION , Collections.EMPTY_LIST ) ;
			rmiClientBroker.putNextMessage ( m ) ;
			notifyMessageFinished () ;
		} 
		catch (AnotherCommandYetRunningException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose() throws IOException {}
	
	/**
	 * @throws RemoteException 
	 */
	private void waitForTheClient () throws RemoteException 
	{
		while ( rmiClientBroker.isClientReady () == false ) ;
	}
	
	/**
	 * 
	 */
	private void notifyMessageFinished () throws RemoteException 
	{
		rmiClientBroker.setServerReady () ;
	}
	
}
