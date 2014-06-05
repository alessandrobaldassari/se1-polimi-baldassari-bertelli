package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.io.IOException;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

/***/
public class NetworkCommunicantPlayer extends Player 
{

	/**
	 * The ClientHandler object this Player object will use to obtain data when the System will
	 * query each Player during the Game. 
	 */
	private transient ClientHandler clientHandler ;
	
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
	public void chooseCardsEligibleForSellingImpl () 
	{
		Collection < SellableCard > arrived ;
		try 
		{
			arrived = CollectionsUtilities.newCollectionFromIterable ( clientHandler.chooseCardsEligibleForSelling ( getSellableCards () ) );
			for ( SellableCard s : arrived )
				if ( getSellableCards ().contains ( s ) )
				{
					getSellableCards ().remove ( s ) ;
					getSellableCards ().add ( s ) ;
				}
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
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
	public Sheperd chooseSheperdForATurnImpl () 
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
	public SellableCard chooseCardToBuyImpl ( Iterable<SellableCard > src ) 
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

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor getColorForSheperd ( Iterable < NamedColor > availableColors ) 
	{
		NamedColor res = null; 
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

	/**
	 * AS THE SUPER'S ONE. 
	 */
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

	/***/
	@Override
	public Road chooseInitialRegionForASheperd ( Iterable < Road > availableRoads ) 
	{
		return null ;
	}

}
