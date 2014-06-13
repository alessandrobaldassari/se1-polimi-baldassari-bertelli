package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;

/***/
public abstract class ObservableFrameworkedWithGridBagLayoutDialog < T extends Observer > extends JDialog implements Observable < T >
{
	
	/***/
	private ObservableFrameworkedWithGridBagLayoutPanel < T > contentPane ;
	
	/***/
	public ObservableFrameworkedWithGridBagLayoutDialog ( Frame parent , String title , boolean modal ) 
	{
		super ( parent , title , modal ) ;
		System.out.println ( "ObservableFrameworkedWithGridBagLayoutDialog : SUPER_CONSTRUCTOR_EXECUTED" ) ;
		contentPane = new DefaultObservableFrameworkedWithGridBagLayoutPanel < T > () ;
		setContentPane ( contentPane ) ;
		System.out.println ( "ObservableFrameworkedWithGridBagLayoutDialog : CONSTRUCTOR_EXECUTED" ) ;
		createComponents();
		manageLayout();
		bindListeners();
		injectComponents();
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public GridBagLayout getLayout () 
	{
		GridBagLayout res ;
		if ( contentPane != null )
			res = contentPane.getLayout () ;
		else 
			res = new GridBagLayout () ;
		return res ;
	}
	
	/***/
	@Override
	public Component add ( Component c )
	{
		contentPane.add ( c ) ;
		return c ;
	}
	
	protected void layoutComponent ( JComponent c , int col , int row , double weightx , double weighty , int numRow , int numCol , int ipadx , int ipady , int fill , int anchor , Insets insets ) 
	{
		contentPane.layoutComponent(c, col, row, weightx, weighty, numRow, numCol, ipadx, ipady, fill, anchor, insets); 
	}
	
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void addObserver ( T newObserver ) 
	{
		contentPane.addObserver ( newObserver ) ; 
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void removeObserver ( T oldObserver ) 
	{
		contentPane.removeObserver ( oldObserver ) ;
	}
	
	/***/
	protected void notifyObservers ( String methodName , Object ... args ) throws MethodInvocationException 
	{
		contentPane.notifyObservers ( methodName , args ) ;
	}
	
	protected abstract void createComponents () ;
	
	protected abstract void manageLayout () ;
	
	protected abstract void bindListeners () ;
	
	protected abstract void injectComponents () ;
	
	/***/
	private class DefaultObservableFrameworkedWithGridBagLayoutPanel < T extends Observer > extends ObservableFrameworkedWithGridBagLayoutPanel < T > 
	{

		/***/
		public DefaultObservableFrameworkedWithGridBagLayoutPanel () 
		{
			super () ;
		}
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		protected void createComponents () {}

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		protected void manageLayout () {}

		/**
		 * AS THE SUPERS' ONE. 
		 */
		@Override
		protected void bindListeners () {}

		/**
		 * AS THE SUPER'S ONE.
		 */
		@Override
		protected void injectComponents () {}
		
	}
	
}
