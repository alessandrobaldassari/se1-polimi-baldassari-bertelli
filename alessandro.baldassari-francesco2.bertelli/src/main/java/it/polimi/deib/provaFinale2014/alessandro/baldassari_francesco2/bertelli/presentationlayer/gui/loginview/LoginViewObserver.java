package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the events a LoginViewObserver can listen to. 
 */
public interface LoginViewObserver extends Observer
{
	
	/**
	 * Called when the User choosed and confirmed his name. 
	 */
	public void onNameEntered ( String enteredName ) ;
	
	/**
	 * Called when the user does not want to complete this operation. 
	 */
	public void onDoNotWantToEnterName () ;
	
}

