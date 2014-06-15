package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JLabel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;

/**
 * A View that shows to the User his Cards. 
 */ 
class PlayersDataViewPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	private JLabel titleLabel ;
	
	/***/
	protected PlayersDataViewPanel () 
	{
		super () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout() 
	{
		setOpaque ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () {}

	/**
	 * AS THE SUPERS' ONE. 
	 */
	@Override
	protected void injectComponents () {}

}

class PlayersDataModel 
{

	private Map < RegionType , Integer > cards ;
	
	private int moneyReserve ;

	public PlayersDataModel () 
	{
		cards = new LinkedHashMap < RegionType , Integer > () ;
		moneyReserve = 0 ;
	}
	
	public void receiveMoney ( int amount ) 
	{
		moneyReserve += amount ;
	}
	
	public void pay ( int amount ) 
	{
		moneyReserve -= amount ;
	}
	
	public int getMoneyReserve () 
	{
		return moneyReserve ;
	}
	
	public void addCard ( RegionType type )
	{
		cards.put ( type , cards.get ( type ) + 1 ) ;
	}
	
	public Integer getNumberOfCards ( RegionType type ) 
	{
		return cards.get ( type ) ;
	}
	
}