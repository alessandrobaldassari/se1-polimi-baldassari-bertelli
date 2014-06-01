package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.LoginView.LoginViewObserver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObservableWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginView extends JDialog 
{

	private JPanel dataPanel ;
	
	public LoginView () 
	{
		super ( ( Frame ) null , "JSheepland - Login" , true ) ;
		GridBagLayout g ;
		Insets insets ;
		dataPanel = new DataPanel () ;
		g = new GridBagLayout () ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		setLayout ( g ) ;
		setDefaultCloseOperation ( DISPOSE_ON_CLOSE ) ;
		GraphicsUtilities.setComponentLayoutProperties ( dataPanel , g , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		add ( dataPanel ) ;
	}
	
	interface LoginViewObserver extends Observer
	{
		
		public void onEnter () ;
		
		public void onExit () ;
		
	}
	
	public static void main ( String [] args ) 
	{
		LoginView d = new LoginView () ;
		d.show();
	}
	
}

/***/
class DataPanel extends ObservableWithGridBagLayoutPanel < LoginViewObserver >
{
	
	/***/
	private JLabel textLabel ;
	
	/***/
	private JTextField nameField ;
	
	/***/
	private JButton enterButton ;
	
	/***/
	private JButton exitButton ;
	
	/***/
	public DataPanel () 
	{
		super () ;
		createCocmponents () ;
		manageLayout () ;
		bindListeners () ;
		injectComponents () ;
	}
	
	/***/
	private void createCocmponents () 
	{
		textLabel = new JLabel () ;
		nameField = new JTextField () ;
		enterButton = new JButton () ;
		exitButton = new JButton () ;
	}
	
	/***/
	private void manageLayout () 
	{
		Insets insets ; 
		insets = new Insets ( 5 , 5 , 5 , 5 ) ;
		layoutComponent ( textLabel , 0 , 0 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( nameField , 0 , 1 , 0 , 0 , 1 , 2 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( enterButton , 0 , 2 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		layoutComponent ( exitButton , 1 , 2 , 0.5 , 0 , 1 , 1 , 5 , 5 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets ) ;
		textLabel.setText ( "Prego, inserisci il nome con cui vuoi giocare a JSheepland" ) ;
		textLabel.setHorizontalTextPosition ( SwingConstants.CENTER ) ;
	}
	
	/***/
	private void bindListeners () 
	{
		Action okAction ;
		Action exitAction ;
		okAction = new OkAction ( "ENTRA" ) ;
		exitAction = new ExitAction ( "ESCI" )  ;
		enterButton.setAction ( okAction ) ;
		exitButton.setAction ( exitAction ) ;
	}
	
	/***/
	private void injectComponents () 
	{
		add ( textLabel ) ;
		add ( nameField ) ;
		add ( enterButton ) ;
		add ( exitButton ) ;
	}
	
	/***/
	private class OkAction extends AbstractAction 
	{

		public OkAction ( String frontEndText )  
		{
			super ( frontEndText ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				DataPanel.this.notifyObservers ( "onEnter" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
		
	}
	
	private class ExitAction extends AbstractAction 
	{

		public ExitAction ( String frontEndText ) 
		{
			super ( frontEndText ) ;
		}
		
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			try 
			{
				DataPanel.this.notifyObservers ( "onExit" ) ;
			} 
			catch ( MethodInvocationException e1 ) 
			{
				e1.printStackTrace();
			}
		}
				
	}
	
	
}