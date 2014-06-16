package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client;

import static org.junit.Assert.*;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities.DummySocketServer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SocketClientTest {

	ProtocolResponser protocolResponser;
	SocketClient socketClient;
	DummySocketServer dummySocketServer;
	
	@Before
	public void setUp() throws Exception {
		protocolResponser = new ProtocolResponser();
		socketClient = new SocketClient(protocolResponser); 
		dummySocketServer = new DummySocketServer();
	}

	@Test
	public void directTechnicalConnect() {
		dummySocketServer.start();
		try {
			socketClient.directTechnicalConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
		
	}
	
	@Test
	public void read(){
		dummySocketServer.start();
		try {
			socketClient.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
		
	}
	
	@Test 
	public void write(){
		try {
			socketClient.write(Message.newInstance(GameProtocolMessage.UID_NOTIFICATION, (Iterable <Serializable>) new ArrayList<Serializable>()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}

}

class ProtocolResponser implements CommunicationProtocolResponser{

	@Override
	public String onNameRequest() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onNameRequestAck(boolean isOk, String notes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotifyMatchStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMatchWillNotStartNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NamedColor onSheperdColorRequest(Iterable<NamedColor> availableColors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Road chooseInitRoadForSheperd(Iterable<Road> availableRoads)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sheperd onChooseSheperdForATurn(Iterable<Sheperd> playersSheperd)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoveSelection onDoMove(MoveSelector f, GameMap m) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<SellableCard> onChooseCardsEligibleForSelling(
			Iterable<SellableCard> sellableCards) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable < SellableCard > onChoseCardToBuy(Iterable<SellableCard> acquirables , Integer i )
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generationNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGUIConnectorOnNotification(Serializable guiConnector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameConclusionNotification(String cause) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}