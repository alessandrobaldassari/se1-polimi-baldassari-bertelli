package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

/**
 * This class models an Ovine, the common factor type between Sheeps, Rams and Lambs.
 * Anyway, it adds no attributes or behaviors to the Animal description. 
 */
public abstract class Ovine extends Animal 
{

	/**
	 * @param name the name of this ovine. 
	 */
	Ovine ( PositionableElementType positionableElementType , String name ) 
	{
		super ( positionableElementType , name ) ;
	}

}
