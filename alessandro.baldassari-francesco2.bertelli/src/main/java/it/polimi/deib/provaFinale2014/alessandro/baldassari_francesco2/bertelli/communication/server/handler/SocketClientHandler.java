package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
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
public class SocketClientHandler implements ClientHandler 
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
	public Color requestSheperdColor ( Iterable<Color> availableColors ) throws IOException 
	{
		String res;
		Color choosedColor = null;
		oos.writeUTF(ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_REQUEST.toString());
		oos.flush();
		oos.writeObject(availableColors);
		oos.flush();
		res = ois.readUTF();
		if ( ClientCommunicationProtocolMessage.valueOf( res ) == ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE )
		{
			try 
			{
				choosedColor = (Color) ois.readObject();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else 
		{
			//error management strategy
		}
			
		return choosedColor;
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void chooseCardsEligibleForSelling(Iterable<SellableCard> sellablecards) throws IOException 
	{
		String resp ;
		oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		oos.writeObject ( sellablecards ) ;
		oos.flush () ;
		//il giocatore modifica sellableCards e il server deve conscere le modifiche
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer ) throws IOException 
	{
		String resp ;
		Sheperd res = null ;
		oos.writeUTF ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ; 
		oos.writeObject ( sheperdsOfThePlayer ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientCommunicationProtocolMessage.valueOf ( resp ) == ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
		{
			try 
			{
				res = ( Sheperd ) ois.readObject () ;
			}
			catch ( ClassNotFoundException e) {
				// error management
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
