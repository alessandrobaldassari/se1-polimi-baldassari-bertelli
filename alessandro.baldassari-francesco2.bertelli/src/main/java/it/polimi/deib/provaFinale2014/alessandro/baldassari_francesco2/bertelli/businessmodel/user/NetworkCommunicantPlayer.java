package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.awt.Color;
import java.io.IOException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;

public class NetworkCommunicantPlayer extends Player 
{

	private ClientHandler clientHandler ;
	
	public NetworkCommunicantPlayer(String name, ClientHandler clientHandler ) 
	{
		super(name);
		if(clientHandler != null)
			this.clientHandler = clientHandler;
		else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void chooseCardsEligibleForSelling() {
		clientHandler.chooseCardsEligibleForSelling ( getSellableCards () );		
	}

	@Override
	public GameMove doMove(MoveFactory moveFactory, GameMap gameMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheperd chooseSheperdForATurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellableCard chooseCardToBuy(Iterable<SellableCard> src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColorForSheperd(Iterable<Color> availableColors) {
		Color res = null; 
		try {
			res = clientHandler.requestSheperdColor(availableColors);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public void genericNotification(String message) {
		
	}

}
