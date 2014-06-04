package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
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
	public String onNameRequest () ;
	
	/***/
	public void onNameRequestAck ( boolean isOk , String notes ) ;
	
	/***/
	public void onNotifyMatchStart () ;
	
	/***/
	public NamedColor onSheperdColorRequest ( Iterable < NamedColor > availableColors ) ;
	
	/***/
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > playersSheperd ) ;
	
	/***/
	public void onMatchWillNotStartNotification ( String msg ) ;
	
	/***/
	public void generationNotification ( String msg ) ;
	
	/***/
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > sellableCards ) ;
	
	/***/
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > acquirables ) ;
	
	/***/
	public GameMove onDoMove ( MoveFactory f , GameMap m ) ;
	
}
