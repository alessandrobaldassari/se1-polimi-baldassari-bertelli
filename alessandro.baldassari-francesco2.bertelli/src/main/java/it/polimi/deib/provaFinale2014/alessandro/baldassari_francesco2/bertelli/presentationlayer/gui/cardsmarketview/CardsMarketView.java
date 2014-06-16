package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.cardsmarketview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview.SheperdColorChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.WithBackgroundImagePanel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This component allows a User to select some Cards between a List. 
 */
public class CardsMarketView extends ObservableFrameworkedWithGridBagLayoutDialog < CardsMarketViewObserver >
{
	
	/**
	 * The generic InputView component. 
	 */
	private InputView view ;
	
	/**
	 * The panel that effectively shows and allow to select some Cards.
	 */
	private CardChooseViewPanel cardsPanel ;
	
	/***/
	private int maxAmount ;
	
	/**
	 * @param observer an Observer for this View. 
	 */
	public CardsMarketView ( CardsMarketViewObserver observer , int maxAmount ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		this.maxAmount = maxAmount ;
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
		view.setTitle ( "Scegli le carte !" ) ;
		view.setShowKo(false); 
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		setResizable ( false ) ;
		setAlwaysOnTop ( true ) ;	
		setUndecorated(true);
 		setSize ( GraphicsUtilities.getVGAResolution () ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
		view.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getBackgroundImage() );
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		view.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ;
		view.setKoAction ( new Couple < Boolean , Runnable > ( false , new KoAction () ) );		
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
	
	/***/
	public void setCards ( Iterable < SellableCard > availableCards , boolean allowEdit ) 
	{
		cardsPanel.setCards ( availableCards , allowEdit ) ;
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
			Iterable < SellableCard > res ;
			int sum ;
			res = cardsPanel.getSelectedData () ;
			if ( maxAmount >= 0)
			{	
				sum = 0 ;
				for ( SellableCard in : res )
					try 
					{
						sum = sum + in.getSellingPrice();
					}
					catch (NotSellableException e) 
					{
						e.printStackTrace();
					}
					catch (SellingPriceNotSetException e) 
					{
						e.printStackTrace();
					}
					if ( maxAmount >= sum )
						try 
						{
							notifyObservers ( "onCardChoosed" , res );
						}
						catch (MethodInvocationException e1) 
						{
							e1.printStackTrace();
						}
					else
						view.setErrors ( "Non hai abbastanza soldi per questo !" ) ; 
				}
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

	/***/
	public static Iterable < SellableCard > showDialog ( Iterable < SellableCard > availableCards , boolean allowEdit , int maxAmount ) 
	{
		CardsMarketView cardsChooseView ;
		AtomicReference < Iterable < SellableCard > > cards ;
		cards = new AtomicReference < Iterable < SellableCard > > ( null ) ;
		cardsChooseView = new CardsMarketView ( new DefaultCardsMarketViewObserver ( cards ) , maxAmount ) ;
		cardsChooseView.setCards ( availableCards , allowEdit ) ;
		GraphicsUtilities.showUnshowWindow ( cardsChooseView , false , true ) ;
		synchronized ( cards ) 
		{
			while ( cards.get() == null )
				try 
				{
					cards.wait () ;
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( cardsChooseView , false , false ) ;
		return cards.get() ;
	}
	
	public static void main ( String [] args ) 
	{
		ArrayList < NamedColor > a = new ArrayList < NamedColor > () ;
		a.add(new NamedColor ( 0 , 0 , 0 , "BLACK" ) ) ;
		a.add ( new NamedColor ( 255 , 0 , 0 , "RED" ) ) ;
		a.add ( new NamedColor ( 0 , 255 , 0 , "GREEN") ) ;
		NamedColor n ;
		n = SheperdColorChooseView.showDialog( a );
		System.out.println ( n ) ;
	}
	
	
}

/**
 * The effectively View that shows a list of Cards and allow the User to select some of them. 
 */
class CardChooseViewPanel extends FrameworkedWithGridBagLayoutPanel 
{
	
	/**
	 * A list containing the Cards the user selected. 
	 */
	private List < SellableCard > selectedCards ;
		
	/**
	 * The panels that will show a graphic representation of the Cards. 
	 */
	private List < WithBackgroundImagePanel > imagePanels ;

	/**
	 * The buttons that allows to select / unselect Cards. 
	 */
	private List < JCheckBox > selectors ;
	
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
		selectedCards = new LinkedList < SellableCard > () ;
		imagePanels = new LinkedList < WithBackgroundImagePanel > () ;
		selectors = new LinkedList < JCheckBox > () ;
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
	public Iterable < SellableCard > getSelectedData () 
	{
		return selectedCards ;
	}
	
	/***/
	public void setCards ( Iterable < SellableCard > in , boolean allowEdit ) 
	{
		Insets insets ;
		WithBackgroundImagePanel imagePanel ;
		JCheckBox checkBox ;
		JSpinner spinner ;
		SpinnerModel spinnerModel ;
		SelectorListener selectorListener ;
		ChangeListener changeListener ;
		int i ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		spinnerModel = new SpinnerNumberModel ( 1 , 1 , 4 , 1 ) ; 
		i = 0 ;
		for ( SellableCard n : in )
		{
			imagePanel = new WithBackgroundImagePanel () ;
			checkBox = new JCheckBox () ;
			layoutComponent ( imagePanel , i , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( checkBox , i , 2 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
			imagePanel.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCardImage ( n.getRegionType() ) );
			imagePanels.add ( imagePanel ) ;
			selectors.add ( checkBox ) ;
			selectorListener = new SelectorListener ( n ) ; 
			selectors.get ( i ).addItemListener ( selectorListener ) ;
			if ( allowEdit )
			{
				spinner = new JSpinner () ;
				spinner.setModel ( spinnerModel ) ;
				layoutComponent ( checkBox , i , 2 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;				
				changeListener = new SpinnerListener ( n ) ;
				selectorListener.setAssociatedValueEnter ( spinner ) ;
				spinner.addChangeListener ( changeListener ) ;
				spinner.setVisible(false); 
				spinner.setBorder ( new TitledBorder ( "Prezzo richiesto per questa carta : " ) );
				checkBox.setBorder ( new  TitledBorder ( "Da vendere" ) );
				add ( spinner ) ;
			} 
			else
			{
				checkBox.setBorder ( new TitledBorder ( "Da comprare : " ) ) ; 
				try 
				{
					checkBox.setText ( "Prezzo : " + n.getSellingPrice() ) ;
				}
				catch (NotSellableException e)
				{
					e.printStackTrace();
				}
				catch (SellingPriceNotSetException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/***/
	private class SpinnerListener implements ChangeListener 
	{

		/***/
		private SellableCard myItem ;
		
		/***/
		public SpinnerListener ( SellableCard myItem ) 
		{
			this.myItem = myItem ;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) 
		{
			Integer val ;
			val = (Integer) ( ( JSpinner ) e.getSource () ).getValue () ;
			for ( SellableCard in : selectedCards )
				if ( in.equals ( myItem ) )
				{
					in.setSellingPrice ( val ) ;
					break ;
				}
		}
		
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		private SellableCard myItem ;
				
		private JSpinner associatedValueEnter ;
		
		/***/
		public SelectorListener ( SellableCard myCard )
		{
			this.myItem = myItem ;
		}
		
		public void setAssociatedValueEnter ( JSpinner associatedValueEnter )
		{
			this.associatedValueEnter = associatedValueEnter ;			
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			if ( ( ( JToggleButton ) e.getSource() ).isSelected () )
			{
				myItem.setSellable(true); 
				myItem.setSellingPrice ( ( Integer ) associatedValueEnter.getValue() ) ;
				selectedCards.add ( myItem ) ;
				associatedValueEnter.setVisible(true);
			}
			else
			{
				for ( SellableCard in : selectedCards )
					if ( in.equals ( myItem ) )
					{
						in.setSellable(false); 
						selectedCards.remove(in);
						break ;
					}
				associatedValueEnter.setVisible(false);
			}
		}	
		
	}
	
}

