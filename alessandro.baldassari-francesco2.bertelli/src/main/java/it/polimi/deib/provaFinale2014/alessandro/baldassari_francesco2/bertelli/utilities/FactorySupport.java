package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;

import java.util.Collection;
import java.util.LinkedList;

public class FactorySupport 
{

	private Collection < Identifiable < Match > > alreadyUsers ;
	
	public FactorySupport () 
	{
		alreadyUsers = new LinkedList < Identifiable < Match > > () ;
	}
	
	public void addUser ( Identifiable < Match > newUser ) 
	{
		alreadyUsers.add ( newUser ) ;
	} 
	
	public boolean isAlreadyUser ( Identifiable < Match > key ) 
	{
		boolean res ;
		res = true ;
		for ( Identifiable < Match > identifiable : alreadyUsers )
			if ( key.isEqualsTo ( identifiable ) )
			{
				res = false;
				break;
			}
		return res ;
	}
	
}
