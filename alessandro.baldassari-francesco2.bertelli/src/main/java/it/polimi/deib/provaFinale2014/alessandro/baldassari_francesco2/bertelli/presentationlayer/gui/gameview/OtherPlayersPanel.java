package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import java.util.HashMap;
import java.util.Map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player.PlayerState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;

public class OtherPlayersPanel extends FrameworkedWithGridBagLayoutPanel 
{

	public OtherPlayersPanel ()
	{}
	
	@Override
	protected void createComponents () 
	{
		
	}

	@Override
	protected void manageLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void injectComponents() {
		// TODO Auto-generated method stub
		
	}

}

class OtherPlayersModel 
{

	private Map < String , PlayerState > otherPlayerNames ;

	public OtherPlayersModel () 
	{
		otherPlayerNames = new HashMap < String , PlayerState > () ;
	}

	public void addPlayer ( String name ) 
	{
		otherPlayerNames.put ( name , PlayerState.PLAYING ) ;
	}
	
	public void suspendPlayer ( String name ) 
	{
		otherPlayerNames.put ( name , PlayerState.SUSPENDED ) ;
	}
	
	public void resumePlayer ( String name ) 
	{
		otherPlayerNames.put ( name , PlayerState.PLAYING ) ;
	}
	
	public void removePlayer ( String name ) 
	{
		otherPlayerNames.put ( name , PlayerState.PLAYING ) ;
	}
	
	public Iterable < String > getNames () 
	{
		return otherPlayerNames.keySet () ;
	}
	
	public PlayerState getState ( String name ) 
	{
		return otherPlayerNames.get ( name ) ;
	}
	
}
