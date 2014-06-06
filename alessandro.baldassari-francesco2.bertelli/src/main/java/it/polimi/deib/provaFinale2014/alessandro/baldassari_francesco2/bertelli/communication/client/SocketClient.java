package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientCommunicationProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.SocketServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Socket based implementation of the Client astraction entity. 
 */
public class SocketClient extends Client 
{
	
	/**
	 * The socket object used to make the connection to the server. 
	 */
	private Socket channel ;
		
	/**
	 * An ObjectInputStream object used to extend the features of the socket's attribute InputStream. 
	 */
	private ObjectInputStream ois ;
	
	/**
	 * An ObjectOutputStream object used to extend the features of the socket's attribute OutputStream.  
	 */
	private ObjectOutputStream oos ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public SocketClient ( CommunicationProtocolResponser communicationProtocolResponser )   
	{
		super ( communicationProtocolResponser ) ;
		channel = new Socket () ;
		ois = null ;
		oos = null ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void technicalConnect () throws IOException
	{
		SocketAddress socketAddress ;
		System.out.println ( "SOCKET_CLIENT : BEGIN TECHNICAL CONNECT" ) ;
		socketAddress = new InetSocketAddress ( SocketServer.SERVER_IP_ADDRESS , SocketServer.TCP_LISTENING_PORT ) ;
		channel.connect ( socketAddress ) ;
		ois = new ObjectInputStream ( channel.getInputStream () ) ;
		oos = new ObjectOutputStream ( channel.getOutputStream () ) ;
		System.out.println ( "SOCKET CLIENT : END TECHNICAL CONNECT" ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void technicalDisconnect () throws IOException 
	{
		ois.close () ;
		oos.close () ;
		channel.close () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void communicationProtocolImpl () 
	{
		List < Serializable > params ;
		ClientCommunicationProtocolMessage op ;
		Message m ;
		Iterable < Sheperd > sheperds ;
		Iterable <SellableCard> sellableCards;
		Iterable < NamedColor > colors ;
		NamedColor n ;
		Sheperd choosenSheperd ;
		String s ;
		SellableCard c ;
		Boolean b ;
		MoveFactory gf ;
		GameMap map ;
		GameMove move ;
		try 
		{
			System.out.println ( "SOCKET CLIENT : ASPETTANDO UN MESSAGGIO" ) ;
			m = readMessage () ;
			System.out.println ( "SOCKET CLIENT : MESSAGGIO RICEVUTO : " + m.getOperation () ) ;
			switch ( m.getOperation () )  
			{
				case NAME_REQUESTING_REQUEST :
					s = getDataPicker ().onNameRequest () ;
					params = new ArrayList < Serializable > ( 1 ) ;
					params.add ( s ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE , params ) ;
					writeMessage ( m ) ;
				break ;	
				case NAME_REQUESTING_RESPONSE_RESPONSE :
					params = CollectionsUtilities.newListFromIterable ( m.getParameters () )  ;
					b = ( Boolean ) params.get ( 0 ) ;
					s = ( String ) params.get ( 1 ) ;
					getDataPicker ().onNameRequestAck  ( b , s ) ;
				break ;
				case MATCH_STARTING_NOTIFICATION :
					getDataPicker ().onNotifyMatchStart () ;
				break ;
				case MATCH_WILL_NOT_START_NOTIFICATION:
					getDataPicker ().onMatchWillNotStartNotification ( (String) m.getParameters().iterator().next() ) ;
				break ;
				case SHEPERD_COLOR_REQUESTING_REQUEST :	
					colors = ( Iterable < NamedColor > ) m.getParameters().iterator().next() ;
					n = getDataPicker ().onSheperdColorRequest ( colors ) ;
					params = new ArrayList < Serializable > ( 1 ) ;
					params.add( n );
					System.out.println ( "SOCKET CLIENT : SHEPERD COLOR REQUEST REQUEST - CREANDO MESSAGGIO DI RISPOSTA " ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , params ) ;
					System.out.println ( "SOCKET CLIENT : SHEPERD COLOR REQUEST REQUEST - SCRIVENDO MESSAGGIO DI RISPOSTA" ) ;
					writeMessage ( m ) ;
				break;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
					sheperds = ( Iterable < Sheperd > ) m.getParameters ().iterator().next () ;
					choosenSheperd = getDataPicker ().onChooseSheperdForATurn ( sheperds ) ;
					params = new ArrayList < Serializable > ( 1 ) ;
					params.add ( choosenSheperd ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE , params ) ;
					writeMessage ( m ) ;
				break ;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					params = CollectionsUtilities.newListFromIterable ( m.getParameters() ) ;
					sellableCards = (Iterable<SellableCard>) params.get ( 0 ) ;
					sellableCards = getDataPicker ().onChooseCardsEligibleForSelling ( sellableCards );
					params.clear () ;
					params.add ( ( Serializable ) sellableCards ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE , params ) ;
					writeMessage ( m ) ;
				break ;
				case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
					params = CollectionsUtilities.newListFromIterable ( m.getParameters () ) ;
					sellableCards = ( Iterable < SellableCard > ) params.get ( 0 ) ;
					c = getDataPicker ().onChoseCardToBuy ( sellableCards ) ; 
					params.clear () ;
					params.add ( ( Serializable ) c ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE , params ) ;
					writeMessage ( m ) ;
				break ;
				case DO_MOVE_REQUESTING_REQUEST :
					params = CollectionsUtilities.newListFromIterable ( m.getParameters() ) ;
					gf = ( MoveFactory ) params.get ( 0 ) ;
					map = ( GameMap ) params.get ( 1 ) ;
					move = getDataPicker ().onDoMove ( gf , map ) ;
					params.clear () ;
					params.add ( move ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE , params ) ;
					writeMessage ( m ) ;
				break ;
				case GENERIC_NOTIFICATION_NOTIFICATION:
				break;
				default :
				break ;
			}
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace () ;
		}	 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}	
	}
	
	private Message readMessage () throws ClassNotFoundException, IOException 
	{
		Message res ;
		System.out.println ( "SOCKET CLIENT : READING MESSAGE" ) ; 
		res = (Message) ois.readObject () ;
		System.out.println ( "SOCKET CLIENT : MESSAGE READ" ) ; 
		return res ;
	}
	
	/***/
	private void writeMessage ( Message m ) throws IOException 
	{
		System.out.println ( "SOCKET CLIENT : WRITING MESSAGE" ) ; 
		oos.writeObject ( m ) ;
		oos.flush () ;
		System.out.println ( "SOCKET CLIENT : MESSAGE WRITTEN" ) ;
	}
	
}
