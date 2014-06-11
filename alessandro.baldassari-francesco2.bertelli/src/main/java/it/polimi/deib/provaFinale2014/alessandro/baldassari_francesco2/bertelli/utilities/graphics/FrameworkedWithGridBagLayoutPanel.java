package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics;


/***/
public abstract class FrameworkedWithGridBagLayoutPanel extends WithGridBagLayoutPanel
{

	/***/
	protected FrameworkedWithGridBagLayoutPanel () 
	{
		super () ;
		createComponents () ;
		manageLayout () ;
		bindListeners () ;
		injectComponents () ;
	}
	
	/***/
	protected abstract void createComponents () ;

	/***/
	protected abstract void manageLayout () ;

	/***/
	protected abstract void bindListeners () ;

	/***/
	protected abstract void injectComponents () ; 

}
