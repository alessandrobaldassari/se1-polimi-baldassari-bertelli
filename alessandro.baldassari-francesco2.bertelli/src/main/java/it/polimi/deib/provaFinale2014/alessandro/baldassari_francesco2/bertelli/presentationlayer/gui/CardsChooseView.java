package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

/**
 * This component allows a User to select some Cards between a List. 
 */
public class CardsChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < CardsChooseView.CardsChooseViewObserver >
{
	
	/**
	 * The generic InputView component. 
	 */
	private InputView view ;
	
	/**
	 * The panel that effectively shows and allow to select some Cards.
	 */
	private CardChooseViewPanel cardsPanel ;
	
	/**
	 * @param observer an Observer for this View. 
	 */
	public CardsChooseView ( CardsChooseViewObserver observer ) 
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
		cardsPanel = new CardChooseViewPanel () ;		
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
		view.setTitle ( PresentationMessages.CHOOSE_COLOR_FOR_SHEPERD_MESSAGE ) ;
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
		view.setKoAction ( new Couple < Boolean , Runnable > ( true , new KoAction () ) );		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( view ) ;
		view.setContentsPanel ( cardsPanel ) ;		
	}
	
	/**
	 * 
	 */
	public void setCards ( Iterable < Card > cards , boolean moreThanOneCard ) 
	{
		cardsPanel.setCards ( cards , moreThanOneCard ) ;
	}
	
	/**
	 * This interface defines the methods that a CardsChooseViewObserver can listen to. 
	 */
	public interface CardsChooseViewObserver extends Observer
	{
		
		/**
		 * Called when the User has finished the choosing process and confirm his selection.
		 * 
		 * @param selectedCards the Cards the User has choosen.
		 */
		public void onCardChoosed ( Iterable < Card > selectedCards ) ;
		
		/**
		 * Called when the User does not want to make any selection. 
		 */
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
			res = cardsPanel.getSelectedCards () ;
			if ( res != null )
				try 
				{
					notifyObservers ( "onCardChoosed" , res );
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
				notifyObservers ( "onDoNotWantChooseAnyCard" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}	
		}		
		
	}
	
}

/**
 * The effectively View that shows a list of Cards and allow the User to select some of them. 
 */
class CardChooseViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/**
	 * A list containing the Cards among which the User can choose. 
	 */
	private List < Card > availableCards ;
	
	/**
	 * A list containing the Cards the user selected. 
	 */
	private List < Card > selectedCards ;
	
	/**
	 * The panels that will show a graphic representation of the Cards. 
	 */
	private List < JPanel > imagePanels ;

	/**
	 * The buttons that allows to select / unselect Cards. 
	 */
	private List < JToggleButton > selectors ;
	
	/**
	 * A component that allows to select just one card.
	 */
	private ButtonGroup buttonGroup ;
	
	/***/
	public CardChooseViewPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		selectedCards = new LinkedList < Card > () ;
		imagePanels = new LinkedList < JPanel > () ;
		selectors = new LinkedList < JToggleButton > () ;
		buttonGroup = new ButtonGroup () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () {}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () {}

	/***/
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
	
	/**
	 * Getter method for the selectedCards property.
	 * 
	 * @return the Cards the User selected.
	 */
	public Iterable < Card > getSelectedCards () 
	{
		return selectedCards ;
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		private int associatedIndex ;
		
		/***/
		public SelectorListener ( int associatedIndex )
		{
			this.associatedIndex = associatedIndex ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
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

