package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class is a Socket implementation of the ClientHandler interface, so it manages the game
 * communication with a Client that uses the Socket technology as its data transfer method. 
 */
public class SocketClientHandler implements ClientHandler , Serializable
{

	/**
	 * A Socket object that points the Client. 
	 */
	private Socket clientChannel ;
	
	/**
	 * An ObjectInputStream object used to extend the features of the socket's attribute InputStream. 
	 */
	private ObjectInputStream ois ;
	
	/**
	 * An ObjectOutputStream object used to extend the features of the socket's attribute OutputStream. 
	 */
	private ObjectOutputStream oos ;
	
	/**
	 * @param clientChannel a Socket object that points to the client.
	 * @throws IllegalArgumentException if the clientChannel parameter is null 
	 */
	public SocketClientHandler ( Socket clientChannel ) throws IOException  
	{ 
		if ( clientChannel != null  )
		{
			this.clientChannel = clientChannel ;
			oos = new ObjectOutputStream ( clientChannel.getOutputStream () ) ;
			ois = new ObjectInputStream ( clientChannel.getInputStream () ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String requestName () throws IOException  
	{
		ClientCommunicationProtocolMessage op ;
		Message m ;
		String res = null ;
		try
		{
			op = ClientCommunicationProtocolMessage.NAME_REQUESTING_REQUEST ;
			m = Message.newInstance ( op , Collections.EMPTY_LIST ) ;
			oos.writeObject ( m ) ;
			oos.flush () ;
			System.out.println ( "Socket Client Handler : Name Request Sent" ) ;
			m = (Message) ois.readObject () ;
			System.out.println ( res ) ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE )
			{
				System.out.println( res ) ;
				res = (String) m.getParameters().iterator().next();
				System.out.println ( "NAME = " + res ) ;
			}
			else
			{
				// ERROR MANAGEMENT STRATEGY
			}
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void notifyNameChoose ( boolean isNameOk , String note ) throws IOException 
	{
		Collection < Serializable > c ;
		Message m ;
		c = new ArrayList < Serializable > ( 2 ) ;
		c.add ( isNameOk ) ;
		c.add ( note ) ;
 		m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE_RESPONSE , c ) ; 
 		oos.writeObject ( m ) ;
 		oos.flush () ;
	}

	/***/
	public void notifyMatchStart () throws IOException 
	{
		Message m ;
		m = Message.newInstance ( ClientCommunicationProtocolMessage.MATCH_STARTING_NOTIFICATION , Collections.EMPTY_LIST ) ;
		oos.writeObject ( m ) ;
		oos.flush () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void notifyMatchWillNotStart ( String message ) throws IOException 
	{
		Message m ;
		ClientCommunicationProtocolMessage operation ;
		operation = ClientCommunicationProtocolMessage.MATCH_WILL_NOT_START_NOTIFICATION ;
		m = Message.newInstance ( operation , Collections.singleton ( ( Serializable ) message ) ) ;
		oos.writeObject ( m ) ;
		oos.flush () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor requestSheperdColor ( Iterable< NamedColor > availableColors ) throws IOException 
	{
		Message m ;
		NamedColor res ;
		try 
		{
			res = null ;
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : CREANDO IL MESSAGGIO" ) ;
			m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) availableColors ) ) ;
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : MESSAGGIO CREATO" ) ;
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : INVIO MESSAGGIO" ) ;
			oos.writeObject( m ) ;
			oos.flush();
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : MESSAGGIO INVIATO" ) ;
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : ATTENDENDO MESSAGGIO DI RISPOSTA" ) ;
			m = ( Message ) ois.readObject () ;
			System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : MESSAGGIO DI RISPOSTA RICEVUTO : " + m.getOperation () ) ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE )
			{	
				System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR - COLORE RICEVUTO : " + ( ( NamedColor ) m.getParameters ().iterator ().next () ).getName () ) ;				
				res = ( NamedColor ) m.getParameters ().iterator ().next () ;
			}
			else 
			{
				System.out.println ( "SOCKET CLIENT HANDLER - REQUEST SHEPERD COLOR : IL CLIENT NON HA RISPOSTO IN ACCORDO CON IL PROTOCOLLO" ) ;
				throw new RuntimeException () ;
			}
			System.out.println ( "XXXXXXXXXXXXX" ) ;
		}
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace () ;
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
			m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) sheperdsOfThePlayer ) ) ;
			oos.flush () ;
			m = (Message) ois.readObject () ;
			if ( m.getOperation () == ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
			{
				res = ( Sheperd ) ois.readObject () ;
			}
			else
			{
				// error management
			}
		}
		catch ( ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > chooseCardsEligibleForSelling ( Iterable < SellableCard > sellablecards) throws IOException 
	{
		String resp ;
		oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		oos.writeObject ( sellablecards ) ;
		oos.flush () ;
		//il giocatore modifica sellableCards e il server deve conscere le modifiche
		return null ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src ) throws IOException 
	{
		String resp ;
		SellableCard res = null ;
		oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		oos.writeObject ( src ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientCommunicationProtocolMessage.valueOf ( resp ) == ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE )
		{
			try 
			{
				res = (SellableCard) ois.readObject() ;
			}
			catch (ClassNotFoundException e) {
				// error management ;
				e.printStackTrace();
			}
		}
		else
		{
			// error management
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GameMove doMove ( MoveFactory gameFactory , GameMap gameMap ) throws IOException 
	{
		String resp ;
		GameMove res = null ;
		oos.writeUTF ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ; 
		oos.writeObject ( gameFactory ) ;
		oos.flush () ;
		oos.writeObject ( gameMap ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientCommunicationProtocolMessage.valueOf ( resp ) == ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE )
		{
			try 
			{
				res = (GameMove) ois.readObject () ;
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		else
		{
			// error management here
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void genericNotification(String message) throws IOException 
	{
		oos.writeUTF(ClientCommunicationProtocolMessage.GENERIC_NOTIFICATION_NOTIFICATION.toString());
		oos.flush();
		oos.writeUTF(message);
		oos.flush();
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void dispose () throws IOException  
	{
		oos.close () ;
		ois.close () ;
		clientChannel.close () ;
	}

}
