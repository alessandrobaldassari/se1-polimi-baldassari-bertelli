package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MathUtilities;

/**
 * This class models an AdultOvine, which is a Sheep or a Ram.
 * AdultOvine's can mate between them, and so can generate Lambs, sometimes, but obviously
 * the mating process can be only between two AdultOvines of different types ( sex ). 
 */
public class AdultOvine extends Ovine
{

	/**
	 * The type ( sex ) of this AdultOvine, Sheep XOR Ram.
	 */
	private AdultOvineType type ;
	
	/**
	 * @param name the name of this AdultOvine.
	 * @type the type ( sex ) of this AdultOvine, Sheep XOR Ram.
	 * @throws IllegalArgumentException if the type parameter is null.
	 */
	protected AdultOvine ( String name , AdultOvineType type ) 
	{
		this ( type == AdultOvineType.RAM ? PositionableElementType.RAM : PositionableElementType.SHEEP , name , type ) ;
	}
	
	protected AdultOvine ( PositionableElementType positionableElementType , String name , AdultOvineType type )
	{
		super ( positionableElementType , name ) ;
		if ( type != null )
			this.type = type ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the type property.
	 * 
	 * @return the type property.
	 */
	public AdultOvineType getType () 
	{
		return type ;
	}
	
	/**
	 * This method represent the mate behavior between a Ram and a Sheep.
	 * If this AdultOvine and the partner parameter AdultOvine have different
	 * type ( sex ), and if the mate has success ( random decision ) a new Lamb is returned
	 * to the caller, else exceptions are throwns.
	 * 
	 * @param partner the partner with which mate.
	 * @return the generated Lamb if everything goes ok.
	 * @throws CanNotMateWithHimException if the partner's type is equals
	 *         to this AdultOvine's type
	 * @throws MateNotSuccesfullException if this mate process does not go well.
	 */
	public Lamb mate ( AdultOvine partner ) throws CanNotMateWithHimException, MateNotSuccesfullException  
	{
		AdultOvine father ;
		AdultOvine mother ;
		Lamb res ;
		double d ; 
		if ( type != partner.getType () )
		{
			d = MathUtilities.genProbabilityValue () ;
			if ( d > 0.5 )
			{
				if ( type == AdultOvineType.RAM )
				{
					mother = partner ;
					father = this ;
				}
				else
				{
					mother = this ;
					father = partner ;
				}
				res = new Lamb ( "" , 0 , father , mother ) ;
			}
			else
				throw new MateNotSuccesfullException ( this , partner ) ; 
		}
		else
			throw new CanNotMateWithHimException ( this , partner ) ; 
		return res ;
	}
	
	@Override
	public boolean equals ( Object obj ) 
	{
		boolean res ;
		AdultOvine otherAdOv ;
		if ( obj instanceof AdultOvine )
		{
			otherAdOv = ( AdultOvine ) obj ;
			if ( otherAdOv.getType () == type )
				res = super.equals ( obj ) ;
			else
				res = false ;
		}
		else 
			res = false ;
		return res ;
	}
	
	@Override
	public String toString ()
	{
		return "AdultOvine: " + type;
		
	}

	// ENUMS
	
	/**
	 * Models the type an AdultOvine can have:
	 * 1. RAM : the male.
	 * 2. SHEEP : the famale. 
	 */
	public enum AdultOvineType 
	{
		
		RAM ,
		
		SHEEP 
		
	}
	
	// INNER CLASSES
	 
	// EXCEPTIONS
	
	/**
	 * This class models a generic exception during the mating process. 
	 */
	public abstract class MatingException extends Exception 
	{
		
		/**
		 * The first partner involved in the mating process. 
		 */
		private final AdultOvine firstPartner ;
		
		/**
		 * The second partner involved in the mating process. 
		 */
		private final AdultOvine secondPartner ;
		
		/**
		 * @param firstPartner the first partner involved in the mating process.
		 * @param secondPartner the second partner involved in the mating process.
		 * @throws IllegalArgumentException if the firstPartner or the secondPartner
		 *	       parameter is null. 
		 */
		protected MatingException ( AdultOvine firstPartner , AdultOvine secondPartner ) 
		{
			super () ;
			if ( firstPartner != null && secondPartner != null )
			{
				this.firstPartner = firstPartner ;
				this.secondPartner = secondPartner ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * @return the firstPartner property. 
		 */
		public AdultOvine getFirstPartner () 
		{
			return firstPartner ;
		}
		
		/**
		 * @return the secondPartner property. 
		 */
		public AdultOvine getSecondPartner () 
		{
			return secondPartner ;
		}
		
	}
	
	/**
	 * This class model the wrong situation where two AdultOvine of the same type ( sex )
	 * tries to mate. 
	 */
	public class CanNotMateWithHimException extends MatingException 
	{

		protected CanNotMateWithHimException ( AdultOvine firstPartner , AdultOvine secondPartner ) 
		{
			super ( firstPartner , secondPartner ) ;
		}		
		
	}
	
	/**
	 * This class model the situation where a mate process does not go well. 
	 */
	public class MateNotSuccesfullException extends MatingException 
	{
		
		protected MateNotSuccesfullException ( AdultOvine firstPartner , AdultOvine secondPartner ) 
		{
			super ( firstPartner , secondPartner ) ;
		}		
		
	}
	
}
