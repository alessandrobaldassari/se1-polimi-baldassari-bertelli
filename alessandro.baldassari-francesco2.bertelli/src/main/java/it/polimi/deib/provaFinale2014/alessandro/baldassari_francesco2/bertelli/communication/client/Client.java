package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.message.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements the communication end-point by the Client side.
 * It's implementation technically relies on an infinite loop executing in the run method of this
 * class where subclasses has the chance to implement their server-listening logic, server-sending-messages
 * logic and so on.
 */
public abstract class Client implements Runnable , Terminable
{
	
	private int uid ;
	
	/**
	 * Boolean flag indicating if the connections pointing to the server are opened. 
	 */
	private boolean technicallyOn ;
	
	/**
	 * A CommunicationProtocolResponser to retrieve the data to send to the Server. 
	 */
	private CommunicationProtocolResponser dataPicker ;
	
	/**
	 * @param dataPicker the value for the dataPicker field.
	 * @throws IllegalArgumentException if the dataPicker field is null.
	 */
	protected Client ( CommunicationProtocolResponser dataPicker )
	{
		if ( dataPicker != null )
		{
			this.dataPicker = dataPicker ;
			technicallyOn = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	protected int getUID ()
	{
		return uid ;
	}
	
	/**
	 * This method is the one this class' users has to call to initialize the connections
	 * used by this client.
	 * It also enable the server part of this method to start.
	 * 
	 * @throws IOException if something goes wrong with this method.
	 */
	public void openConnection () throws IOException  
	{
		if ( technicallyOn == false )
		{
			directTechnicalConnect () ;
			technicallyOn = true ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	public void terminate () 
	{
		if ( technicallyOn == true )
			technicallyOn = false ;
	}
	
	/**
	 * This method is the one this class's users has to call in order to close the connections 
	 * used by this client.
	 * This will probably stop the Thread from its running, also the behavior depends also by
	 * the subclasses implementation.
	 */
	public void closeConnection () throws IOException 
	{
		technicalDisconnect () ;
		technicallyOn = false ; 
	}
	
	/**
	 * This method represents the one where a Client has to implement the mechanism which open all
	 * the connection to the server
	 * It's not a logic related method, just a technical one.
	 * 
	 * @throws IOException if something goes wrong with the communication operations.
	 */
	protected abstract void directTechnicalConnect () throws IOException ;
		
	/**
	 * Method called by the workflow when the connection fell.
	 * It tries to resume a connection with some mechanism.
	 * If the method effectively resume the connection, this methods completes normally.
	 * If the connection can not be resumed, it launches an IOException.
	 * At this point, nothing else can be done to resume it.  
	 */
	protected abstract void resumeConnectionConnect () throws IOException ;
	
	/**
	 * This method represents the one where a Client has to implement the mechanism which closes
	 * all the connection to the server.
	 * It's not a logic related method, just a technical one.  
	 */
	protected abstract void technicalDisconnect () throws IOException ;
	
	/**
	 * Technical subclasses has to implement this method to retrieve the next incoming message.
	 * 
	 * @return the next incoming message.
	 * @throws IOException if there is a problem due to Message reading.
	 */
	protected abstract Message read () throws IOException ;
		
	/**
	 * Technical subclasses has to implement this method to write a Message over the net.
	 * 
	 * @param messageToWrite the message to write.
	 * @throws IOException if something goes wrong with the Message Writing
	 */
	protected abstract void write ( Message messageToWrite ) throws IOException ;
	
	/**
	 * With the client has to do when a communication operation is notified to be finished.
	 * Actions here may be synchronization operations , buffer management and so on.
	 * 
	 * @throws IOException if something goes wrong during the execution.
	 */
	protected abstract void operationFinished () throws IOException ;
	
	/**
	 * AS THE SUPER'S ONE.
	 * For every time the value of the technicallyOn attribute is true, it continues to call
	 * the communicationProtocolImpl method.
	 */
	@Override
	public void run () 
	{
		List < Serializable > inParams ;
		List < Serializable > outParams ;
		Iterable < SellableCard > cards ;
		Iterable < Sheperd > sheperds ;
		Iterable < Road > roads ;
		Serializable se ;
		MoveSelection move ;
		MoveSelector gf ;
		GameMap gm ;
		Message m ;
		String s ;
		Sheperd sh ;
		Road r ;
		int money ;
		boolean b ;
		outParams = new LinkedList < Serializable > () ;
		while ( technicallyOn )
		{
			try 
			{
				System.out.println ( "CLIENT - RUN : WAITING FOR A MESSAGE." ) ;
				m = read () ;
				System.out.println ( "CLIENT - RUN : MESSAGE RECEIVED : " + m.getOperation() ) ; 
				inParams = CollectionsUtilities.newListFromIterable ( m.getParameters () ) ;
				System.out.println ( "CLIENT : PARAMETERS LOADED + " + m.getParameters () ) ;
				outParams.clear () ;
				switch ( m.getOperation() ) 
				{
					case UID_NOTIFICATION :
						uid = ( Integer ) inParams.get ( 0 ) ;
					break ;
					case NAME_REQUESTING_REQUEST :
						s = dataPicker.onNameRequest () ;
						outParams.add ( s ) ;
						m = Message.newInstance ( GameProtocolMessage.NAME_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case NAME_REQUESTING_RESPONSE_RESPONSE :
						b = ( Boolean ) inParams.get ( 0 ) ;
						s = (String) inParams.get ( 1 ) ;
						dataPicker.onNameRequestAck  ( b , s ) ;
					break ;
					case MATCH_STARTING_NOTIFICATION :
						dataPicker.onNotifyMatchStart () ;
					break ;
					case MATCH_WILL_NOT_START_NOTIFICATION:
						s = ( String ) inParams.get ( 0 ) ;
						dataPicker.onMatchWillNotStartNotification ( s ) ;
					break ;
					case SHEPERD_COLOR_REQUESTING_REQUEST:
						write ( sheperdColorRequestingRequest ( inParams ) ) ;
					break;
					case SHEPERD_INIT_ROAD_REQUESTING_REQUEST :
						roads = ( Iterable<Road> ) inParams.get ( 0 ) ;
						r = dataPicker.chooseInitRoadForSheperd ( roads ) ;
						outParams.add ( r ) ;
						m = Message.newInstance ( GameProtocolMessage.SHEPERD_INIT_ROAD_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST :
						sheperds = ( Iterable < Sheperd > ) inParams.get ( 0 ) ;
						sh = dataPicker.onChooseSheperdForATurn ( sheperds ) ;
						outParams.add ( sh ) ;
						m = Message.newInstance ( GameProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case DO_MOVE_REQUESTING_REQUEST :
						gf = ( MoveSelector ) inParams.get ( 0 ) ;
						gm = ( GameMap ) inParams.get ( 1 ) ;
						move = dataPicker.onDoMove ( gf , gm ) ;
						outParams.add ( move ) ;
						m = Message.newInstance ( GameProtocolMessage.DO_MOVE_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST:
						cards = ( Iterable < SellableCard > ) inParams.get ( 0 ) ;
						cards = dataPicker.onChooseCardsEligibleForSelling ( cards ) ;
						outParams.add ( ( Serializable ) cards ) ;
						m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST :
						cards = ( Iterable < SellableCard > ) inParams.get ( 0 ) ;
						money = ( Integer ) inParams.get ( 1 ) ;
						cards = dataPicker.onChoseCardToBuy ( cards , money ) ; 
						outParams.add ( ( Serializable ) cards ) ;
						m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case GUI_CONNECTOR_NOTIFICATION :
						se = inParams.get ( 0 ) ;
						dataPicker.onGUIConnectorOnNotification ( se ) ;
					break ;
					case GENERIC_NOTIFICATION_NOTIFICATION :
						s = ( String ) inParams.get ( 0 ) ;							
						dataPicker.generationNotification ( s ) ;
					break;
					case GAME_CONCLUSION_NOTIFICATION :
						s = ( String ) inParams.get ( 0 ) ;
						dataPicker.generationNotification ( s ) ;
					break ;
					default :
						throw new IOException ( "UNMANAGED_OPERATION" ) ;
				}
				operationFinished () ;
				inParams.clear () ;
				System.out.println ( "CLIENT : OPERATION FINISHED." ) ;
			}
			catch ( IOException e ) 
			{
				System.out.println ( "CLIENT - RUN : IOEXCEPTION GENERATED " + Utilities.CARRIAGE_RETURN + "ERROR MESSAGE : " + e.getMessage () ) ;
				// inform the user the connection fell.
				dataPicker.generationNotification ( "Server connection loosed." + Utilities.CARRIAGE_RETURN + "Try to resume it" + Utilities.CARRIAGE_RETURN + "It will take some time." );
				try 
				{
					// try to resume the connection
					resumeConnectionConnect () ;
					// if the method above is finished, the connection is now on.
				} 
				catch ( IOException e1 ) 
				{
					// nothing to do, the connection is dead and can not be resumed.
					dataPicker.generationNotification ( "Can not try to resume the connection." + Utilities.CARRIAGE_RETURN + "Sorry but there is nothing to do !" + Utilities.CARRIAGE_RETURN + "The program will be closed." );
					technicallyOn = false ;
				}
			}
		}
	}
	
	private Message sheperdColorRequestingRequest ( List < Serializable > inParams ) throws IOException 
	{
		Iterable < NamedColor > colors ;
		NamedColor n ;
		Message m ;
		colors = ( Iterable < NamedColor > ) inParams.get ( 0 ) ;
		n = dataPicker.onSheperdColorRequest ( colors ) ;
		System.out.println ( n ) ;
		m = Message.newInstance ( GameProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , Collections.<Serializable>singleton ( n ) ) ;
		return m ;
	}
	
}
