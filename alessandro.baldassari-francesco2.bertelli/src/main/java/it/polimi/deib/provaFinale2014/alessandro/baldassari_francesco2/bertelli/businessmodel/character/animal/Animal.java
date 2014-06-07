package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Character;

/**
 * This class models a generic Animal which stays in the Game.
 * It adds no properties or behaviours to the description did by the Character one, except
 * the fact that an Animal can only stay in a Region MapElement.
 */
public abstract class Animal extends Character < Region > 
{

	/**
	 * @param name the name of this Animal 
	 */
	protected Animal ( PositionableElementType positionableElementType , String name ) 
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
		if ( obj instanceof Animal )
			res = super.equals ( obj ) ;
		else
			res = false ;
		return res ;
	}
	
}
