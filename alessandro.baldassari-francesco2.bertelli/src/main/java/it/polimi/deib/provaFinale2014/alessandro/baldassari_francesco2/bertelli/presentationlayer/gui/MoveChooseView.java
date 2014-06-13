package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

public class MoveChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < SheperdColorRequestViewObserver >
{

	/***/
	private InputView view ;
	
	/***/
	private MoveListPanel moveListPanel ;
	
	/**
	 * @param observer 
	 */
	public MoveChooseView ( SheperdColorRequestViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		addObserver ( observer ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		view = new InputView () ;
		moveListPanel = new MoveListPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( view , getLayout () , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setResizable ( false ) ;
		setAlwaysOnTop ( true ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		view.setOkAction ( new Couple < Boolean , Runnable > ( true , new OkAction () ) ) ; 
		view.setKoAction ( new Couple < Boolean , Runnable > ( true , new KoAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		view.setContentsPanel ( moveListPanel ) ;
		add ( view ) ;
	}
	
	/**
	 * This class manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			GameMoveType res ;
			res = moveListPanel.getSelectedMove () ;
			if ( res != null )
			try 
			{
				notifyObservers ( "onMoveChoosed" , moveListPanel.getSelectedMove () );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
			else
				JOptionPane.showMessageDialog ( null , PresentationMessages.INVALID_CHOOSE_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE );
		}
		
	}
	
	/**
	 * This class manages the ko action. 
	 */
	private class KoAction implements Runnable 
	{
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			try 
			{
				notifyObservers ( "onDoNotWantChooseMove" );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * This class defines the events a MoveChooseViewObserver can listen to.
	 */
	public interface MoveChooseViewObserver extends Observer
	{
		
		/**
		 * Called when the User choosed a move and wants to confirm it. 
		 */
		public void onMoveChoosed ( GameMoveType move ) ;
		
		/**
		 * Undo action. 
		 */
		public void onDoNotWantChooseMove () ;
		
	}

	
}

/***/
class MoveListPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/***/
	private GameMoveType selected ;
	
	/***/
	private JPanel [] imagePanels ;

	/***/
	private JRadioButton [] selectors ;
		
	/***/
	private ButtonGroup buttonGroup ;	
	
	/***/
	public MoveListPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		int i ;
		selected = null ;
		imagePanels = new JPanel [ GameMoveType.values().length ] ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
			imagePanels [ i ] = new JPanel () ;
		selectors = new JRadioButton [ imagePanels.length ] ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
			selectors [ i ] = new JRadioButton () ;
		buttonGroup = new ButtonGroup () ;
		
	}

	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		int i ;
		for ( i = 0 ; i < imagePanels.length ; i ++ )
		{
			layoutComponent ( imagePanels [ i ] , i , 1 , 1 / imagePanels.length , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors [ i ] , i , 2 , 1 / imagePanels.length , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			selectors [ i ].setText ( GameMoveType.values () [ i ].name () ) ;
		}
	}

	@Override
	protected void bindListeners () 
	{
		int i ;
		for ( i = 0 ; i < selectors.length ; i ++ )
			selectors [ i ].addItemListener ( new SelectorListener ( GameMoveType.values () [ i ] ) ) ;
	}

	@Override
	protected void injectComponents () 
	{
		for ( JPanel p : imagePanels )
			add ( p ) ;
		for ( JRadioButton r : selectors )
			add ( r ) ;
		for ( JRadioButton r : selectors )
			buttonGroup.add ( r ) ;
	}

	/***/
	public GameMoveType getSelectedMove () 
	{
		return selected ;
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		/***/
		private GameMoveType gameMove ;
		
		/***/
		public SelectorListener ( GameMoveType gameMove )
		{
			this.gameMove = gameMove ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			selected = gameMove ;
		}	
		
	}
	
}
