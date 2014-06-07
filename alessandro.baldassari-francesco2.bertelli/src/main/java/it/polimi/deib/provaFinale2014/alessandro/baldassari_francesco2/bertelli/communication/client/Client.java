package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements the communication end-point by the Client side.
 * It's implementation technically relies on an infinite loop executing in the run method of this
 * class where subclasses has the chance to implement their server-listening logic, server-sending-messages
 * logic and so on.
 */
public abstract class Client extends Thread implements Terminable
{
	
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
	
	/**
	 * This method is the one this class' users has to call to initialize the connections
	 * used by this client.
	 * It also enable the server part of this method to start.
	 * 
	 * @throws IOException if something goes wrong with this method.
	 */
	public void openConnection () throws IOException  
	{
		directTechnicalConnect () ;
		technicallyOn = true ;
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
		
	/***/
	protected abstract void resumeConnectionConnect () throws IOException ;
	
	/**
	 * This method represents the one where a Client has to implement the mechanism which closes
	 * all the connection to the server.
	 * It's not a logic related method, just a technical one.  
	 */
	protected abstract void technicalDisconnect () throws IOException ;
	
	/**
	 * @return
	 * @throws 
	 */
	protected abstract Message read () throws IOException ;
		
	/**
	 * @return
	 * @throws 
	 */
	protected abstract void write ( Message m ) throws IOException ;
	
	/**
	 * @throws IOException
	 */
	protected abstract void operationFinished () throws IOException ;
	
	/**
	 * Getter method for the dataPicker property.
	 * 
	 * @return the dataPicker property.
	 */
	protected CommunicationProtocolResponser getDataPicker () 
	{
		return dataPicker ;
	}
	
	/**
	 * AS THE SUPER'S ONE.
	 * This methods do things in order for this Thread to start only if the technicallyOn parameter
	 * is true, so only if the openConnection method has been called before. 
	 */
	@Override
	public void start () 
	{
		if ( technicallyOn )
			super.start () ;
	}
	
	private void connectionDownManagement () 
	{
		
	}
	
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
		Iterable < NamedColor > colors ;
		Iterable < SellableCard > cards ;
		Iterable < Sheperd > sheperds ;
		Iterable < Road > roads ;
		GameMove move ;
		MoveFactory gf ;
		GameMap gm ;
		SellableCard c ;
		NamedColor n ;
		Message m ;
		String s ;
		Sheperd sh ;
		Road r ;
		boolean b ;
		outParams = new LinkedList < Serializable > () ;
		while ( technicallyOn )
		{
			try 
			{
				System.out.println ( "CLIENT : WAITING FOR A MESSAGE." ) ;
				m = read () ;
				System.out.println ( "CLIENT : MESSAGE RECEIVED." ) ;
				inParams = CollectionsUtilities.newListFromIterable ( m.getParameters () ) ;
				System.out.println ( "CLIENT : PARAMETERS LOADED." ) ;
				outParams.clear () ;
				switch ( m.getOperation() ) 
				{
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
						colors = ( Iterable < NamedColor > ) inParams.get ( 0 ) ;
						n = dataPicker.onSheperdColorRequest ( colors ) ;
						outParams.clear();
						outParams.add ( n ) ;
						System.out.println ( outParams ) ;
						m = Message.newInstance ( GameProtocolMessage.SHEPERD_COLOR_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
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
						sh = getDataPicker ().onChooseSheperdForATurn ( sheperds ) ;
						outParams.add ( sh ) ;
						m = Message.newInstance ( GameProtocolMessage.CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case DO_MOVE_REQUESTING_REQUEST :
						gf = ( MoveFactory ) inParams.get ( 0 ) ;
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
						c = getDataPicker ().onChoseCardToBuy ( cards ) ; 
						outParams.add ( c ) ;
						m = Message.newInstance ( GameProtocolMessage.CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE , outParams ) ;
						write ( m ) ;
					break ;
					case GENERIC_NOTIFICATION_NOTIFICATION :
						s = ( String ) inParams.get ( 0 ) ;
						dataPicker.generationNotification ( s ) ;
					break;
					default :
						throw new IOException ( "UNMANAGED_OPERATION" ) ;
				}
				operationFinished () ;
				inParams.clear () ;
				System.out.println ( "CLIENT : OPERATION FINISHED." ) ;
			}
			catch ( IOException e ) 
			{
				dataPicker.generationNotification ( "Server connection loosed.\nTry to resume it\nIt will take some time." );
				try 
				{
					resumeConnectionConnect () ;
					connectionDownManagement () ;
				} 
				catch ( IOException e1 ) 
				{
					dataPicker.generationNotification ( "Can not try to resume the connection.\nSorry but there is nothing to do !\nThe program will be closed" );
					technicallyOn = false ;
				}
			}
		}
	}
	
}
