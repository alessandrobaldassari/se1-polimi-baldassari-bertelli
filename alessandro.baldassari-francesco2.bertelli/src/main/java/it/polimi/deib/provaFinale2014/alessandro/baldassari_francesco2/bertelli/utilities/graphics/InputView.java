package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

/**
 * A class that offers an infrastructure to show multioptions choose views.
 */
public class InputView extends FrameworkedWithGridBagLayoutPanel 
{

	/**
	 * An image to show as background 
	 */
	private Image backgroundImage ;
	
	/**
	 * The label that displays the intent of this View. 
	 */
	private JLabel titleLabel ;
	
	/**
	 * The panel containing the options a User can choose between. 
	 */
	private SimpleWithGridBagLayoutPanel contentsPanel ;
	
	/**
	 * The label that shows eventually present input errors. 
	 */
	private JLabel errorLabel ;
	
	/**
	 * The button to confirm the choose. 
	 */
	private JButton okButton ;
	
	/**
	 * The button to cancel the choose. 
	 */
	private JButton koButton ;
	
	/**
	 * The action to execute with the ok command.
	 * The boolean field is true if the job has to block the flows, false if asynchronous execution is ok. 
	 */
	private Couple < Boolean , Runnable > okAction ;
	
	/**
	 * The action to execute with the ko command.
	 * The boolean field is true if the job has to block the flows, false if asynchronous execution is ok. 
	 */
	private Couple < Boolean , Runnable > koAction ;

	/**
	 * Boolean variable that, if false, hide the ko button. 
	 */
	private boolean showKo ;
	
	/***/
	public InputView () 
	{
		super () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void paintComponent ( Graphics g )
	{
		super.paintComponent(g); 
		if ( backgroundImage != null )
			g.drawImage ( backgroundImage , 0 , 0 , getWidth() , getHeight() , this ) ;
	}
	
	/**
	 * Setter method for the backgroundImage property.
	 * 
	 * @param backgroundImage the value for the backgroundImage property.
	 */
	public void setBackgroundImage ( Image backgroundImage )
	{
		this.backgroundImage = backgroundImage ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		titleLabel = new JLabel () ;
		contentsPanel = new SimpleWithGridBagLayoutPanel() ;
		errorLabel = new JLabel () ;
		okButton = new JButton () ;
		koButton = new JButton () ;
		showKo = true ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( titleLabel ,    0 , 0 , 1   , 0.1 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		layoutComponent ( contentsPanel , 0 , 1 , 1   , 0.6 , 1 , 2 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets );		
		layoutComponent ( errorLabel ,    0 , 2 , 1   , 0.1 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );				
		layoutComponent ( okButton ,      0 , 3 , 0.5 , 0.1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		layoutComponent ( koButton ,      1 , 3 , 0.5 , 0.1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );		
		//errorLabel.setVisible(false); 
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		Action okAction ;
		Action koAction ;
		okAction = new OkAction ( "OK" ) ;
		koAction = new KoAction ( "ANNULLA" ) ;
		okButton.setAction ( okAction ) ;
		koButton.setAction ( koAction ) ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		add ( titleLabel ) ;
		add ( contentsPanel ) ;
		add ( errorLabel ) ;
		add ( okButton ) ;
		add ( koButton ) ;
	}
	
	/**
	 * Set the title of this view
	 * 
	 * @param title the title to set.
	 */
	public void setTitle ( String title ) 
	{
		titleLabel.setText ( title ) ;
		repaint () ;
	}
	
	/***/
	public void setErrors ( String errors ) 
	{
		errorLabel.setVisible(true); 
		errorLabel.setText ( errors ) ;
		repaint () ;
	}
	
	/**
	 * Set the title for the ok button.
	 * 
	 * @param okTitle the title for the ok button.
	 */
	public void setOkTitle ( String okTitle ) 
	{
		okButton.getAction().putValue ( Action.NAME , okTitle ) ;
	}
	
	/**
	 * Set the title for the ko button.
	 * 
	 * @param koTitle the title for the ko button.
	 */
	public void setKoTitle ( String koTitle ) 
	{
		koButton.getAction().putValue ( Action.NAME , koTitle ) ;		
	}
	
	/**
	 * Setter method for the okAction field.
	 * 
	 * @param okAction the value for the okAction field.
	 */
	public void setOkAction ( Couple < Boolean , Runnable > okAction ) 
	{
		this.okAction = okAction ;
	}
	
	/**
	 * Setter method for the koAction field.
	 * 
	 * @param koAction the value for the koAction field.
	 */
	public void setKoAction ( Couple < Boolean , Runnable > koAction )
	{
		this.koAction = koAction ;
	}
	
	/**
	 * Set the contentsPanel for this View.
	 * 
	 * @param p the panel that contains the options among those choose.
	 */
	public void setContentsPanel ( JPanel p ) 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0, 0 , 0 ) ;
		contentsPanel.layoutComponent ( p , 0 , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets )  ;
		contentsPanel.add ( p ) ;
		repaint () ;
	}
	
	/**
	 * Setter method for the showKo property.
	 * Adapts the layout as a consequence of the choice.
	 * 
	 * @param showKo the value for the showKo property. 
	 */
	public void setShowKo ( boolean showKo ) 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		this.showKo = showKo ;
		if ( showKo )
		{
			koButton.setVisible ( true ) ;
			layoutComponent ( okButton , 0 , 2 , 0.5 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
			layoutComponent ( koButton , 1 , 2 , 0.5 , 0.125 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );		
		}
		else
		{
			koButton.setVisible ( false ) ;
			layoutComponent ( okButton , 0 , 2 , 1 , 0.125 , 1 , 2 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets );
		}
	}
	
	/**
	 * Helper method to execute the actions associated with the buttons.
	 * 
	 * @param action a couple containing the data to execute.
	 */
	private void executeActions ( Couple < Boolean , Runnable > action ) 
	{
		if ( action != null )
		{
			if ( action.getFirstObject () )
				try 
				{
					SwingUtilities.invokeAndWait ( action.getSecondObject () );
				}
				catch (InvocationTargetException e) 
				{
					e.printStackTrace();
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
			else
				SwingUtilities.invokeLater ( action.getSecondObject () );
		}
	}
	
	/**
	 * This class manages the action associated with the confirm concept in this view.
	 */
	private class OkAction extends AbstractAction 
	{

		/***/
		public OkAction ( String txt ) 
		{
			super ( txt ) ;
		}
		
		/**
		 * AS THE SUPER' ONE. 
		 */
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			executeActions ( okAction ) ;
		}
		
	}
	
	/**
	 * This class manages the action associated with the undo action concept in this view.
	 */
	private class KoAction extends AbstractAction 
	{

		/***/
		public KoAction ( String txt ) 
		{
			super ( txt ) ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void actionPerformed ( ActionEvent e ) 
		{
			executeActions ( koAction ) ;
		}
				
	}

	/**
	 * Default implementation of the WithGridBagLayoutPanel class that makes the layoutComponent method public.
	 */
	public class SimpleWithGridBagLayoutPanel extends WithGridBagLayoutPanel 
	{
		
		/***/
		public SimpleWithGridBagLayoutPanel () 
		{
			super () ;
			setOpaque ( false ) ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void layoutComponent ( JComponent c , int col , int row , double weightx , double weighty , int numRow , int numCol , int ipadx , int ipady , int fill , int anchor , Insets insets ) 
		{
			super.layoutComponent(c, col, row, weightx, weighty, numRow, numCol, ipadx, ipady, fill, anchor, insets) ;
		}
		
	}
	
}
