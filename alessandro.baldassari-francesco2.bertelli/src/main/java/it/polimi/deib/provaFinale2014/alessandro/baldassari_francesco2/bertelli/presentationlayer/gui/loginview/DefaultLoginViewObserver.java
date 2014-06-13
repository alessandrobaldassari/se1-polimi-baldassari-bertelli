package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.loginview;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultLoginViewObserver implements LoginViewObserver 
{

	private AtomicReference < String > name ;
	
	public DefaultLoginViewObserver ( AtomicReference < String > name ) 
	{
		this.name = name ;
	}
	
	@Override
	public void onNameEntered ( String enteredName ) 
	{
		synchronized ( name ) 
		{
			name.set(enteredName); 
			name.notifyAll();
		}	
	}

	@Override
	public void onDoNotWantToEnterName() {}
	
}