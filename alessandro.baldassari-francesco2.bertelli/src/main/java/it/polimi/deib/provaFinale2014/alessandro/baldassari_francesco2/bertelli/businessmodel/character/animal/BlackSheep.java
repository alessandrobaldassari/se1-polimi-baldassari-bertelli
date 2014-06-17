package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

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

	/**
	 * @param name the name of this BlackSheep 
	 */
	protected BlackSheep ( String name ) 
	{
		super ( PositionableElementType.BLACK_SHEEP , name , AdultOvineType.SHEEP ) ; 
	}

	/**
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
			if ( winnerRoad.getFirstBorderRegion ().equals ( myRegion ) )
				destinationRegion = winnerRoad.getSecondBorderRegion () ;
			else
				destinationRegion = winnerRoad.getFirstBorderRegion () ;
			destinationRegion.addAnimal ( this ) ;
			myRegion.removeAnimal ( this ) ;
			
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
