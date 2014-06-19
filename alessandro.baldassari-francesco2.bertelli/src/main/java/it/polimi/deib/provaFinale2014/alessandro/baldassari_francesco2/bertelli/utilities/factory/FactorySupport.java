package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/***/
public class FactorySupport < T > implements Serializable
{

	/***/
	private Collection < ObjectIdentifier < T > > alreadyUsers ;
	
	/***/
	public FactorySupport () 
	{
		alreadyUsers = new LinkedList < ObjectIdentifier < T > > () ;
	}
	
	/***/
	public void addUser ( ObjectIdentifier < T > newUser ) 
	{
		alreadyUsers.add ( newUser ) ;
	} 
	
	/***/
	public boolean isAlreadyUser ( ObjectIdentifier < T > key ) 
	{
		boolean res ;
		res = false ;
		for ( ObjectIdentifier < T > identifiable : alreadyUsers )
			if ( key.isEqualsTo ( identifiable ) )
			{
				res = true;
				break;
			}
		return res ;
	}
	
}
