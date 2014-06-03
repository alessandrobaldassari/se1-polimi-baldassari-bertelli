package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.util.Collection;
import java.util.LinkedList;

/***/
public class FactorySupport < T >
{

	private Collection < Identifiable < T > > alreadyUsers ;
	
	public FactorySupport () 
	{
		alreadyUsers = new LinkedList < Identifiable < T > > () ;
	}
	
	public void addUser ( Identifiable < T > newUser ) 
	{
		alreadyUsers.add ( newUser ) ;
	} 
	
	public boolean isAlreadyUser ( Identifiable < T > key ) 
	{
		boolean res ;
		res = false ;
		for ( Identifiable < T > identifiable : alreadyUsers )
			if ( key.isEqualsTo ( identifiable ) )
			{
				res = true;
				break;
			}
		return res ;
	}
	
}
