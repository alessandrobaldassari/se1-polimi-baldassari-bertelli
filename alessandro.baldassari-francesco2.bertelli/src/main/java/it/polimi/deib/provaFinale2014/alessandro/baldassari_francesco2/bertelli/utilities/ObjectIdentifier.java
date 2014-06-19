package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.io.Serializable;

/**
 * A class whose intent is to declare that a given class ( T ) is identifiable with a strong type
 * mechanism, implementing this interface.
 * Obviously the comparation means only between objects that implements this interface both. 
 */
public interface ObjectIdentifier < T > extends Serializable
{

	/***/
	public boolean isEqualsTo ( ObjectIdentifier < T > otherObject ) ; 
	
}
