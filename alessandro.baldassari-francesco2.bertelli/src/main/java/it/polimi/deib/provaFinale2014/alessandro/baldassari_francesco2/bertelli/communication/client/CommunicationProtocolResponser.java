package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;
import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

/**
 * Abstraction interface that contains all the method a class has to implement to answer requests for 
 * the Server. 
 */
public interface CommunicationProtocolResponser 
{

	/**
	 * Implementers has to return from here with the name choosen for a Match. 
	 */
	public String onNameRequest () throws IOException ;
	
	/**
	 * Notification by the server to communicate if the choosen name is ok or not. 
	 */
	public void onNameRequestAck ( boolean isOk , String notes ) ;
	
	/**
	 * Notification by the server that the Match is about to start. 
	 */
	public void onNotifyMatchStart () ;
	
	/**
	 * Notification by the server that the Match will not start. 
	 */
	public void onMatchWillNotStartNotification ( String msg ) ;
	
	/**
	 * Request by the Server that the Player has to choose a Color for his Sheperd. 
	 */
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) throws IOException ;
	
	/***/
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException ; 
	
	/***/
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > playersSheperd ) throws IOException ;
	
	/***/
	public MoveSelection onDoMove ( MoveSelector f , GameMap m ) throws IOException ;
	
	/***/
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > sellableCards ) throws IOException ;
	
	/***/
	public Iterable < SellableCard > onChoseCardToBuy ( Iterable < SellableCard > acquirables , Integer playerMoney ) throws IOException ;
	
	/***/
	public void onGameConclusionNotification ( String cause ) throws IOException ;
	
	/**
	 * Generic notification by the Server. 
	 */
	public void generationNotification ( String msg ) ;
	
	/***/
	public void onGUIConnectorOnNotification ( Serializable guiConnector ) ;
	
}
