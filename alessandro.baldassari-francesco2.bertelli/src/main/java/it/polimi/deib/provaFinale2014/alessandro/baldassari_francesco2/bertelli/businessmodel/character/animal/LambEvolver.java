package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

/**
 * This interface defines a component that is able to evolve a Lamb into an AdultOvine. 
 */
public interface LambEvolver 
{
	
	/**
	 * The method which will perform the evolution.
	 * 
	 * @param lamb the evolving Lamb.
	 */
	public void evolve ( Lamb lamb ) ;
	
}