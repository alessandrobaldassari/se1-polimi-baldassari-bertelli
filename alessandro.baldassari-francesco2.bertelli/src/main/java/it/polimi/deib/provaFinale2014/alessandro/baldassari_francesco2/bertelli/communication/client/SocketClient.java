package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandlerClientCommunicationProtocolOperation;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NetworkUtilities;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/***/
public class SocketClient extends Client 
{

	/**
	 * The IP address of the server where connect to. 
	 */
	public static final String SERVER_IP_ADDRESS = "127.0.0.1" ;
	
	/**
	 * The TCP port of the server where connect to. 
	 */
	public static final int SERVER_PORT_NUMBER = 3333 ;
	
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
		
	public SocketClient () throws IOException  
	{
		super () ;
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
		InetAddress inetAddress ;
		SocketAddress socketAddress ;
		inetAddress = InetAddress.getByAddress ( NetworkUtilities.fromStringToByteArrayIPAddress ( SERVER_IP_ADDRESS ) ) ;
		socketAddress = new InetSocketAddress ( inetAddress , SERVER_PORT_NUMBER ) ;
		channel.connect ( socketAddress ) ;
		ois = new ObjectInputStream ( channel.getInputStream () ) ;
		oos = new ObjectOutputStream ( channel.getOutputStream () ) ;
		System.out.println ( "Socket Client : Connected" + channel.isConnected() ) ;
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
		MoveFactory moveFactory ;
		GameMap gameMap ;
		Iterable < Sheperd > sheperds ;
		Iterable <SellableCard> sellableCards;
		Iterable <Color> colors ;
		String command ;
		String s ;
		try 
		{
			System.out.println ( "Socket Client : Before Receiving Command" ) ;
			command = ois.readUTF () ; 
			System.out.println ( "Sockeet Client : command received" );
			switch ( ClientHandlerClientCommunicationProtocolOperation.valueOf ( command ) ) 
			{
				case NAME_REQUESTING_REQUEST :
					oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_RESPONSE.toString () );
					oos.flush () ;
					oos.writeUTF ( "Pippo" ) ;
					oos.flush () ;
					System.out.println ( "sent name" ) ;
				break ;	
				case SHEPERD_COLOR_REQUESTING_REQUEST:
					colors = (Iterable<Color>) ois.readObject();
					oos.writeUTF( ClientHandlerClientCommunicationProtocolOperation.SHEPERD_COLOR_REQUESTING_RESPONSE.toString() );
					oos.flush();
					oos.writeObject(Color.BLUE);
					oos.flush();
					System.out.println("sent color");
				break;
				case MATCH_WILL_NOT_START_NOTIFICATION:
					s = ois.readUTF();
				break;
				case GENERIC_NOTIFICATION_NOTIFICATION:
					s = ois.readUTF();
				break;
				case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
					sellableCards = (Iterable<SellableCard>) ois.readObject();
					oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE.toString () );
					oos.flush();
				break ;
				case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
					sheperds = (Iterable<Sheperd>) ois.readObject () ;
					oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE.toString () ) ;
				break ;
				case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
					sellableCards = (Iterable<SellableCard>) ois.readObject () ;
					oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE.toString () );
				break ;
				case DO_MOVE_REQUESTING_REQUEST :
					moveFactory = (MoveFactory) ois.readObject () ;
					gameMap = (GameMap) ois.readObject () ;
					oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.DO_MOVE_REQUESTING_RESPONSE.toString () ) ;
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
