package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;


public abstract class AdultOvine extends Ovine
{

	private AdultOvineType type ;
	
	public AdultOvine ( String name , AdultOvineType type ) 
	{
		super ( name ) ;
		if ( type != null )
			this.type = type ;
		else
			throw new IllegalArgumentException () ;
	}

	public AdultOvineType getType () 
	{
		return type ;
	}
	
	public Lamb mate ( AdultOvine partner ) throws CanNotMateWithHimException, MateNotSuccesfullException  
	{
		Lamb res ;
		double d ; 
		if ( type != partner.getType () )
		{
			d = Math.random () ;
			if ( d > 0.5 )
				res = new Lamb () ;
			else
				throw new MateNotSuccesfullException () ; 
		}
		else
			throw new CanNotMateWithHimException ( this , partner ) ; 
		return null ;
	}

	public enum AdultOvineType 
	{
		
		RAM ,
		
		SHEEP 
		
	}
	
	public class CanNotMateWithHimException extends Exception 
	{
		
		private AdultOvine firstPartner ;
		private AdultOvine secondPartner ;
		
		public CanNotMateWithHimException ( AdultOvine firstPartner , AdultOvine secondPartner ) 
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
		
	}
	
	public class MateNotSuccesfullException extends Exception {}
	
}
