package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.MapUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

/**
 * This class models the Black Sheep, a particular Sheep which exists in one
 * instance per Match.
 */
public class BlackSheep extends AdultOvine 
{

	// METHODS
	
	/**
	 * @param name the name of this BlackSheep 
	 */
	protected BlackSheep ( String name ) 
	{
		super ( PositionableElementType.BLACK_SHEEP , name , AdultOvineType.SHEEP ) ; 
	}

	/**
	 * Actuate the escape behavior of a BlackSheep.
	 * It first retrieves all the possible destinations of the BlackSheep, then, if the BlackSheep has to move
	 * ( due to business rules ), move it to the correct place ( that anyway is a Random one ). 
	 * 
	 * @throws CharacterDoesntMoveException if this BlackSheep does not move.  
	 */
	public void escape () throws CharacterDoesntMoveException 
	{	
		Iterable < Road > borders ;
		Road winnerRoad ;
		Region myRegion ;
		Region destinationRegion ;
		int diceResult ;
		diceResult = MathUtilities.launchDice () ;
		myRegion = getPosition () ;
		borders = myRegion.getBorderRoads () ; 
		winnerRoad = null ;
		for ( Road road : borders )
			if ( road.getNumber () == diceResult )
			{
				winnerRoad = road ;
				break ;
			}
		if ( winnerRoad != null && winnerRoad.getElementContained () == null  ) 
		{
			destinationRegion = MapUtilities.getOtherAdjacentDifferentFrom ( winnerRoad , myRegion ) ;
			myRegion.removeAnimal ( this ) ;
			destinationRegion.addAnimal ( this ) ;
			moveTo ( destinationRegion ) ;
		}
		else
			throw new CharacterDoesntMoveException ( this ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		if ( obj instanceof BlackSheep )
			res = super.equals ( obj ) ;
		else
			res = false ;
		return res ;
	}
	
}
