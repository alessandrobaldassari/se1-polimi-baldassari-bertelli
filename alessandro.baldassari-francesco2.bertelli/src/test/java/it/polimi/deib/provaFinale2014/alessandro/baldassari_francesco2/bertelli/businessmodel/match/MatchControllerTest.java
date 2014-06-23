package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.ConnectionLoosingServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching.MatchLauncherServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching.MatchStarter;

//grossi problemi ad instanziare i parametri che mi servono

public class MatchControllerTest 
{
	
	private MatchController matchController;
	private MatchStarter matchStarter;
	private List <GameMapObserver> mapObservers;
	private boolean correct;
	private PlayerObserver playerObserver;
	private MatchLauncherServer matchLauncherServer;
	private ConnectionLoosingServer connectionLoosingServer;
	
	@Test
	public void MatchController()
	{
		mapObservers = new ArrayList <GameMapObserver>();
		connectionLoosingServer = new ConnectionLoosingServer();
		matchLauncherServer = new MatchLauncherServer(connectionLoosingServer);
		matchStarter = null;
		correct = false;
		try
		{
			matchController = new MatchController(matchStarter, mapObservers);
		}
		catch (IllegalArgumentException e){
			correct = true;
			assertTrue(correct);
		}	
		correct = false;
		mapObservers = null;
		try{
		matchController = new MatchController(matchLauncherServer, mapObservers);
		}
		catch (IllegalArgumentException e)
		{
			correct = true;
			assertTrue(correct);
		}	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addPlayerObserver(){
		mapObservers = new ArrayList <GameMapObserver>();
		mapObservers.add(new DummyGameMapObserver());
		connectionLoosingServer = new ConnectionLoosingServer();
		matchLauncherServer = new MatchLauncherServer(connectionLoosingServer);
		matchController = new MatchController(matchLauncherServer, mapObservers);
		playerObserver = null ;
		matchController = new MatchController(matchLauncherServer, mapObservers);
		matchController.addPlayerObserver(playerObserver, 1);
		fail () ;
	}
	

	class DummyGameMapObserver implements GameMapObserver 
	{

		@Override
		public void onPositionableElementAdded(GameMapElementType whereType,
				Integer whereId, PositionableElementType whoType, Integer whoId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPositionableElementRemoved(GameMapElementType whereType,
				Integer whereId, PositionableElementType whoType, Integer whoId) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class DummyPlayerObserver implements PlayerObserver
	{

		@Override
		public void onCardAdded(RegionType cardType) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCardRemoved(RegionType cardType) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPay(Integer paymentAmount, Integer moneyYouHaveNow) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPayed(Integer paymentAmount, Integer moneyYouHaveNow) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
}
