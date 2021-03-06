package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

/**
 * This class models a Lamb, the Ovine generated when a Ram and a Sheep mate.
 * A Lamb remains a Lamb only a given number of turns in the Game ( this parameter
 * is a business rule and is not known in advance in this class ).
 * When it comes a turn when its number, subtracting the birthDate of this Lamb, is equals
 * to the value said up, this Lamb has to become a Ram or a Sheep ( he can not remain a 
 * Lamb anymore ).
 */
public class Lamb extends Ovine 
{
	
	// ATTRIBUTES
	
	/**
	 * The father of this ovine, a Ram.
	 */
	private final AdultOvine father ;
	
	/**
	 * The mother of this ovine, a Sheep. 
	 */
	private final AdultOvine mother ;

	/**
	 * The birthDate of this Lamb, expressed in Game Turns time units. 
	 */
	private int birthTurn ;
	
	/**
	 * @param name the name of this Lamb
	 * @param birthTurn the birthDate of this Lamb, expressed in Game Turns times unit.
	 * @param father the AdultOvine father of this Lamb.
	 * @param mother the AdultOvine mother of this Lamb.
	 * @throws {@link IllegalArgumentException} if the mother parameter is null, or the father 
	 *         parameter is null, or the birthTurn is < 0, or the father's parameter type is 
	 *         equals to the mother's parameter type.
	 */
	protected Lamb ( String name , int birthTurn , AdultOvine father , AdultOvine mother ) 
	{
		super ( PositionableElementType.LAMB , name ) ;
		if ( father != null && mother != null && birthTurn >= 0 && father.getType () != mother.getType () )
		{
			this.birthTurn = birthTurn ;
			this.father = father ;
			this.mother = mother ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * Getter method for the birthTurn property.
	 * 
	 * @return the birthTurn property. 
	 */
	public int getBirthTurn () 
	{
		return birthTurn ;
	}
	
	/**
	 * Getter method for the father property.
	 * 
	 * @return the father property. 
	 */
	public AdultOvine getFather () 
	{
		return father ;
	}
	
	/**
	 * Getter method for the mother property.
	 * 
	 * @return the mother property. 
	 */
	public AdultOvine getMother () 
	{
		return mother ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		if ( obj instanceof Lamb )
			res = super.equals ( obj ) ;
		else
			res = false ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public String toString () 
	{
		String res ;
		res = super.toString () ;
		res = res + "Birth Turn : " + birthTurn + Utilities.CARRIAGE_RETURN ;
		res = res + "Mom : " + mother.getName() + Utilities.CARRIAGE_RETURN ;
		res = res + "Dad : " +father.getName() + Utilities.CARRIAGE_RETURN ;
		return res ;
	}
	
}
