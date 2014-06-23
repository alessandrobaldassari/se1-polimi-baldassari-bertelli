package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.WithBackgroundImagePanel;

/**
 * A View that shows to the User his Cards. 
 */ 
public class PlayerInfoView extends FrameworkedWithGridBagLayoutPanel implements PlayerInfoViewModelObserver
{
			
	/***/
	private Map < RegionType , WithBackgroundImagePanel > cardsPanel ;
		
	/***/
	private JLabel cardsLabel ;
	
	/***/
	private JLabel moneyLabel ;
	
	/***/
	public PlayerInfoView () 
	{
		super () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		WithBackgroundImagePanel panel ;
		cardsPanel = new HashMap < RegionType , WithBackgroundImagePanel > () ;
		cardsLabel = new JLabel () ;
		moneyLabel = new JLabel () ;
		for ( RegionType r : RegionType.allTheTypesExceptSheepsburg() )
		{
			panel = new WithBackgroundImagePanel () ;
			panel.setVisible ( true ) ;
			panel.setBorder ( BorderFactory.createTitledBorder ( "Ne ho 0" ) ) ;
			panel.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCardImage ( r ) ) ;
			cardsPanel.put ( r , panel ) ;
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout() 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( cardsLabel , 0, 1 , 1 , 0.05 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( cardsPanel.get ( RegionType.CULTIVABLE ) , 0 , 2 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( cardsPanel.get ( RegionType.DESERT ) , 1 , 2 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		layoutComponent ( cardsPanel.get ( RegionType.FOREST ) , 0 , 3 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		layoutComponent ( cardsPanel.get ( RegionType.HILL ) , 1 , 3 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		layoutComponent ( cardsPanel.get ( RegionType.LACUSTRINE ) , 0 , 4 , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		layoutComponent ( cardsPanel.get ( RegionType.MOUNTAIN ) , 1 ,4, 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		layoutComponent ( moneyLabel , 0, 5 , 1 , 0.05 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER , insets ) ;
		cardsLabel.setText ( "Le tue carte" ) ;
		moneyLabel.setText ( "I tuoi risparmi : " ) ;
		setBorder ( new TitledBorder ( "La tua situazione" ) ) ;
		setOpaque ( false ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		Logger.getGlobal().log ( Level.INFO , "Player info view - bind listeners" );
	}

	/**
	 * AS THE SUPERS' ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( cardsLabel ) ;
		add ( moneyLabel ) ;
		for ( RegionType r : cardsPanel.keySet () )
			add ( cardsPanel.get ( r ) ) ;
	}
	
	@Override
	public void onCardsChanged ( RegionType cardType , Integer numberOfCardOfThisTypeNow ) 
	{
		( ( TitledBorder ) cardsPanel.get ( cardType ).getBorder () ).setTitle ( "Ne ho " + numberOfCardOfThisTypeNow ) ;
		repaint () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameChanged ( String name ) 
	{
		( ( TitledBorder ) getBorder () ).setTitle ( name.concat ( ", la tua situazione" ) );
		repaint () ;
	}
	
	@Override
	public void onMoneyReserveChanged ( Integer newAmount , Boolean cause ) 
	{
		moneyLabel.setText ( "I tuoi risparmi : " + newAmount ) ;
		repaint () ;
	}
	
}

