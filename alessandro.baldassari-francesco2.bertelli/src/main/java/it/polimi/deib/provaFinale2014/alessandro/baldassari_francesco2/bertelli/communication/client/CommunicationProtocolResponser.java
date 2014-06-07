package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

/**
 * Abstraction interface that contains all the method a class has to implement to answer requests for 
 * the Server. 
 */
public interface CommunicationProtocolResponser 
{

	/***/
	public String onNameRequest () throws IOException ;
	
	/***/
	public void onNameRequestAck ( boolean isOk , String notes ) ;
	
	/***/
	public void onNotifyMatchStart () ;
	
	/***/
	public void onMatchWillNotStartNotification ( String msg ) ;
	
	/***/
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) throws IOException ;
	
	/***/
	public Road chooseInitRoadForSheperd ( Iterable < Road > availableRoads ) throws IOException ; 
	
	/***/
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > playersSheperd ) throws IOException ;
	
	/***/
	public GameMove onDoMove ( MoveFactory f , GameMap m ) throws IOException ;
	
	/***/
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > sellableCards ) throws IOException ;
	
	/***/
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > acquirables ) throws IOException ;
	
	/***/
	public void generationNotification ( String msg ) ;
	
}
