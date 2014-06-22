package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.PlayerObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.NotificationContainer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.NotificationPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.GameMapViewInputMode;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.GameMapViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.gamemapview.GameMapViewPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview.PlayerInfoView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview.playerinfoview.PlayerInfoViewModel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;

/**
 * This class is a JFrame wrapper for the GameView. 
 */
public class GameView extends JFrame implements NotificationContainer 
{

	/**
	 * The GameViewPanel object that will render the the GameView. 
	 */
	private GameViewPanel gameViewPanel ;

	/***/
	public GameView () 
	{
		super () ;
		GridBagLayout g ;
		Insets insets ;
		gameViewPanel = new GameViewPanel () ; 
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( gameViewPanel , g , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( EXIT_ON_CLOSE ) ;
		setLayout ( g ) ;
		setResizable ( true ) ;
		add ( gameViewPanel ) ;
		setExtendedState ( Frame.MAXIMIZED_BOTH ) ;
	}

	public void setPlayerName ( String playerName ) 
	{
		( ( PlayerInfoViewModel ) gameViewPanel.getPlayerObserver() ).setName ( playerName ) ;
	}
	
	/***/
	@Override
	public void addNotification ( String notif ) 
	{
		gameViewPanel.addNotification ( notif ) ;
	} 

	/***/
	public GameMapObserver getGameMapObserver () 
	{
		return gameViewPanel.getGameMapObserver();
	}
	
	/***/
	public PlayerObserver getPlayerObserver () 
	{
		return gameViewPanel.getPlayerObserver () ;
	}
	
	/***/
	public void setGameMapViewObserver ( GameMapViewObserver observer ) 
	{
		gameViewPanel.setGameMapViewObserver ( observer ) ;
	}
	
	/***/
	public void setInputMode ( GameMapViewInputMode mode , Iterable < ? extends Identifiable > uidOfSelectableElements ) 
	{
		gameViewPanel.setInputMode ( mode , uidOfSelectableElements ) ;
	}

	/***/
	public GameMapViewInputMode getInputMode () 
	{
		return gameViewPanel.getInputMode () ;
	}

	public static void main ( String [] args ) 
	{
		GameView g ;
		g = new GameView () ;
		g.setExtendedState ( GameView.MAXIMIZED_BOTH );
		g.setInputMode ( GameMapViewInputMode.REGIONS , null ) ;
		g.setVisible ( true ) ;
	}
	
}

/**
 * The true View for the Game 
 */
class GameViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	// ATTRIBUTES
	
	private PlayerInfoViewModel model ;
	
	/**
	 * A PlayersCardView to manage the Cards a User owns. 
	 */
	private PlayerInfoView playersCardPanel ;
	
	/**
	 * A MapView to render the Map and let the User interact with it. 
	 */
	private GameMapViewPanel mapPanel ;
	
	private JScrollPane notificationAreaScrollPane ;
	
	/**
	 * A panel to show some notifications to the User. 
	 */
	private NotificationPanel notificationArea ;
	
	/**
	 * 
	 */
	private Image backgroundImage ;
	
	// METHODS
	
	/***/
	protected GameViewPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		model = new PlayerInfoViewModel () ;
		playersCardPanel = new PlayerInfoView () ;
		mapPanel = new GameMapViewPanel () ;
		notificationAreaScrollPane = new JScrollPane () ;
		notificationArea = new NotificationPanel();
		backgroundImage = SheeplandClientApp.getInstance().getImagesHolder().getBackgroundImage(true);
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( notificationAreaScrollPane , 0 , 0 , 0.25 , 0.375 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( playersCardPanel , 0 , 1 , 0.25 , 0.625 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( mapPanel , 1 , 0 , 0.75 , 1 , 2 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setOpaque ( false ) ;
		notificationArea.setOpaque(false); 
		notificationAreaScrollPane.setViewportView(notificationArea); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		model.addObserver(playersCardPanel); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( playersCardPanel ) ;
		add ( mapPanel ) ;
		add ( notificationAreaScrollPane ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void paintComponent ( Graphics g ) 
	{
		super.paintComponent ( g ) ;
		g.drawImage ( backgroundImage , 0 , 0 , getWidth() , getHeight() , this ) ;
	}
	
	/**
	 * Show some notification to the User.
	 */
	public void addNotification ( String msg ) 
	{
		notificationArea.addNotification(msg); 
	}

	/***/
	protected void setInputMode ( GameMapViewInputMode mode , Iterable < ? extends Identifiable > uidOfSelectableElements ) 
	{
		mapPanel.setCurrentInputMode ( mode , uidOfSelectableElements ) ;
	}
	
	/***/
	public GameMapViewInputMode getInputMode () 
	{
		return mapPanel.getCurrentInputMode () ;
	}
	
	/**
	 * Access the GameMapObserver associated to this GameMapView.
	 * 
	 * @return a GameMapObserver associated to this GameMapView.
	 */
	protected GameMapObserver getGameMapObserver () 
	{
		return mapPanel ;
	}
	
	/***/
	protected PlayerObserver getPlayerObserver () 
	{
		return model;
	}
	
	/***/
	public void setGameMapViewObserver ( GameMapViewObserver observer )
	{
		mapPanel.addObserver ( observer ) ;
	}
	
}

