package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.MultioptionChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

public class MoveChooseView extends JDialog implements Observable < SheperdColorRequestViewObserver >
{

	/***/
	private WithReflectionObservableSupport < SheperdColorRequestViewObserver > support ;
	
	/***/
	private MultioptionChooseView view ;
	
	/***/
	private MoveListPanel moveListPanel ;
	
	/***/
	public MoveChooseView ( SheperdColorRequestViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		GridBagLayout g ;
		Insets insets ;
		
		view = new MultioptionChooseView () ;
		moveListPanel = new MoveListPanel () ;
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		
		GraphicsUtilities.setComponentLayoutProperties ( view , g , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		
		view.setOkAction ( new Couple < Boolean , Runnable > ( true , new OkAction () ) ) ; 
		view.setKoAction ( new Couple < Boolean , Runnable > ( true , new KoAction () ) ) ;
		addObserver ( observer ) ;
	
		setLayout ( g ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setResizable ( false ) ;
		setAlwaysOnTop ( true ) ;

		view.setContentsPanel ( moveListPanel ) ;
		add ( view ) ;
	}
	
	@Override
	public void addObserver ( SheperdColorRequestViewObserver newObserver ) 
	{
		support.addObserver ( newObserver ) ;
	}

	@Override
	public void removeObserver(SheperdColorRequestViewObserver oldObserver) 
	{
		support.removeObserver ( oldObserver ) ;
	}
	
	/***/
	private class OkAction implements Runnable 
	{
		
		@Override
		public void run () 
		{
			GameMoveType res ;
			res = moveListPanel.getSelectedMove () ;
			if ( res != null )
			try 
			{
				support.notifyObservers ( "onMoveChoosed" , moveListPanel.getSelectedMove () );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
			else
				JOptionPane.showMessageDialog ( null , PresentationMessages.INVALID_CHOOSE_MESSAGE , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE );
		}
		
	}
	
	/***/
	private class KoAction implements Runnable 
	{
		
		@Override
		public void run () 
		{
			try 
			{
				support.notifyObservers ( "onDoNotWantChooseMove" );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***/
	public interface MoveChooseViewObserver extends Observer
	{
		
		/***/
		public void onMoveChoosed ( GameMoveType move ) ;
		
		/***/
		public void onDoNotWantChooseMove () ;
		
	}
	
}

/***/
class MoveListPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	private GameMoveType selected ;
	
	private JPanel [] imagePanels ;

	private JRadioButton [] selectors ;
	
	private ButtonGroup buttonGroup ;
	
	public MoveListPanel () 
	{
		super () ;
	}
	
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
