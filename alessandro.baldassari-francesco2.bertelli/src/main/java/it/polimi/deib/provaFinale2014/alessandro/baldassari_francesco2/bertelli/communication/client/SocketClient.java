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
	 * A list for the communication parameters.
	 * Here for efficiency. 
	 */
	private List < Serializable > communicationParameters ;
	
	/**
	 * @param communicationProtocolResponser the value for the communicationProtocolResponser field.
	 */
	public SocketClient ( CommunicationProtocolResponser communicationProtocolResponser )   
	{
		super ( communicationProtocolResponser ) ;
		channel = new Socket () ;
		communicationParameters = new ArrayList < Serializable > ( 2 ) ;
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
		ClientCommunicationProtocolMessage op ;
		Message m ;
		MoveFactory moveFactory ;
		GameMap gameMap ;
		Iterable < Sheperd > sheperds ;
		Iterable <SellableCard> sellableCards;
		Iterable < NamedColor > colors ;
		String command ;
		NamedColor n ;
		Sheperd choosenSheperd ;
		String s ;
		Boolean b ;
		try 
		{
			System.out.println ( "SOCKET CLIENT : ASPETTANDO UN MESSAGGIO" ) ;
			m = ( Message ) ois.readObject () ;
			System.out.println ( "SOCKET CLIENT : MESSAGGIO RICEVUTO : " + m.getOperation () ) ;
			switch ( m.getOperation () )  
			{
				case NAME_REQUESTING_REQUEST :
					s = getDataPicker ().onNameRequest () ;
					communicationParameters.add ( s ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE , communicationParameters ) ;
					oos.writeObject ( m ) ;
					oos.flush () ;
				break ;	
				case NAME_REQUESTING_RESPONSE_RESPONSE :
					communicationParameters.addAll ( CollectionsUtilities.newCollectionFromIterable ( m.getParameters () ) ) ;
					b = ( Boolean ) communicationParameters.get ( 0 ) ;
					s = ( String ) communicationParameters.get ( 1 ) ;
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
					communicationParameters.clear(); 
					communicationParameters.add( n );
					System.out.println ( "SOCKET CLIENT : SHEPERD COLOR REQUEST REQUEST - CREANDO MESSAGGIO DI RISPOSTA " ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , communicationParameters ) ;
					System.out.println ( "SOCKET CLIENT : SHEPERD COLOR REQUEST REQUEST - SCRIVENDO MESSAGGIO DI RISPOSTA" ) ;
					oos.writeObject ( m ) ;
					oos.flush () ;
				break;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
					sheperds = ( Iterable < Sheperd > ) m.getParameters ().iterator().next () ;
					choosenSheperd = getDataPicker ().onChooseSheperdForATurn ( sheperds ) ;
					communicationParameters.clear () ;
					communicationParameters.add ( choosenSheperd ) ;
					m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE , communicationParameters ) ;
					oos.writeObject ( m ) ;
					oos.flush () ;
				break ;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					sellableCards = (Iterable<SellableCard>) ois.readObject();
					oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE.toString () );
					oos.flush();
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
				case GENERIC_NOTIFICATION_NOTIFICATION:
					s = ois.readUTF();
				break;
				default :
				break ;
			}
			communicationParameters.clear();
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
		
}
