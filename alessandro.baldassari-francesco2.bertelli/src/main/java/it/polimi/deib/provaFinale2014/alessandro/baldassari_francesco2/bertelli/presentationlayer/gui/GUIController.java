package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorRequestView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/***/
public class GUIController extends ViewPresenter implements LoginViewObserver , SheperdColorRequestViewObserver
{

	/***/
	private static final String APP_NAME = "JSheepland" ;
	
	/***/
	private static final String SERVER_CONNECTION_MESSAGE = "Connessione al server, prego attendere ..." ;
	
	/***/
	private static final String WAITING_FOR_OTHER_PLAYERS_MESSAGE = "Attendendo gli altri giocatori..." ;
	
	/***/
	private static final String NAME_ACCEPTED_MESSAGE = "Complimenti!\nIl tuo nome è stato accettato dal Server di JSheeplan.\nOra aspetta pochi istanti l'arrivo degli altri giocatori per iniziare a giocare" ;
	
	/***/
	private static final String NAME_REJECTED_MESSAGE = "Siamo spiacenti, ma il nome che hai proposto è già in uso nel contesto di JSheepland in questo momento.\nPrego, prova con un altro nome!" ;
	
	/***/
	private static final String MATCH_STARTING_MESSAGE = "Tutto è pronto!\nGli avversari sono arrivati!\nPreparati alla partita!" ;
	
	/***/
	private WaitingView waitingView ;
	
	/***/
	private LoginView loginView ;
	
	/***/
	private SheperdColorRequestView sheperdColorRequestView ;
	
	/***/
	private GameView gameView ;
	
	/***/
	private AtomicReference < String > name ;
	
	/***/
	private AtomicReference < NamedColor > color ;
	
	/***/
	public GUIController () 
	{
		super () ;
		Runnable guiElementsCreator ;
		name = new AtomicReference < String > () ;
		name.set ( null ) ;
		color = new AtomicReference < NamedColor > () ;
		color.set ( null ) ;
		guiElementsCreator = new GUIElementsCreatorRunnable () ;
		SwingUtilities.invokeLater ( guiElementsCreator ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void startApp () 
	{
		SwingUtilities.invokeLater ( 
					new Runnable () 
					{ 
						public void run () 
						{
							while ( waitingView == null ) ;
							waitingView.setText ( SERVER_CONNECTION_MESSAGE ) ;
							waitingView.setVisible ( true ) ;
						} 
					} );
	}
	
	/***/
	@Override
	public String onNameRequest () 
	{
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						while ( waitingView == null && loginView == null ) ;
						waitingView.setVisible ( false ) ;
						loginView.setVisible ( true ) ;
					} 
				} 
									);
		while ( name.get () == null ) ;
		return name.get() ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameRequestAck ( boolean isOk , String notes ) 
	{
		final Runnable r ;
		if ( isOk )
		{
			r = new Runnable () { 
					public void run () 
					{
						JOptionPane.showMessageDialog ( loginView , NAME_ACCEPTED_MESSAGE , "JSheepland" , JOptionPane.INFORMATION_MESSAGE ) ;
						loginView.setVisible ( false ) ;
						waitingView.setText ( WAITING_FOR_OTHER_PLAYERS_MESSAGE ) ;
						waitingView.setVisible ( true ) ;
						loginView.dispose () ;
					} 
								} ;
		}
		else
		{
			r = new Runnable () { 
				public void run () 
				{
					loginView.prepareView () ;
					JOptionPane.showMessageDialog ( loginView , NAME_REJECTED_MESSAGE , "JSheepland" , JOptionPane.ERROR_MESSAGE ) ;
				} 
							} ;
		}
		SwingUtilities.invokeLater ( r ) ;
	}

	@Override
	public void onNotifyMatchStart () 
	{
		SwingUtilities.invokeLater ( new Runnable () 
						{ 
							public void run () 
							{
								JOptionPane.showMessageDialog ( waitingView , MATCH_STARTING_MESSAGE , APP_NAME , JOptionPane.INFORMATION_MESSAGE ) ;
								waitingView.setVisible( false ) ;
								gameView.setVisible ( true ) ;
							} 
						} 
								   ) ;
	}
	
	@Override
	public void onMatchWillNotStartNotification ( final String msg ) 
	{
		SwingUtilities.invokeLater ( new Runnable () 
		{ 
			public void run () 
			{
				JOptionPane.showMessageDialog ( waitingView , msg , APP_NAME , JOptionPane.ERROR_MESSAGE) ;
				waitingView.setVisible ( false ) ;
				loginView.dispose () ;
				waitingView.dispose () ;
				try 
				{
					terminateClient () ;
				} 
				catch (WrongStateMethodCallException e) 
				{
					e.printStackTrace();
					throw new RuntimeException ( e ) ;
				}
			} 
		} 
				   ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor onSheperdColorRequest ( final Iterable < NamedColor > availableColors ) 
	{
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						sheperdColorRequestView = new SheperdColorRequestView ( GUIController.this , availableColors ) ;
						gameView.setVisible(false);
						sheperdColorRequestView.setVisible ( true ) ;
					} 
				} 
									);
		while ( color.get () == null ) ;
		return color.get() ;
	}

	@Override
	public Iterable < SellableCard > onChooseCardsEligibleForSelling ( Iterable < SellableCard > playerCards ) 
	{
		return null ;
	}

	@Override
	public Sheperd onChooseSheperdForATurn ( Iterable < Sheperd > sheperds )
	{
		return null ;
	}

	@Override
	public SellableCard onChoseCardToBuy ( Iterable < SellableCard > acquirables ) 
	{
		return null ;
		
	}

	@Override
	public GameMove onDoMove ( MoveFactory f , GameMap m ) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generationNotification(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	/***/
	private class GUIElementsCreatorRunnable implements Runnable 
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			waitingView = new WaitingView () ;
			loginView = new LoginView ( GUIController.this ) ;
			sheperdColorRequestView = null ;
			gameView = new GameView () ;
			waitingView.setSize ( GraphicsUtilities.getVGAResolution () ) ;
			loginView.setSize ( GraphicsUtilities.getVGAResolution () ) ;
			gameView.setExtendedState ( Frame.MAXIMIZED_BOTH );
		}
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onNameEntered ( String enteredName ) 
	{
		name.set ( enteredName ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onExit () 
	{
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						JOptionPane.showMessageDialog ( null , "Grazie di avere utilizzato JSheepland\nArrivederci" , "JSheepland" , JOptionPane.INFORMATION_MESSAGE );
						loginView.setVisible ( false ) ;
						try 
						{
							terminateClient () ;
						} 
						catch (WrongStateMethodCallException e) 
						{
							e.printStackTrace();
							throw new RuntimeException ( e ) ;
						}
					} 
				} 
									);
	}

	@Override
	public void onColorChoosed ( NamedColor selectedColor ) 
	{
		color.set ( selectedColor ) ;
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						
					} 
				} 
									);
	}

	@Override
	public Road chooseInitRoadForSheperd(Iterable<Road> availableRoads)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}		

}
