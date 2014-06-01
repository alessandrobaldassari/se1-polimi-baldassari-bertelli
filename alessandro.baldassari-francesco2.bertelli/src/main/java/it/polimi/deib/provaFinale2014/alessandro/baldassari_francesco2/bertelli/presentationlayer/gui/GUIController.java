package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Terminable;

public class GUIController extends ViewPresenter implements CommunicationProtocolResponser , LoginViewObserver
{

	/***/
	private JDialog initView ;
	
	/***/
	private JDialog loginView ;
	
	/***/
	public GUIController ( Terminable client ) 
	{
		super ( client ) ;
		Runnable guiElementsCreator ;
		guiElementsCreator = new GUIElementsCreatorRunnable () ;
		SwingUtilities.invokeLater ( guiElementsCreator ) ;
	}
	
	/***/
	public void startApp () 
	{
		SwingUtilities.invokeLater ( 
				new Runnable () 
				{ 
					public void run () 
					{
						while ( initView == null ) ;
						initView.setVisible ( true ) ;
					} 
				} 
									);
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
						while ( initView == null && loginView == null ) ;
						initView.setVisible ( false ) ;
						loginView.setVisible ( true ) ;
					} 
				} 
									);
		return null;
	}

	/***/
	@Override
	public Color onSheperdColorRequest ( Iterable<Color> availableColors ) 
	{
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

	/***/
	private class GUIElementsCreatorRunnable implements Runnable 
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			initView = new InitView () ;
			loginView = new LoginView ( GUIController.this ) ;
		}
		
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void onEnter () 
	{
		
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
					} 
				} 
									);
	}		

}
