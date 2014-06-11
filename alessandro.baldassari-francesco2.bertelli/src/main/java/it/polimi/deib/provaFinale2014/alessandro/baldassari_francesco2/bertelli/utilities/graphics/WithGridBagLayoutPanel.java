package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JPanel;

/***/
public abstract class WithGridBagLayoutPanel extends JPanel 
{
	
	/***/
	private boolean layoutSet ;
	
	/***/
	public WithGridBagLayoutPanel () 
	{
		super () ;
		layoutSet = false ;
		setLayout ( new GridBagLayout () ) ;
		layoutSet = true ;
	}
	
	/***/
	@Override
	public void setLayout ( LayoutManager layoutManager ) 
	{
		if ( layoutSet == false )
		{
			if ( layoutManager != null && layoutManager instanceof GridBagLayout )
				super.setLayout ( layoutManager ) ;
		}
		else
			try 
			{
				throw new WriteOncePropertyAlreadSetException ( "LAYOUT" ) ;
			} 
			catch ( WriteOncePropertyAlreadSetException e ) 
			{
				e.printStackTrace();
				throw new IllegalArgumentException ( e ) ;
			}
	}
	
	/***/
	@Override
	public GridBagLayout getLayout () 
	{
		return ( GridBagLayout ) super.getLayout () ;
	}

	/***/
	protected void layoutComponent ( JComponent c , int col , int row , double weightx , double weighty , int numRow , int numCol , int ipadx , int ipady , int fill , int anchor , Insets insets ) 
	{
		GridBagConstraints gridBagConstraints ;
		gridBagConstraints = new GridBagConstraints () ;
		gridBagConstraints.gridx = col ;
		gridBagConstraints.gridy = row ;
		gridBagConstraints.weightx = weightx ;
		gridBagConstraints.weighty = weighty ;
		gridBagConstraints.gridwidth = numCol ;
		gridBagConstraints.gridheight = numRow ;
		gridBagConstraints.ipadx = ipadx ;
		gridBagConstraints.ipady = ipady ;
		gridBagConstraints.fill = fill ;
		gridBagConstraints.anchor = anchor ;
		gridBagConstraints.insets = insets ;
		getLayout().setConstraints ( c , gridBagConstraints ) ;
	}
	
}