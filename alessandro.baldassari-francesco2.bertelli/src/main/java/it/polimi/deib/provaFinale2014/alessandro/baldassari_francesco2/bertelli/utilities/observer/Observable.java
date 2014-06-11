package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer;

/***/
public interface Observable < T extends Observer >
{

	/***/
	public void addObserver ( T newObserver ) ;
	
	/***/
	public void removeObserver ( T oldObserver ) ;
	
}
