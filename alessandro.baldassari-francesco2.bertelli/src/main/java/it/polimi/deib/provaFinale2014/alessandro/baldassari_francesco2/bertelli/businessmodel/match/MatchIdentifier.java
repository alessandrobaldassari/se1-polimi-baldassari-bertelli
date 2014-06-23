package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.UIDGenerator;

/**
 * This class implements a MatchIdentifier for the Match managed by this GameController  
 */
public class MatchIdentifier implements ObjectIdentifier < Match > 
{

	// ATTRIBUTES
	
	private static transient UIDGenerator uidGenerator ;
	
	/**
	 * The unique identifier for this MatchIdentifier.
	 */
	private long uid ;
	
	// METHODS
	
	/**
	 * @param uid the unique identifier for this MatchIdentifier. 
	 */
	private MatchIdentifier ( long uid ) 
	{
		this.uid = uid ;
	}
	
	/**
	 * Creator method for this class.
	 * 
	 * @return a new MatchIdentifier object.
	 */
	public synchronized static MatchIdentifier newInstance () 
	{
		if ( uidGenerator == null )
			uidGenerator = new UIDGenerator ( 0L ) ;
		return new MatchIdentifier ( uidGenerator.generateNewValue () ) ;
	}
	
	/**
	 * Getter method for the uid property.
	 * 
	 * @return the uid property. 
	 */
	public long getUID () 
	{
		return uid ;
	}
	
	/**
	 * Determine if this MatchIdentifier object is the same as the one
	 * passed by parameter.
	 * 
	 * @param otherObject the otherObject to compare this one.
	 * @return true if this object is equals to the one passed by parameter, false else. 
	 */
	@Override
	public boolean isEqualsTo ( ObjectIdentifier<Match> otherObject ) 
	{
		if ( otherObject instanceof MatchIdentifier )
			return uid == ( ( MatchIdentifier ) otherObject).getUID () ;
		return false ;
	}

}

