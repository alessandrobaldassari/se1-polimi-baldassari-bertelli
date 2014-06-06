package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

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
public abstract class ClientHandler
{

	/**
	 * Standard message to give to the User if the Match he was waiting for to start, will no begin. 
	 */
	public static final String MATCH_WILL_NOT_START_MESSAGE = "Sorry, but the game can not start now" ;
	
	/**
	 * This method should call the managed client for his name.
	 * 
	 * @return the name the client returns
	 * @throws IOException if something goes wrong with the communication.
	 */
	public String requestName () throws IOException 
	{
		String res ;
		Message m ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : BEGIN." ) ;
		m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_REQUEST , Collections.EMPTY_LIST ) ;
		write( m ) ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : MESSAGE WRITTEN." ) ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : WAITING FOR THE RESPONSE." ) ;
		m = read () ;
		System.out.println ( "CLIENT_HANDLER - REQUEST NAME : RESPONSE RECEIVED." ) ;
		if ( m.getOperation () == ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE ) 
		{
			System.out.println ( "CLIENT_HANDLER - REQUEST NAME : IN HEADER OK." ) ;
			res = ( String ) CollectionsUtilities.newListFromIterable ( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT_HANDLER - REQUEST NAME : IN PARAMETER OK." ) ;
			if ( res == null || res.compareToIgnoreCase ( Utilities.EMPTY_STRING ) == 0 )
				throw new IOException ( "BAD_RESPONSE" ) ;
		}
		else
		{
			throw new IOException ( "BAD_RESPONSE" ) ;
		}
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.NAME_REQUESTING_RESPONSE_RESPONSE , params ) ;
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.MATCH_STARTING_NOTIFICATION , Collections.EMPTY_LIST ) ;
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.MATCH_WILL_NOT_START_NOTIFICATION , Collections.<Serializable>singleton ( message ) ) ;
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) availableColors ) ) ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : SCRIVENDO IL MESSAGGIO" ) ;
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : ATTENDENDO LA RISPOSTA" ) ;
		m = read () ;
		System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : RISPOSTA RICEVUTA" ) ;
		if ( m.getOperation () == ClientCommunicationProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : HEADER OK." ) ;
			System.out.println ( m ) ;
			res = ( NamedColor ) CollectionsUtilities.newListFromIterable( m.getParameters () ).get ( 0 ) ;
			System.out.println ( "CLIENT HANDLER - REQUEST SHEPERD COLOR : IN PARAMETER OK : " + res ) ;
		}
		else 
			throw new IOException ( "BAD_RESPONSE" ) ; 
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST , Collections.singleton ( ( Serializable ) sellableCards ) ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : CARICANDO IL MESSAGGIO" ) ;
		write ( m ) ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : ATTENDENDO LA RISPOSTA" ) ;
		m = read () ;
		System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING : RISPOSTA RICEVUTA" ) ;
		if ( m.getOperation () == ClientCommunicationProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE CARDS ELIGIBLE FOR SELLING - RISPOSTA RICEVUTA " ) ;
			res = ( Iterable < SellableCard > ) CollectionsUtilities.newListFromIterable( m.getParameters () ).get ( 0 ) ;
		}
		else 
			throw new IOException ( "BAD_RESPONSE" ) ;
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) sheperdsOfThePlayer ) ) ;	
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : WRITING MESSAGE." ) ;
		write ( m ) ;
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : MESSAGE WRITTEN." ) ;
		m = read () ;
		System.out.println ( "CLIENT_HANDLER - CHOOSE SHEPERD FOR A TURN : MESSAGE RECEIVED." ) ;
		if ( m.getOperation() == ClientCommunicationProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE )
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
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src ) throws IOException 
	{
		Message m ;
		SellableCard res ;
		m = Message.newInstance ( ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST , Collections.<Serializable>singleton ( ( Serializable ) src ) ) ;
		write ( m ) ;
		m = read () ;
		if ( m.getOperation() == ClientCommunicationProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT HANDLER - CHOOSE CARD TO BUY : RESPONSE OK." ) ;
			res = (SellableCard ) CollectionsUtilities.newListFromIterable ( m.getParameters() ).get (0);
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
	public GameMove doMove ( MoveFactory gameFactory , GameMap gameMap ) throws IOException 
	{
		GameMove res ;
		Collection < Serializable > params ;
		Message m ;
		params = new ArrayList < Serializable > ( 2 ) ;
		params.add ( gameFactory ) ;
		params.add ( gameMap ) ;
		m = Message.newInstance ( ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_REQUEST , params ) ;
		write ( m ) ;
		m = read () ;
		if ( m.getOperation () == ClientCommunicationProtocolMessage.DO_MOVE_REQUESTING_RESPONSE )
		{
			System.out.println ( "CLIENT_HANDLER - DO_MOVE : RESPONSE HEADER OK." ) ;
			res = ( GameMove ) CollectionsUtilities.newListFromIterable( m.getParameters() ).get ( 0 ) ;
		}
		else
			throw new IOException ( "BAD_RESPONSE" ) ;
		return res ;
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
		m = Message.newInstance ( ClientCommunicationProtocolMessage.GENERIC_NOTIFICATION_NOTIFICATION , Collections.<Serializable>singleton ( message ) ) ;
		write ( m ) ;
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
