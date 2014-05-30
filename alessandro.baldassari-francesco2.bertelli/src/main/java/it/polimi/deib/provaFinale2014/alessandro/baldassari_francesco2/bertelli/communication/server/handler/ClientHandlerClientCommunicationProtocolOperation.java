package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

/**
 * Enums containing all the possible net operations supported by this System. 
 */
public enum ClientHandlerClientCommunicationProtocolOperation 
{

	NAME_REQUESTING_REQUEST ,
	
	NAME_REQUESTING_RESPONSE,
	
	SHEPERD_COLOR_REQUESTING_REQUEST,
	
	SHEPERD_COLOR_REQUESTING_RESPONSE,
	
	MATCH_WILL_NOT_START_NOTIFICATION,
	
	GENERIC_NOTIFICATION_NOTIFICATION,
	
	CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST,
	
	CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_RESPONSE ,
	
	CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_REQUEST ,
	
	CHOOSE_SHEPERD_FOR_A_TURN_REQUESTING_RESPONSE ,
	
	CHOOSE_CARDS_TO_BUY_REQUESTING_REQUEST ,
	
	CHOOSE_CARDS_TO_BUY_REQUESTING_RESPONSE ,

	DO_MOVE_REQUESTING_REQUEST ,
	
	DO_MOVE_REQUESTING_RESPONSE
	
}
