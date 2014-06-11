package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.MultioptionChooseView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.WithReflectionObservableSupport;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SheperdColorView extends JDialog implements Observable < SheperdColorRequestViewObserver >
{

	/***/
	private WithReflectionObservableSupport < SheperdColorRequestViewObserver > support ;
	
	/***/
	private MultioptionChooseView view ;
	
	/***/
	private ColorsListPanel colorsPanel ;
	
	/***/
	public SheperdColorView ( SheperdColorRequestViewObserver observer ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		GridBagLayout g ;
		Insets insets ;
		support = new WithReflectionObservableSupport < SheperdColorRequestViewObserver > () ;
		view = new MultioptionChooseView () ;
		colorsPanel = new ColorsListPanel () ;
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
	
	/***/
	public void setColors ( Iterable < NamedColor > colors ) 
	{
		colorsPanel.setColors ( colors ) ;
	}
	
	/***/
	public interface SheperdColorRequestViewObserver extends Observer
	{
		
		/***/
		public void onColorChoosed ( NamedColor selectedColor ) ;
		
		/***/
		public void onDoNotWantChooseColor () ;
		
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
			NamedColor res ;
			res = colorsPanel.getSelectedColor();
			if ( res != null )
				try 
				{
					support.notifyObservers ( "onColorChoosed" , res );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( null , "Devi prima selezionare un colore!" , PresentationMessages.APP_NAME , JOptionPane.ERROR_MESSAGE);
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
				support.notifyObservers ( "onDoNotWantChooseColor" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}	
		}		
		
	}

	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver(SheperdColorRequestViewObserver newObserver) 
	{
		support.addObserver ( newObserver ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */

	@Override
	public void removeObserver(SheperdColorRequestViewObserver oldObserver) 
	{
		support.removeObserver ( oldObserver ) ;
	}
	
}

/***/
class ColorsListPanel extends FrameworkedWithGridBagLayoutPanel 
{

	private List < NamedColor > colors ;
	
	private NamedColor selected ;
	
	private List < JPanel > colorPanels ;

	private List < JRadioButton > selectors ;
	
	private ButtonGroup buttonGroup ;
	
	public ColorsListPanel () 
	{
		super () ;
	}
	
	@Override
	protected void createComponents () 
	{
		selected = null ;
		colorPanels = new LinkedList < JPanel > () ;
		selectors = new LinkedList < JRadioButton > () ;
		buttonGroup = new ButtonGroup () ;
	}

	@Override
	protected void manageLayout () {}

	@Override
	protected void bindListeners () {}

	@Override
	protected void injectComponents () {}

	public void setColors ( Iterable < NamedColor > in ) 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		colors = CollectionsUtilities.newListFromIterable ( in ) ;
		selected = null ;
		for ( NamedColor n : colors )
		{
			colorPanels.add ( new JPanel () ) ;
			selectors.add ( new JRadioButton () ) ;
		}
		int i ;
		for ( i = 0 ; i < colors.size() ; i ++ )
		{
			layoutComponent ( colorPanels.get ( i ) , i , 1 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors.get ( i ) , i , 2 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		}
		for ( i = 0 ; i < colors.size() ; i ++ )
		{
			colorPanels.get(i).setBackground ( colors.get ( i ) ) ;
			selectors.get ( i ).addItemListener ( new SelectorListener ( i ) ) ;
			buttonGroup.add ( selectors.get( i ) );
			selectors.get(i).setText ( colors.get ( i ).getName () ) ;
		}
		for ( JPanel p : colorPanels )
			add ( p ) ;
		for ( JRadioButton r : selectors )
			add ( r ) ;
	}
	
	public NamedColor getSelectedColor () 
	{
		return selected ;
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
			selected = colors.get(associatedIndex);
		}	
		
	}
	
}

