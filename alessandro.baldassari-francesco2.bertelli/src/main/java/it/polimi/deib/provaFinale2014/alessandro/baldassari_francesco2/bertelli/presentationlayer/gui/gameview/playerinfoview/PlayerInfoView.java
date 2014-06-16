package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.WithBackgroundImagePanel;

/**
 * A View that shows to the User his Cards. 
 */ 
public class PlayerInfoView extends FrameworkedWithGridBagLayoutPanel implements PlayerInfoViewModelObserver
{
			
	private Map < RegionType , WithBackgroundImagePanel > cardsPanel ;
	
	private JLabel titleLabel ;
	
	private JLabel cardsLabel ;
	
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
		titleLabel = new JLabel () ;
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
		List < RegionType > regionsList ;
		Insets insets ;
		int i ;
		regionsList = CollectionsUtilities.newListFromIterable ( RegionType.allTheTypesExceptSheepsburg() ) ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( titleLabel , 0, 0 , 1 , 0.25 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( cardsLabel , 0, 1 , 1 , 0.25 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER , insets ) ;
		for ( i = 0 ; i < regionsList.size () ; i = i + 2 )
		{
			layoutComponent ( cardsPanel.get ( regionsList.get ( i ) ) , 0 , 2 + i , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( cardsPanel.get ( regionsList.get ( i ) ) , 1 , 2 + i , 1 , 0.3 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH, GridBagConstraints.CENTER , insets ) ;			
		}
		layoutComponent ( moneyLabel , 0, 5 , 1 , 0.25 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER , insets ) ;
		titleLabel.setText ( "La tua situazione di gioco" ) ;
		cardsLabel.setText ( "Le tue carte" ) ;
		moneyLabel.setText ( "I tuoi risparmi : " ) ;
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
	protected void injectComponents () 
	{
		add ( titleLabel ) ;
		add ( cardsLabel ) ;
		add ( moneyLabel ) ;
		for ( RegionType r : cardsPanel.keySet () )
			add ( cardsPanel.get ( r ) ) ;
	}

	@Override
	public void onMoneyReserveChanged ( int newAmount , boolean cause ) 
	{
		moneyLabel.setText ( "I tuoi risparmi : " + newAmount ) ;
	}

	@Override
	public void onCardsChanged ( RegionType cardType , int numberOfCardOfThisTypeNow ) 
	{
		( ( TitledBorder ) cardsPanel.get ( cardType ).getBorder () ).setTitle ( "Ne ho " + numberOfCardOfThisTypeNow ) ;
	}

}

