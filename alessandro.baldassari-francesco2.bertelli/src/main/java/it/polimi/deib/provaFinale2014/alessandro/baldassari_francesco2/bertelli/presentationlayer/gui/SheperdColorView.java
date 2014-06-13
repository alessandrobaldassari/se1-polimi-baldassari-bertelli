package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * This class allows the User to select one color for one of his Sheperds. 
 */
public class SheperdColorView extends ObservableFrameworkedWithGridBagLayoutDialog < SheperdColorRequestViewObserver >
{
	
	/**
	 * An input view to contains the data. 
	 */
	private InputView view ;
	
	/**
	 * The effective data-content panel. 
	 */
	private ColorsListPanel colorsPanel ;
	
	/**
	 * @param observer an Observer for this View. 
	 */
	public SheperdColorView ( SheperdColorRequestViewObserver observer ) 
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
		colorsPanel = new ColorsListPanel () ;		
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
		view.setShowKo(false); 
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
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( view ) ;
		view.setContentsPanel ( colorsPanel ) ;
	}

	/**
	 * Set the colors among which the User can choose.
	 * 
	 * @param colors the list of colors the User can choose in.
	 */
	public void setColors ( Iterable < NamedColor > colors ) 
	{
		colorsPanel.setColors ( colors ) ;
	}
	
	/**
	 * This interface defines the events a SheperdColorRequestViewObserver can be notified about.
	 */
	public interface SheperdColorRequestViewObserver extends Observer
	{
		
		/**
		 * Called when a color is choosed and the User confirm that he has decided the value. 
		 * 
		 * @param selectedColor the color the User choosed.
		 */
		public void onColorChoosed ( NamedColor selectedColor ) ;
		
		/**
		 * Undo events 
		 */
		public void onDoNotWantChooseColor () ;
		
	}
	
	/**
	 * Class that manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
		
		/***/
		private static final String NAME_OF_THE_METHOD_TO_CALL = "onColorChoosed" ;

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			NamedColor res ;
			res = colorsPanel.getSelectedColor();
			if ( res != null )
				try 
				{
					notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , res );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( null , "Devi prima selezionare un colore!" , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE);
		}
				
	}
	
	/**
	 * Class that manages the ko action  
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
				notifyObservers ( "onDoNotWantChooseColor" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}	
		}		
		
	}

}

/**
 * The Panel that effectively contains colors. 
 */
class ColorsListPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/**
	 * The color the user has selected. 
	 */
	private NamedColor selected ;
	
	/**
	 * Panels to represent the Colors to the User. 
	 */
	private List < JPanel > colorPanels ;

	/**
	 * Buttons to allow the User to select a Color. 
	 */
	private List < JRadioButton > selectors ;
	
	/**
	 * Needed to ensure just one color is selected. 
	 */
	private ButtonGroup buttonGroup ;
	
	/***/
	public ColorsListPanel () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		selected = null ;
		colorPanels = new LinkedList < JPanel > () ;
		selectors = new LinkedList < JRadioButton > () ;
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

	/**
	 * Fill this component with data, allowing it to effectively show something useful to the User.
	 * 
	 * @param in the colors the User can choose betweeen.
	 */
	public void setColors ( Iterable < NamedColor > in ) 
	{
		List < NamedColor > colors ;
		Insets insets ;
		int i ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		colors = CollectionsUtilities.newListFromIterable ( in ) ;
		for ( NamedColor n : colors )
		{
			colorPanels.add ( new JPanel () ) ;
			selectors.add ( new JRadioButton () ) ;
		}
		for ( i = 0 ; i < colors.size() ; i ++ )
		{
			layoutComponent ( colorPanels.get ( i ) , i , 1 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors.get ( i ) , i , 2 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			colorPanels.get(i).setBackground ( colors.get ( i ) ) ;
			selectors.get ( i ).addItemListener ( new SelectorListener ( colors.get ( i ) ) ) ;
			selectors.get(i).setText ( colors.get ( i ).getName () ) ; 
		}
		for ( JPanel p : colorPanels )
			add ( p ) ;
		for ( JRadioButton r : selectors )
			add ( r ) ;
		for ( JRadioButton r : selectors )
			buttonGroup.add ( r ) ;
	}
	
	/***/
	public NamedColor getSelectedColor () 
	{
		return selected ;
	}
	
	/***/
	private class SelectorListener implements ItemListener 
	{

		/***/
		private NamedColor associatedColor ;
		
		/***/
		public SelectorListener ( NamedColor associatedColor )
		{
			this.associatedColor = associatedColor ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			selected = associatedColor ;
		}	
		
	}
	
}

