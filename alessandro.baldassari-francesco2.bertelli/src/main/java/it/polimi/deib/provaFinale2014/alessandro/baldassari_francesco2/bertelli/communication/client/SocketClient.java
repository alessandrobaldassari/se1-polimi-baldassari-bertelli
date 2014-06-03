package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientCommunicationProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.requestsaccepterserver.SocketServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
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
		MoveFactory moveFactory ;
		GameMap gameMap ;
		Iterable < Sheperd > sheperds ;
		Iterable <SellableCard> sellableCards;
		Iterable < NamedColor > colors ;
		String command ;
		NamedColor n ;
		String s ;
		Boolean b ;
		try 
		{
			params = new ArrayList < Serializable > ( 2 ) ;
			m = ( Message ) ois.readObject () ;
			switch ( m.getOperation () )  
			{
				case NAME_REQUESTING_REQUEST :
					op = ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE ;
					s = getDataPicker ().onNameRequest () ;
					params.add ( s ) ;
					System.out.println ( "SOCKET CLIENT : " + s ) ;
					m = Message.newInstance ( op , params ) ;
					oos.writeObject ( m ) ;
					oos.flush () ;
				break ;	
				case NAME_REQUESTING_RESPONSE_RESPONSE :
					params = CollectionsUtilities.newListFromIterable ( m.getParameters () ) ;
					s = (String) params.get ( 1 ) ;
					b = ( Boolean ) params.get ( 0 ) ;
					getDataPicker ().onNameRequestAck  ( b , s ) ;
				break ;
				case MATCH_STARTING_NOTIFICATION :
					getDataPicker ().onNotifyMatchStart () ;
				break ;
				case MATCH_WILL_NOT_START_NOTIFICATION:
					getDataPicker ().onMatchWillNotStartNotification ( (String) m.getParameters().iterator().next() ) ;
				break ;
				case SHEPERD_COLOR_REQUESTING_REQUEST:
					colors = ( Iterable < NamedColor > ) m.getParameters().iterator().next() ;
					n = getDataPicker ().onSheperdColorRequest ( colors ) ;
					params.clear(); 
					params.add(n);
					m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , params ) ;
					oos.writeObject ( m ) ;
					oos.flush () ;
				break;
				case GENERIC_NOTIFICATION_NOTIFICATION:
					s = ois.readUTF();
				break;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					sellableCards = (Iterable<SellableCard>) ois.readObject();
					oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE.toString () );
					oos.flush();
				break ;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
					sheperds = ( Iterable<Sheperd> ) ois.readObject () ;
					oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE.toString () ) ;
				break ;
				case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
					sellableCards = (Iterable<SellableCard>) ois.readObject () ;
					oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE.toString () );
				break ;
				case DO_MOVE_REQUESTING_REQUEST :
					moveFactory = (MoveFactory) ois.readObject () ;
					gameMap = (GameMap) ois.readObject () ;
					oos.writeUTF ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE.toString () ) ;
				break ;
				default :
				break ;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
}
