package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/**
 * This class Models the MoveSheep move.
 * In this context, a Sheperd wants to move an Ovine from the Region the Ovine is
 * now to a new destination Region. 
 */
public class MoveSheep extends GameMove 
{

	/**
	 * The Sheperd who wants to make this move. 
	 */
	private Sheperd moverSheperd ;
	
	/**
	 * The Ovine that is going to be moved. 
	 */
	private Ovine movingOvine ;
	
	/**
	 * The new destination of the moving ovine. 
	 */
	private Region ovineDestinationRegion ;
	
	/**
	 * @param moverSheperd the Sheperd who wants to make this move. 
	 * @param movingOvine the Ovine that is going to be moved. 
	 * @param ovineDestinationRegion the new destination of the moving ovine. 
	 * @throws IllegalArgumentException if the moverSheperd or the movingOvine or the
	 *         ovineDestinationRegion parameter is null.  
	 */
	MoveSheep ( Sheperd moverSheperd , Ovine movingOvine , Region ovineDestinationRegion ) 
	{
		if ( moverSheperd != null && movingOvine != null && ovineDestinationRegion != null )
		{
			this.moverSheperd = moverSheperd ;
			this.movingOvine = movingOvine ;
			this.ovineDestinationRegion = ovineDestinationRegion ;
		}
		else 
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * The effective algorithm-method.
	 * It checks if the the destinationRegion respects the business rules, and if so
	 * performs the moving.
	 * 
	 * @param match the Match object on which this move is performed.
	 * @throws MoveNotAllowedException if something goes wrong.
	 */
	@Override
	public void execute ( Match match ) throws MoveNotAllowedException 
	{
		Region ovineInitialRegion ;
		Region sheperdFirstBorderRegion ;
		Region sheperdSecondBorderRegion ;
		ovineInitialRegion = movingOvine.getPosition () ;
		sheperdFirstBorderRegion = moverSheperd.getPosition ().getFirstBorderRegion () ;
		sheperdSecondBorderRegion = moverSheperd.getPosition ().getSecondBorderRegion () ;
		if ( movingOvine.getPosition ().equals ( ovineDestinationRegion ) == false )
		{
			if ( ( ovineInitialRegion.equals ( sheperdFirstBorderRegion ) || ovineInitialRegion.equals ( sheperdSecondBorderRegion ) ) && ( ovineDestinationRegion.equals ( sheperdFirstBorderRegion ) == true || ovineDestinationRegion.equals ( sheperdSecondBorderRegion ) == true ) )
			{
				ovineInitialRegion.removeAnimal ( movingOvine ) ;
				movingOvine.moveTo ( ovineDestinationRegion ) ;
				ovineDestinationRegion.addAnimal ( movingOvine ) ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ;
		}
		else
			throw new MoveNotAllowedException ( "" ) ;
	}
	
}
