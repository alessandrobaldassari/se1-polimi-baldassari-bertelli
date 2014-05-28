package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
import java.io.IOException;

/**
 * This interface is an abstraction that represents all the operations a ClientHandler has to do.
 * A ClientHandler is a server component whose target is to manage the connection between the server
 * itself and the client. 
 */
public interface ClientHandler 
{

	/**
	 * This method should call the managed client for his name.
	 * 
	 * @return the name the client returns
	 * @throws IOException if something goes wrong with the communication.
	 */
	public String requestName () throws IOException ;

	/**
	 * Sends a special message to the client, notifying him that the match that he was playing
	 * for start will not start
	 * 
	 * @param a message to explain the client the situation
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void notifyMatchWillNotStart (String message) throws IOException ;
	
	/**
	 * Ask the color to choose a color for one of his Sheperds.
	 * The color must be in the parameter list.
	 * 
	 * @param the list of colors where the client may choose the one he wants.
	 * @return the color the client chooses.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public Color requestSheperdColor( Iterable <Color> availableColors) throws IOException;
	
	/**
	 * Ask the client to say which of his cards are eligible for selling and at what price.
	 * 
	 * @param the client cards, those he has to set the selling properties.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void chooseCardsEligibleForSelling(Iterable <SellableCard> sellablecards) throws IOException;
	
	/**
	 * Ask the client to choose a Sheperd he will use for the current Game Turn.
	 * 
	 * @param sheperdsOfThePlayer the collection where the client has to choose the Sheperd he
	 *        wants to use.
	 * @return the choosen Sheperd
	 * @throws IOException if something goes wrong with the communication.
	 */
	public Sheperd chooseSheperdForATurn ( Iterable < Sheperd > sheperdsOfThePlayer ) throws IOException ;
	
	/**
	 * Ask the client which card between those contained in the src parameter he wants to buy.
	 * 
	 * @param src the cards where the client can choose
	 * @return the card the client wants to buy, null if he doesn't want to buy cards anymore.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public SellableCard chooseCardToBuy ( Iterable < SellableCard > src ) throws IOException ;
	
	/**
	 * Ask the client to choose which move he wants to do in the current Game turn.
	 * 
	 * @param gameFactory the object the client has to use to generate the Move he wants to do.
	 * @param gameMap the map of the Game, containing all the information the client needs to 
	 *        decide which move do.
	 * @return the move the client wants to do.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public GameMove doMove ( MoveFactory gameFactory , GameMap gameMap ) throws IOException ;
	
	/**
	 * This methods sends to the client a generic message.
	 * 
	 * @param message the message to send.
	 * @throws IOException if something goes wrong with the communication.
	 */
	public void genericNotification(String message) throws IOException;
	
	/**
	 * This method closes and frees all the resources held by this ClientHandler.
	 * 
	 * @throws IOException if something goes wrong with the operations performed.
	 */
	public void dispose () throws IOException ;
	
}
