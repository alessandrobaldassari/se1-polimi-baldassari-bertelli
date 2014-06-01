package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;

import java.awt.Color;

/**
 * Abstraction interface that contains all the method a class has to implement to answer requests for 
 * the Server. 
 */
public interface CommunicationProtocolResponser 
{

	/***/
	public String onNameRequest () ;
	
	/***/
	public void onNameRequestAck ( boolean isOk , String notes ) ;
	
	/***/
	public void onNotifyMatchStart () ;
	
	/***/
	public Color onSheperdColorRequest ( Iterable < Color > availableColors ) ;
	
	/***/
	public void onMatchWillNotStartNotification ( String msg ) ;
	
	/***/
	public void generationNotification ( String msg ) ;
	
	/***/
	public void onChooseCardsEligibleForSelling () ;
	
	/***/
	public void onChooseSheperdForATurn () ;
	
	/***/
	public void onChoseCardToBuy () ;
	
	/***/
	public GameMove onDoMove () ;
	
}
