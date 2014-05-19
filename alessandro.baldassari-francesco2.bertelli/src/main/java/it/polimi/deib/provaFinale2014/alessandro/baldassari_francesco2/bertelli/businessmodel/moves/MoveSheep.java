package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

public class MoveSheep extends ExecutableGameMove 
{

	private Sheperd moverSheperd ;
	private Ovine movingOvine ;
	private Region ovineDestinationRegion ;
	
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
				ovineInitialRegion.getContainedAnimals().remove ( movingOvine ) ;
				movingOvine.moveTo ( ovineDestinationRegion ) ;
				ovineDestinationRegion.getContainedAnimals ().add ( movingOvine ) ;
			}
			else
				throw new MoveNotAllowedException () ;
		}
		else
			throw new MoveNotAllowedException () ;
	}
	
}
