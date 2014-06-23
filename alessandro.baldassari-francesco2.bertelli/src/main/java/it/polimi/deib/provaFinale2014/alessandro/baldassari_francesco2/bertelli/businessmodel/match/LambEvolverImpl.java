package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AnimalFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

/**
 * Component that manages the evolution of the Lambs in the Game. 
 */
class LambEvolverImpl implements LambEvolver 
{
	
	// ATTRIBUTES

	/**
	 * An AnimalFactory object to replace the evolving Lamb. 
	 */
	private AnimalFactory animalFactory ;
	
	// METHODS
	
	/**
	 * @param animalFactory the value for the animalFactory instance.
	 * @throws IllegalArgumentException if the animalFactory parameter is null. 
	 */
	protected LambEvolverImpl ( AnimalFactory animalFactory ) 
	{
		if ( animalFactory != null )
			this.animalFactory = animalFactory ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void evolve ( Lamb lamb ) 
	{
		Region whereTheLambIsNow ;
		Ovine newOvine ;
		if ( lamb != null )
		{
			whereTheLambIsNow = lamb.getPosition () ;
			whereTheLambIsNow.removeAnimal ( lamb ) ;
			lamb.moveTo ( null ) ;
			newOvine = animalFactory.newAdultOvine ( lamb.getName () + "EVOLVED" , MathUtilities.genProbabilityValue() > 0.5 ? AdultOvineType.RAM : AdultOvineType.SHEEP ) ;
			newOvine.moveTo ( whereTheLambIsNow ) ;
			whereTheLambIsNow.addAnimal ( newOvine ) ;
		}
		else
			throw new IllegalArgumentException () ;	
	}

}
