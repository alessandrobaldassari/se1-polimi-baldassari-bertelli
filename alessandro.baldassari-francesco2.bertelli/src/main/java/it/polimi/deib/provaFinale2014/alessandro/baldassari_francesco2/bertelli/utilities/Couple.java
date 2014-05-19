package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/**
 * A generic and general purpose class that represents an object containing two other object.
 * Immutable implementations ( also the two object values contained may change themselves internally ).
 */
public class Couple < T , S > 
{

	// ATTRIBUTES
	
	/**
	 * The first object contained in this Couple.
	 */
	private final T firstObject ;
	
	/**
	 * The second object contained in this Couple.
	 */
	private final S secondObject ;
	
	// METHODS
	
	/**
	 * @param firstObject the first object to be contained in this Couple.
	 * @param secondObject the second object to be contained in this Couple.
	 */
	public Couple ( T firstObject , S secondObject ) 
	{
		this.firstObject = firstObject ;
		this.secondObject = secondObject ;
	}
	
	/**
	 * AS THE SUPER ONE'S.
	 * Two Couple objects are considered equals if, and only if, the two component objects are equals;
	 * the position is not important; the only thing that matters is that there exist an ordering in which
	 * the two component object are respectively equals each other.
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		Couple otherObj ;
		boolean res ;
		if ( obj instanceof Couple )
		{
			otherObj = ( Couple ) obj ;
			res = ( firstObject.equals ( otherObj.getFirstObject () ) && secondObject.equals ( otherObj.getSecondObject () ) ) 
			|| ( firstObject.equals ( otherObj.getSecondObject() ) && secondObject.equals ( otherObj.getFirstObject () ) ) ;
		}
		else
			res = false ;
		return res ;
	}
	
	@Override
	public String toString () 
	{
		String res ;
		StringBuffer stringBuffer ;
		stringBuffer = new StringBuffer () ;
		stringBuffer.append( "Class : " + getClass ().getName () ) ;
		stringBuffer.append ( "\nFirst Object : " + firstObject.toString () ) ;
		stringBuffer.append ( "\nSecond Object : " + secondObject.toString () ) ;
		res = stringBuffer.toString () ;
		return res ;
	}
	
	/**
	 * Getter for the first object of this Couple.
	 * 
	 * @return the first object contained in this Couple.
	 */
	public T getFirstObject () 
	{
		return firstObject ;
	}
	
	/**
	 * Getter for the second object of this Couple.
	 * 
	 * @return the second object contained in this Couple.
	 */
	public S getSecondObject () 
	{
		return secondObject ;
	}
	
}
