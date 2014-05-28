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
import java.net.Socket;

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
	
	/***/
	public String requestName () throws IOException  
	{
		String res ;
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		System.out.println ( "Socket Client Handler : Name Request Sent" ) ;
		res = ois.readUTF () ;
		if ( ClientHandlerClientCommunicationProtocolOperation.valueOf ( res ) == ClientHandlerClientCommunicationProtocolOperation.NAME_REQUESTING_RESPONSE )
		{
			res = ois.readUTF () ;
		}
		else
		{
			// ERROR MANAGEMENT STRATEGY
		}
		return res ;
	}
	
	/***/
	@Override
	public void notifyMatchWillNotStart ( String message ) throws IOException 
	{
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.MATCH_WILL_NOT_START_NOTIFICATION.toString () ) ;
		oos.flush () ;
		oos.writeUTF ( message);
		oos.flush () ;
	}
	
	/***/
	@Override
	public Color requestSheperdColor ( Iterable<Color> availableColors ) throws IOException 
	{
		String res;
		Color choosedColor = null;
		oos.writeUTF(ClientHandlerClientCommunicationProtocolOperation.SHEPERD_COLOR_REQUESTING_REQUEST.toString());
		oos.flush();
		oos.writeObject(availableColors);
		oos.flush();
		res = ois.readUTF();
		if ( ClientHandlerClientCommunicationProtocolOperation.valueOf( res ) == ClientHandlerClientCommunicationProtocolOperation.SHEPERD_COLOR_REQUESTING_RESPONSE )
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
	
	/***/
	@Override
	public void chooseCardsEligibleForSelling(Iterable<SellableCard> sellablecards) throws IOException 
	{
		String resp ;
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		oos.writeObject ( sellablecards ) ;
		oos.flush () ;
		//il giocatore modifica sellableCards e il server deve conscere le modifiche
	}

	/***/
	@Override
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer ) throws IOException 
	{
		String resp ;
		Sheperd res = null ;
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ; 
		oos.writeObject ( sheperdsOfThePlayer ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientHandlerClientCommunicationProtocolOperation.valueOf ( resp ) == ClientHandlerClientCommunicationProtocolOperation.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
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

	/***/
	@Override
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src ) throws IOException 
	{
		String resp ;
		SellableCard res = null ;
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		oos.writeObject ( src ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientHandlerClientCommunicationProtocolOperation.valueOf ( resp ) == ClientHandlerClientCommunicationProtocolOperation.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE )
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

	/***/
	@Override
	public GameMove doMove ( MoveFactory gameFactory , GameMap gameMap ) throws IOException 
	{
		String resp ;
		GameMove res = null ;
		oos.writeUTF ( ClientHandlerClientCommunicationProtocolOperation.DO_MOVE_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ; 
		oos.writeObject ( gameFactory ) ;
		oos.flush () ;
		oos.writeObject ( gameMap ) ;
		oos.flush () ;
		resp = ois.readUTF () ;
		if ( ClientHandlerClientCommunicationProtocolOperation.valueOf ( resp ) == ClientHandlerClientCommunicationProtocolOperation.DO_MOVE_REQUESTING_RESPONSE )
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

	/***/
	@Override
	public void genericNotification(String message) throws IOException {
		oos.writeUTF(ClientHandlerClientCommunicationProtocolOperation.GENERIC_NOTIFICATION_NOTIFICATION.toString());
		oos.flush();
		oos.writeUTF(message);
		oos.flush();
	}
	
	/***/
	@Override
	public void dispose () throws IOException  
	{
		oos.close () ;
		ois.close () ;
		clientChannel.close () ;
	}

	

	

	
	
}
