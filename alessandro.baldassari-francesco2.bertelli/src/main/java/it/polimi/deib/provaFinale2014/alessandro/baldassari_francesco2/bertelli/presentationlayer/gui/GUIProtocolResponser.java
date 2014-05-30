package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;

public class GUIProtocolResponser implements CommunicationProtocolResponser {

	@Override
	public String onNameRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color onSheperdColorRequest(Iterable<Color> availableColors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMatchWillNotStartNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generationNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseCardsEligibleForSelling() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseSheperdForATurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChoseCardToBuy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameMove onDoMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
