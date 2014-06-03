package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Observer;

public class DummyObserver implements Observer
{

	private Integer dummyInt ;
	
	public DummyObserver () 
	{
		dummyInt = null ;
	}
	
	public void setDummyInt ( Integer dummyInt ) 
	{
		this.dummyInt = dummyInt ;
	}
	
	public Integer getDummyInt () 
	{
		return dummyInt ;
	}
	
}
