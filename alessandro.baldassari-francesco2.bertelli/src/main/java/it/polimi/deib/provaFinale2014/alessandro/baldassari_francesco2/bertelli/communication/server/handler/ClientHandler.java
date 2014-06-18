package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerWantsToExitGameException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This interface is an abstraction that represents all the operations a ClientHandler has to do.
 * A ClientHandler is a server component whose target is to manage the connection between the server
 * itself and the client. 
 */
public abstract class ClientHandler < T >
{

	private static int uidGenerator = 0 ;
	/***/
	private final int UID ;
	
	/**
	 * An object to technically do a net connection. 
	 */
	private ClientHandlerConnector < T > connector ;
	
	/**
	 * @param connector
	 * @throws IOException
	 * @throws IllegalArgumentException 
	 */
	public ClientHandler ( ClientHandlerConnector < T > connector ) throws IOException
	{
		if ( connector != null )
		{
			uidGenerator ++ ;
			UID = uidGenerator ;
			rebind ( connector ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public ClientHandlerConnector < T > rebind ( ClientHandlerConnector < T > newConnector ) throws IOException
	{
		ClientHandlerConnector < T > old ;
		if ( newConnector != null )
		{
			old = connector ;
			connector = newConnector ;
			technicalRebinding () ;
		}
		else
			throw new IllegalArgumentException () ;
		return old ;
	}
	
	public int getUID ()
	{
		return UID ;
	}
	
	/***/
	protected T getConnector () 
	{
		return connector.getConnector () ;
	}
	
	/**
	 * 
	 */
	protected abstract void technicalRebinding () throws IOException ;
	
	/**
	 * Notify the User the Id has been choosen for him  
	 * 
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void uidNotification () throws IOException 
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.UID_NOTIFICATION , Collections.<Serializable>singleton ( UID ) ) ;
		write ( m ) ;
	}
	
	/**
	 * This method should call the managed client for his name.
	 * 
	 * @return the name the client returns
	 * @throws IOException if something goes wrong with the communication.
	 */
	public String requestName () throws IOException , PlayerWantsToExitGameException 
	{
		String res ;
		Message m ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : BEGIN." ) ;
		m = Message.newInstance ( GameProtocolMessage.NAME_REQUESTING_REQUEST , Collections.<Serializable> emptyList() ) ;
		write( m ) ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : MESSAGE WRITTEN." ) ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : WAITING FOR THE RESPONSE." ) ;
		m = read () ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : RESPONSE RECEIVED." ) ;
		if ( m.getOperation () == GameProtocolMessage.NAME_REQUESTING_RESPONSE ) 
		{
			System.out.println ( "CLIENT_HANDLER - REQUEST NAME : IN HEADER OK." ) ;
			res = ( String ) CollectionsUtilities.newListFromIterable ( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT_HANDLER - REQUEST NAME : IN PARAMETER OK : " + res ) ;
			if ( res == null )
				throw new PlayerWantsToExitGameException () ;
		}
		else
			throw new PlayerWantsToExitGameException ( "BAD_RESPONSE" ) ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : END" ) ;
		return res ;
	}

	/**
	 * This method should notify the User if the name he choosed is ok or not. 
	 * 
	 * @param isNameOk if the name the User received is ok or note.
	 * @param note eventually note associated with this event.
	 */
	public void notifyNameChoose ( boolean isNameOk , String note ) throws IOException 
	{
		Collection < Serializable > params ;
		Message m ;
		System.out.println ( "CLIENT_HANDLER - NOTIFY NAME CHOOSE : BEGIN" ) ;
		params = new ArrayList < Serializable > ( 2 ) ;
		params.add ( isNameOk ) ;
		params.add ( note ) ;
		m = Message.newInstance ( GameProtocolMessage.NAME_REQUESTING_RESPONSE_RESPONSE , params ) ;
		write ( m ) ;
		System.out.println ( "CLIENT_HANDLER - NOTIFY NAME CHOOSE : MESSAGE WRITTEN" ) ;
		System.out.println ( "CLIENT_HANDLER - NOTIFY NAME CHOOSE : END" ) ;
	}
	
	/**
	 * This method is just a notify to the user that the Match is starting. 
	 */
	public void notifyMatchStart () throws IOException 
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.MATCH_STARTING_NOTIFICATION , Collections.EMPTY_LIST ) ;
		write ( m ) ;
		System.out.println ( "CLIENT_HANDLER - NOTIFY MATCH START : END" ) ;
	}
	
	/**
	 * Sends a special message to the client, notifying him that the match that he was playing
	 * for start will not start
	 * 
	 * @param a message to explain the client the situation
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void notifyMatchWillNotStart ( String message ) throws IOException 
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.MATCH_WILL_NOT_START_NOTIFICATION , Collections.<Serializable>singleton ( message ) ) ;
		write ( m ) ;
		System.out.println ( "CLIENT_HANDLER - NOTIFY MATCH WILL NOT START : END" ) ;
	}
	
	/**
	 * Ask the color to choose a color for one of his Sheperds.
	 * The color must be in the parameter list.
	 * 
	 * @param the list of colors where the client may choose the one he wants.
	 * @return the color the client chooses.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public NamedColor requestSheperdColor( Iterable < NamedColor > availableColors ) throws IOException 
	{
		NamedColor res ;
		Message m ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : CREANDO IL MESSAGGIO" ) ;
		m = Message.newInstance ( GameProtocolMessage.SHEPERD_COLOR_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) availableColors ) ) ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : SCRIVENDO IL MESSAGGIO" ) ;
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : ATTENDENDO LA RISPOSTA" ) ;
		m = read () ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : RISPOSTA RICEVUTA" ) ;
		if ( m.getOperation () == GameProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : HEADER OK." ) ;
			System.out.println ( "Message : " + m ) ;
			res = ( NamedColor ) CollectionsUtilities.newListFromIterable( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : IN PARAMETER OK : " + res ) ;
		}
		else 
			throw new IOException ( "BAD_RESPONSE" ) ; 
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : FINISH " ) ;
		return res ;
	}
		
	/**
	 * Ask the client to say which of his cards are eligible for selling and at what price.
	 * 
	 * @param the client cards, those he has to set the selling properties.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public Iterable < SellableCard > chooseCardsEligibleForSelling ( Iterable <SellableCard> sellableCards ) throws IOException 
	{
		Iterable < SellableCard > res ;
		Message m ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : CREANDO IL MESSAGGIO" ) ;
		m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST , Collections.< Serializable >singleton ( ( Serializable ) sellableCards ) ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : INVIANDO IL MESSAGGIO" ) ;
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : ATTENDENDO LA RISPOSTA" ) ;
		m = read () ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : RISPOSTA RICEVUTA" ) ;
		if ( m.getOperation () == GameProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING - HEADER OK." ) ;
			res = ( Iterable < SellableCard > ) CollectionsUtilities.newListFromIterable( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING - PARAMETER OK : " + res ) ;
		}
		else 
			throw new IOException ( "BAD_RESPONSE" ) ;
		return res ;
	}
	
	/**
	 * @param availableRoads
	 * @return 
	 */
	public Road chooseInitialRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException 
	{
		Road res ;
		Message m ;
		System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : CREANDO IL MESSAGGIO" ) ;		
		m = Message.newInstance ( GameProtocolMessage.SHEPERD_INIT_ROAD_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) availableRoads ) ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : INVIANDO MESSAGGIO" ) ;		
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : INVIANDO MESSAGGIO" ) ;		
		m = read () ;
		System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : RISPOSTA RICEVUTA" ) ;		
		if ( m.getOperation () == GameProtocolMessage.SHEPERD_INIT_ROAD_REQUESTING_RESPONSE ) 
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : HEADER OK." ) ;		
			res = ( Road ) CollectionsUtilities.newListFromIterable ( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : PARAMETER OK : " + res ) ;					
		}
		else
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE INITIAL ROAD FOR SHEPERD : BAD HEADER" ) ;		
			throw new IOException ( "BAD RESPONSE" ) ;
		}
		return res ;
	}
	
	/**
	 * Ask the client to choose a Sheperd he will use for the current Game Turn.
	 * 
	 * @param sheperdsOfThePlayer the collection where the client has to choose the Sheperd he
	 *        wants to use.
	 * @return the choosen Sheperd
	 * @throws IOException if something goes wrong with the communication.
	 */
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer ) throws IOException 
	{
		Message m ;
		Sheperd res ;
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : CREATING MESSAGE." ) ;		
		m = Message.newInstance ( GameProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) sheperdsOfThePlayer ) ) ;	
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : WRITING MESSAGE." ) ;
		write ( m ) ;
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : MESSAGE WRITTEN." ) ;
		m = read () ;
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : MESSAGE RECEIVED." ) ;
		if ( m.getOperation() == GameProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : RESPONSE HEADER OK." ) ;
			res = ( Sheperd ) ( CollectionsUtilities.newListFromIterable ( m.getParameters () ).get ( 0 ) ) ;
			System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : IN PARAMETER OK : " + res ) ;		
		}
		else
		{
			System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : BAD PARAMETER" ) ;		
			throw new IOException ( "BAD_REQUEST" ) ;
		}
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : FINISH." ) ;		
		return res ;
	}
	
	/**
	 * Ask the client which card between those contained in the src parameter he wants to buy.
	 * 
	 * @param src the cards where the client can choose
	 * @return the card the client wants to buy, null if he doesn't want to buy cards anymore.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public Iterable < SellableCard > chooseCardToBuy ( Iterable < SellableCard > src , Integer playerMoney ) throws IOException 
	{
		Message m ;
		Collection < Serializable > params ;
		Iterable < SellableCard > res ;
		params = new ArrayList < Serializable > ( 2 ) ;
		params.add ( (Serializable) src ) ;
		params.add ( playerMoney ) ;
		m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST , params ) ;
		write ( m ) ;
		m = read () ;
		if ( m.getOperation() == GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE CARD TO BUY : RESPONSE OK." ) ;
			res = ( Iterable < SellableCard > ) CollectionsUtilities.newListFromIterable ( m.getParameters() ).get (0);
		}
		else
			throw new IOException ( "BAD_RESPONSE" ) ;
		return res ;
	}
	
	/**
	 * Ask the client to choose which move he wants to do in the current Game turn.
	 * 
	 * @param gameFactory the object the client has to use to generate the Move he wants to do.
	 * @param gameMap the map of the Game, containing all the information the client needs to 
	 *        decide which move do.
	 * @return the move the client wants to do.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public MoveSelection doMove ( MoveSelector moveSelector , GameMap gameMap ) throws IOException 
	{
		MoveSelection res ;
		Collection < Serializable > params ;
		Message m ;
		params = new ArrayList < Serializable > ( 2 ) ;
		params.add ( moveSelector ) ;
		params.add ( gameMap ) ;
		m = Message.newInstance ( GameProtocolMessage.DO_MOVE_REQUESTING_REQUEST , params ) ;
		write ( m ) ;
		m = read () ;
		if ( m.getOperation () == GameProtocolMessage.DO_MOVE_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT_HANDLER - DO_MOVE : RESPONSE HEADER OK." + m.getParameters() ) ;
			res = ( MoveSelection ) CollectionsUtilities.newListFromIterable( m.getParameters() ).get ( 0 ) ;
		}
		else
			throw new IOException ( "BAD_RESPONSE" ) ;
		return res ;
	}
	
	public void gameConclusionNotification ( String cause ) throws IOException  
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.GAME_CONCLUSION_NOTIFICATION , Collections.<Serializable>singleton ( cause ) ) ;
		write ( m ) ;
	}
	
	/**
	 * This methods sends to the client a generic message.
	 * 
	 * @param message the message to send.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void genericNotification(String message) throws IOException 
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.GENERIC_NOTIFICATION_NOTIFICATION , Collections.<Serializable>singleton ( message ) ) ;
		write ( m ) ;
	}
	
	/***/
	public void sendGuiConnectorNotification ( Serializable guiConnector ) throws IOException
	{
		Message m ;
		m = Message.newInstance ( GameProtocolMessage.GUI_CONNECTOR_NOTIFICATION , Collections.singleton ( guiConnector ) ) ;
		write ( m ) ;
	}
	
	/***/
	public void sendResumeSucceedNotification () throws IOException 
	{
		Message m ;
		System.out.println ( "CLIENT HANDLER - SEND RESUME SUCCEED NOTIFICATION : PREPARING MESSAGE" ) ;
		m = Message.newInstance ( GameProtocolMessage.RESUME_SUCCEEED , Collections.<Serializable> emptyList() ) ;
		System.out.println ( "CLIENT HANDLER - SEND RESUME SUCCEED NOTIFICATION : MESSAGE PREPARED" ) ;
		System.out.println ( "CLIENT HANDLER - SEND RESUME SUCCEED NOTIFICATION : WRITING MESSAGE" ) ;		
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - SEND RESUME SUCCEED NOTIFICATION : MESSAGE WRITTEN" ) ;		
		System.out.println ( "CLIENT HANDLER - SEND RESUME SUCCEED NOTIFICATION : END" ) ;
	}
	
	/**
	 * This method closes and frees all the resources held by this ClientHandler.
	 * 
	 * @throws IOException if something goes wrong with the operations performed.
	 */
	public abstract void dispose () throws IOException ;
	
	/***/
	protected abstract Message read () throws IOException ;
	
	/***/
	protected abstract void write ( Message m ) throws IOException ;
		
}
