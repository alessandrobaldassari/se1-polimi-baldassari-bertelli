package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientCommunicationProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.RMIClientBroker.AnotherCommandYetRunningException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * RMI-based implementation of the Client abstraction entity. 
 */
public class RMIClient extends Client 
{

	/**
	 * The IP address where locate the Server. 
	 */
	private final static String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The port where contact the Server. 
	 */
	private final static int SERVER_PORT = 3334 ;

	/**
	 * A broker object to interact with the Server. 
	 */
	private RMIClientBroker clientBroker ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public RMIClient ( CommunicationProtocolResponser communicationProtocolResponser ) 
	{
		super ( communicationProtocolResponser ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalConnect() throws IOException 
	{
		RMIServer server ;
		String key ;
		Registry registry ;
		registry = LocateRegistry.getRegistry ( SERVER_IP_ADDRESS , SERVER_PORT ) ;
		try 
		{
			server = ( RMIServer ) registry.lookup ( RMIServer.LOGICAL_SERVER_NAME ) ;
			key = server.addPlayer () ; 
			clientBroker = ( RMIClientBroker ) registry.lookup ( key ) ;
		} 
		catch ( NotBoundException e ) 
		{
			e.printStackTrace();
		}
	}	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalDisconnect () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void communicationProtocolImpl () 
	{
		List < Serializable > params ;
		Iterable < SellableCard > sellableCards ;
		Iterable < NamedColor > colors ;
		Iterable < Sheperd > sheperds ;
		NamedColor n ;
		SellableCard c ;
		Sheperd sh ;
		String s ;
	    Boolean b ;
		Message m ;
		GameMap map ;
		MoveFactory gf ;
		GameMove move ;
		try 
		{
			params = new ArrayList < Serializable > ( 2 ) ;
			System.out.println ( "RMICLIENT : ATTENDENDO COMANDI IN ARRIVO " ) ;
			while ( clientBroker.isServerReady () == false ) ;
			m = clientBroker.getNextMessage () ;
			params = CollectionsUtilities.newListFromIterable ( m.getParameters() ) ;
			System.out.println ( "RMI CLIENT : MESSAGGIO RICEVUTO : " + m.getOperation () ) ;
			switch ( m.getOperation () )
			{
				case NAME_REQUESTING_REQUEST :
					// ask the interactive component for a name
					s = getDataPicker ().onNameRequest () ;
					params.clear () ;
					params.add  ( s ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage ( m ) ;
				break ;
				case NAME_REQUESTING_RESPONSE_RESPONSE :
					s = (String) params.get ( 1 ) ;
					b = ( Boolean ) params.get ( 0 ) ;
					getDataPicker ().onNameRequestAck  ( b , s ) ;
				break ;
				case MATCH_WILL_NOT_START_NOTIFICATION:
					getDataPicker ().onMatchWillNotStartNotification ( ( String ) params.get ( 0 ) ) ;
				break;
				case MATCH_STARTING_NOTIFICATION :
					getDataPicker ().onNotifyMatchStart () ;
				break ;
				case SHEPERD_COLOR_REQUESTING_REQUEST:
					colors = ( Iterable < NamedColor > ) params.get ( 0 ) ;
					// ask the interactive component for a color.
					n = getDataPicker ().onSheperdColorRequest ( colors ) ;
					params.clear(); 
					params.add(n);
					m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage ( m ) ;
				break;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					sellableCards = (Iterable<SellableCard>) params.get ( 0 ) ;
					sellableCards = getDataPicker ().onChooseCardsEligibleForSelling ( sellableCards );
					params.clear () ;
					params.add ( ( Serializable ) sellableCards ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage ( m ) ;
				break ;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
					sheperds = (Iterable<Sheperd>) params.get ( 0 ) ;
					sh = getDataPicker ().onChooseSheperdForATurn ( sheperds ) ;
					params.clear () ;
					params.add ( ( Serializable ) sh ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage ( m ) ;
				break ;
				case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
					sellableCards = ( Iterable < SellableCard > ) params.get ( 0 ) ;
					c = getDataPicker ().onChoseCardToBuy ( sellableCards ) ; 
					params.clear () ;
					params.add ( ( Serializable ) c ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage ( m ) ;
				break ;
				case DO_MOVE_REQUESTING_REQUEST :
					gf = ( MoveFactory ) params.get ( 0 ) ;
					map = ( GameMap ) params.get ( 1 ) ;
					move = getDataPicker ().onDoMove ( gf , map ) ;
					params.clear () ;
					params.add ( move ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE , params ) ;
					clientBroker.putNextMessage (m ) ;
				break ;
				case GENERIC_NOTIFICATION_NOTIFICATION :
					
				break;	
				default :
				break ;
			}
			// the client has finished is job.
			clientBroker.setClientReady () ;
		}
		catch ( RemoteException e) 
		{
			e.printStackTrace();
		}
		catch ( AnotherCommandYetRunningException e ) 
		{
			e.printStackTrace();
		}
		
	}
	
}
