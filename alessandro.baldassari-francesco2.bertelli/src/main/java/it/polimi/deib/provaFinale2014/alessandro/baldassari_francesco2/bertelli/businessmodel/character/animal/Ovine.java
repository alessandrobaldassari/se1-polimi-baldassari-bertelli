package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

/**
 * This class models an Ovine, the common factor type between Sheeps, Rams and Lambs.
 * Anyway, it adds no attributes or behaviors to the Animal description. 
 */
public abstract class Ovine extends Animal 
{

	// ATTRIBUTES
	
	/**
	 * @param positionableElementType the PositionableElementType of this Ovine.
	 * @param name the name of this ovine. 
	 */
	protected Ovine ( PositionableElementType positionableElementType , String name ) 
	{
		super ( positionableElementType , name ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		if ( obj instanceof Ovine )
			res = super.equals ( obj ) ;
		else
			res = false ;
		return res ;
	}
	
}
