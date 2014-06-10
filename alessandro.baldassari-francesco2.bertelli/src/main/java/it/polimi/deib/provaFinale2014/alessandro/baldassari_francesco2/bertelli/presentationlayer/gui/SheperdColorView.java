package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableFrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SheperdColorView extends JDialog 
{

	/***/
	private SheperdColorRequestViewPanel view ;
	
	/***/
	public SheperdColorView ( SheperdColorRequestViewObserver observer ) 
	{ 
		super ( ( Frame ) null , "JSheepland - Scelta colore pastore" , true ) ;
		GridBagLayout g ;
		Insets insets ;
		view = new SheperdColorRequestViewPanel () ;
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		GraphicsUtilities.setComponentLayoutProperties ( view , g , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		view.addObserver ( observer ) ;
		setLayout ( g ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		add ( view ) ;
		setResizable ( false ) ;
		setAlwaysOnTop ( true ) ;
	}
	
	/***/
	public void setColors ( Iterable < NamedColor > colors ) 
	{
		view.setColors ( colors ) ;
	}
	
	/***/
	public interface SheperdColorRequestViewObserver extends Observer
	{
		
		/***/
		public void onColorChoosed ( NamedColor selectedColor ) ;
		
		/***/
		public void onDoNotWantChooseColor () ;
		
	}
	
}

class SheperdColorRequestViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < SheperdColorRequestViewObserver >
{
	
	private JLabel titleLabel ;
	
	private ColorsListPanel colorsPanel ;
	
	private JButton okButton ;
	
	private JButton koButton ;
	
	private Insets insets ;
	
	protected SheperdColorRequestViewPanel () 
	{
		createComponents () ;
		manageLayout () ;
		bindListeners () ;
		injectComponents () ;
	}
	
	public void setColors ( Iterable < NamedColor > n ) 
	{
		colorsPanel.setColors ( n ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		titleLabel = new JLabel () ;
		colorsPanel = new ColorsListPanel () ;
		okButton = new JButton () ;
		koButton = new JButton () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
	}

	/***/
	@Override
	protected void manageLayout () 
	{
		layoutComponent ( titleLabel , 0 , 0 , 1 , 0.125 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		layoutComponent ( colorsPanel , 0 , 1 , 1 , 0.75 , 1 , 2 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets );		
		layoutComponent ( okButton , 0 , 2 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		layoutComponent ( koButton , 1 , 2 , 1 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		titleLabel.setText ( "Scegli il colore per il tuo pastore." ) ;
		
	}

	/***/
	@Override
	protected void bindListeners () 
	{
		Action okAction ;
		Action koAction ;
		okAction = new OkAction ( "Ho scelto" ) ;
		koAction = new KoAction ( "Annulla ( terminerai la tua partita ! )" ) ;
		okButton.setAction ( okAction ) ;
		koButton.setAction ( koAction ) ;
	}

	@Override
	protected void injectComponents () 
	{
		add ( titleLabel ) ;
		add ( colorsPanel ) ;
		add ( okButton ) ;
		add ( koButton ) ;
	}
	
	private class OkAction extends AbstractAction 
	{

		public OkAction ( String txt ) 
		{
			super ( txt ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			NamedColor res ;
			res = colorsPanel.getSelectedColor();
			if ( res != null )
				try 
				{
					System.out.println ( "viaksfnawòf" + res ) ;
					notifyObservers ( "onColorChoosed" , res );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( SheperdColorRequestViewPanel.this , "Devi prima selezionare un colore!" , "JSheepland" , JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/***/
	private class KoAction extends AbstractAction 
	{

		/***/
		public KoAction ( String frontEndText ) 
		{
			super ( frontEndText ) ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void actionPerformed ( ActionEvent e ) 
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
			for ( JPanel p : colorPanels )
				add ( p ) ;
			for ( JRadioButton r : selectors )
				add ( r ) ;
		}
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

