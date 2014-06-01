package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.awt.Color;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

/***/
public class NetworkCommunicantPlayer extends Player 
{

	/**
	 * The ClientHandler object this Player object will use to obtain data when the System will
	 * query each Player during the Game. 
	 */
	private ClientHandler clientHandler ;
	
	/**
	 * @param name the name of this Player
	 * @param clientHandler the value for the ClientHandler property.
	 * @throws IllegalArgumentException if the clientHandler parameter is null.
	 */
	public NetworkCommunicantPlayer ( String name , ClientHandler clientHandler ) 
	{
		super ( name ) ;
		if ( clientHandler != null )
			this.clientHandler = clientHandler;
		else 
			throw new IllegalArgumentException();
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void chooseCardsEligibleForSelling () 
	{
		try 
		{
			clientHandler.chooseCardsEligibleForSelling ( getSellableCards () );
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}		
	}

	@Override
	public GameMove doMove ( MoveFactory moveFactory , GameMap gameMap ) 
	{
		GameMove res = null ;
		try 
		{
			res = clientHandler.doMove ( moveFactory , gameMap ) ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	@Override
	public Sheperd chooseSheperdForATurn () 
	{
		Sheperd res = null ;
		try {
			res = clientHandler.chooseSheperdForATurn ( getSheperds () ) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res ;
	}

	@Override
	public SellableCard chooseCardToBuy ( Iterable<SellableCard > src ) 
	{
		SellableCard res = null ;
		try 
		{
			res = clientHandler.chooseCardToBuy ( src ) ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	@Override
	public Color getColorForSheperd ( Iterable < Color > availableColors ) 
	{
		Color res = null; 
		try 
		{
			res = clientHandler.requestSheperdColor(availableColors);
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public void genericNotification ( String message ) 
	{
		try 
		{
			clientHandler.genericNotification ( message ) ;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
