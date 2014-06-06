package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.SheperdColorRequestView.SheperdColorRequestViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
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

public class SheperdColorRequestView extends JDialog 
{

	private SheperdColorRequestViewPanel view ;
	
	public SheperdColorRequestView ( SheperdColorRequestViewObserver observer , Iterable < NamedColor > colors ) 
	{ 
		super ( ( Frame ) null , "JSheepland - Scelta colore pastore" , true ) ;
		GridBagLayout g ;
		Insets insets ;
		view = new SheperdColorRequestViewPanel ( colors ) ;
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
	
	interface SheperdColorRequestViewObserver extends Observer
	{
		
		public void onColorChoosed ( NamedColor selectedColor ) ;
				
	}
	
}

class SheperdColorRequestViewPanel extends ObservableFrameworkedWithGridBagLayoutPanel < SheperdColorRequestViewObserver >
{

	private List < NamedColor > colors ;
	
	private NamedColor selected ;
	
	private JLabel titleLabel ;
	
	private List < JPanel > colorPanels ;

	private List < JRadioButton > selectors ;
	
	private ButtonGroup buttonGroup ;
	
	private JButton okButton ;
	
	private Insets insets ;
	
	SheperdColorRequestViewPanel ( Iterable < NamedColor > in ) 
	{
		createComponents () ;
		manageLayout () ;
		bindListeners () ;
		injectComponents () ;
	}
	
	public void setColors ( Iterable < NamedColor > in ) 
	{
		colors = CollectionsUtilities.newListFromIterable ( in ) ;
		selected = null ;
		for ( NamedColor n : colors )
		{
			colorPanels.add ( new JPanel () ) ;
			selectors.add ( new JRadioButton () ) ;
		}
	}
	
	@Override
	protected void createComponents () 
	{
		titleLabel = new JLabel () ;
		colorPanels = new LinkedList < JPanel > () ;
		selectors = new LinkedList < JRadioButton > () ;
		buttonGroup = new ButtonGroup () ;
		okButton = new JButton () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		int i ;
		for ( i = 0 ; i < colors.size() ; i ++ )
		{
			layoutComponent ( colorPanels.get ( i ) , i , 1 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
			layoutComponent ( selectors.get ( i ) , i , 2 , 1 / colors.size() , 0.7 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		}
		for ( i = 0 ; i < colors.size() ; i ++ )
		{
			colorPanels.get(i).setBackground ( colors.get ( i ) ) ;
			buttonGroup.add ( selectors.get( i ) );
			selectors.get(i).setText ( colors.get ( i ).getName () );
			for ( JPanel p : colorPanels )
				add ( p ) ;
			for ( JRadioButton r : selectors )
				add ( r ) ;
		}
		getLayout ().getConstraints ( titleLabel ).gridwidth = colors.size() ;
		getLayout ().getConstraints ( okButton ).gridwidth = colors.size () ;
	}

	@Override
	protected void manageLayout () 
	{
		layoutComponent ( titleLabel , 0 , 0 , 1 , 0.1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		layoutComponent ( okButton , 0 , 3 , 1 , 0.1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		titleLabel.setText ( "Scegli il colore per il tuo pastore." ) ;
		
	}

	@Override
	protected void bindListeners () 
	{
		Action okAction ;
		okAction = new OkAction () ;
		okButton.setAction ( okAction ) ;
	}

	@Override
	protected void injectComponents () 
	{
		add ( titleLabel ) ;
		add ( okButton ) ;
		
	}
	
	private class OkAction extends AbstractAction 
	{

		public OkAction () 
		{
			super ( "OK" ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			if ( selected != null )
				try 
				{
					SheperdColorRequestViewPanel.this.notifyObservers ( "onColorChoosed" , selected );
				}
				catch (MethodInvocationException e1) 
				{
					e1.printStackTrace();
				}
			else
				JOptionPane.showMessageDialog ( SheperdColorRequestViewPanel.this , "Devi prima selezionare un colore!" , "JSheepland" , JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}

