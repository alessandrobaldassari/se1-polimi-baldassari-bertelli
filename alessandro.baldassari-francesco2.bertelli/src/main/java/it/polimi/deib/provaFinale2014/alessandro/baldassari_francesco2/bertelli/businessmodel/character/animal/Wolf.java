package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.NoRoadWithThisNumberException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.CharacterDoesntMoveException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

/**
 * This class models a Wolf, a particular Animal present in the Game.
 * Just one Wolf can be present per Match.
 */
public class Wolf extends Animal
{

	/**
	 * @param name the name of this Wolf. 
	 */
	protected Wolf ( String name ) 
	{
		super ( PositionableElementType.WOLF , name ) ;
	}
	
	/**
	 * Method that simulates the Wolf Escaping.
	 * W.R.T. this Wolf tries to escape, and if he succeeds in it, eat ( if possible ) an
	 * animal of the Region where he arrived.
	 * 
	 * @throws CharacterDoesntMoveException if this Wold does not move ( an Exception but it is not 
	 *         a real error ).
	 */
	public void escape () throws CharacterDoesntMoveException
	{
		Road winnerRoad ;
		Region initRegion ;
		Region destRegion ;
		int diceResult ;
		boolean allGatesClosed ;
		try 
		{
			diceResult = MathUtilities.launchDice () ;
			initRegion = getPosition () ;
			allGatesClosed = initRegion.isClosed () ;	
			winnerRoad = initRegion.getBorderRoad ( diceResult ) ;
			// se il lupo si può muovere
			if ( allGatesClosed || ( ! allGatesClosed && !( winnerRoad.getElementContained () instanceof Fence) ) ) 
			{
				if ( winnerRoad.getFirstBorderRegion ().equals ( initRegion ) )
					destRegion = winnerRoad.getSecondBorderRegion () ;
				else
					destRegion = winnerRoad.getFirstBorderRegion () ;
				initRegion.removeAnimal ( this ) ;
				destRegion.addAnimal ( this ) ;
				setPosition ( destRegion ) ;
				// il lupo cerca di mangiare qualcuno
				for ( Animal a : destRegion.getContainedAnimals () )
					if ( ! ( a instanceof BlackSheep ) )
					{
						// mangia
						a.getPosition().removeAnimal ( a ) ;
						a.moveTo ( null ) ; 
					}
			}
			else
				throw new CharacterDoesntMoveException ( this ) ;
		} 
		catch (NoRoadWithThisNumberException e ) 
		{
			e.printStackTrace();
			throw new CharacterDoesntMoveException ( this ) ;			
		}
		
	}
	
	@Override
	public boolean equals ( Object obj )
	{
		boolean res ;
		if ( obj instanceof Wolf )
			res = super.equals(obj) ;
		else
			res = false ;
		return res ;
	}
	
}
