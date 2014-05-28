package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
import java.io.IOException;

public class RMIClientHandler implements ClientHandler , RMIClientBroker 
{
	
	@Override
	public String requestName () throws IOException 
	{
		return null;
	}

	@Override
	public void notifyMatchWillNotStart(String message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color requestSheperdColor(Iterable<Color> availableColors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void chooseCardsEligibleForSelling(
			Iterable<SellableCard> sellablecards) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sheperd chooseSheperdForATurn(Iterable<Sheperd> sheperdsOfThePlayer)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellableCard chooseCardToBuy(Iterable<SellableCard> src)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameMove doMove(MoveFactory gameFactory, GameMap gameMap)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genericNotification(String message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() throws IOException {
		// TODO Auto-generated method stub
		
	}}
