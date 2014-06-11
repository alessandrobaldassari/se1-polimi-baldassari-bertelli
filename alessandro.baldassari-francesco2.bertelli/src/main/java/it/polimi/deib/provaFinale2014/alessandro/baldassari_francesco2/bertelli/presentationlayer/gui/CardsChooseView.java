package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.MultioptionChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

public class CardsChooseView extends JDialog implements Observable < CardsChooseView.CardsChooseViewObserver >
{

	/***/
	private WithReflectionObservableSupport < CardsChooseViewObserver > support ;
	
	/***/
	private MultioptionChooseView view ;
	
	/***/
	private CardChooseViewPanel colorsPanel ;
	
	/***/
	public CardsChooseView ( CardsChooseViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		GridBagLayout g ;
		Insets insets ;
		support = new WithReflectionObservableSupport < CardsChooseViewObserver > () ;
		view = new MultioptionChooseView () ;
		colorsPanel = new CardChooseViewPanel () ;
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		
		GraphicsUtilities.setComponentLayoutProperties ( view , g , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		view.setTitle ( PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE ) ;
		setLayout ( g ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setResizable ( false ) ;
		setAlwaysOnTop ( true ) ;
		
		view.setOkAction ( new Couple < Boolean , Runnable > ( true , new OkAction () ) ) ;
		view.setKoAction ( new Couple < Boolean , Runnable > ( true , new KoAction () ) );
		addObserver ( observer ) ;
		
		add ( view ) ;
		view.setContentsPanel ( colorsPanel ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver ( CardsChooseViewObserver newObserver) 
	{
		support.addObserver ( newObserver ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */

	@Override
	public void removeObserver( CardsChooseViewObserver oldObserver) 
	{
		support.removeObserver ( oldObserver ) ;
	}
	
	/***/
	public void setCards ( Iterable < Card > cards ) {}
	
	/***/
	public interface CardsChooseViewObserver extends Observer
	{
		
		/***/
		public void onCardChoosed ( Card selectedCard ) ;
		
		/***/
		public void onDoNotWantChooseAnyCard () ;
		
	}
	
	/**
	 * Class that manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			Iterable < Card > res ;
			res = colorsPanel.getSelectedCard();
			if ( res != null )
				try 
				{
					support.notifyObservers ( "onCardChoosed" , res );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( null , "Devi prima selezionare una carta!" , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE);
		}
				
	}
	
	/***/
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
				support.notifyObservers ( "onDoNotWantChooseAnyCard" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}	
		}		
		
	}
	
}

/***/
class CardChooseViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	private List < Card > availableCards ;
	
	private List < Card > selectedCards ;
	
	private List < JPanel > imagePanels ;

	private List < JToggleButton > selectors ;
	
	private ButtonGroup buttonGroup ;
	
	public CardChooseViewPanel () 
	{
		super () ;
	}
	
	@Override
	protected void createComponents () 
	{
		selectedCards = new LinkedList < Card > () ;
		imagePanels = new LinkedList < JPanel > () ;
		selectors = new LinkedList < JToggleButton > () ;
		buttonGroup = new ButtonGroup () ;
	}

	@Override
	protected void manageLayout () {}

	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () {}

	public void setCards ( Iterable < Card > in , boolean multipleSelectionsAllowed ) 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		availableCards = CollectionsUtilities.newListFromIterable ( in ) ;
		for ( Card n : availableCards )
		{
			imagePanels.add ( new JPanel () ) ;
			if ( multipleSelectionsAllowed )
				selectors.add ( new JCheckBox () ) ;
			else
				selectors.add ( new JRadioButton () ) ;
		}
		int i ;
		for ( i = 0 ; i < availableCards.size() ; i ++ )
		{
			layoutComponent ( imagePanels.get ( i ) , i , 1 , 1 / availableCards.size() , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors.get ( i ) , i , 2 , 1 / availableCards.size() , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		}
		for ( i = 0 ; i < availableCards.size() ; i ++ )
		{
			selectors.get ( i ).addItemListener ( new SelectorListener ( i ) ) ;
			selectors.get(i).setText ( availableCards.get(i).getRegionType().toString() ) ;
			
		}
		for ( JPanel p : imagePanels )
			add ( p ) ;
		for ( JToggleButton t : selectors )
			add ( t ) ;
		if ( ! multipleSelectionsAllowed )
			for ( JToggleButton t : selectors )
				buttonGroup.add ( t ) ;
			
	}
	
	public Iterable < Card > getSelectedCard () 
	{
		return selectedCards ;
	}
	
	private class SelectorListener implements ItemListener 
	{

		private int associatedIndex ;
		
		public SelectorListener ( int associatedIndex )
		{
			this.associatedIndex = associatedIndex ;
		}
		
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			if ( ( ( JToggleButton ) e.getSource() ).isSelected () )
				selectedCards.add ( availableCards.get(associatedIndex) ) ;
			else
				selectedCards.remove ( availableCards.get ( associatedIndex ) ) ;
		}	
		
	}
	
}

