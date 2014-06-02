package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

/***/
public interface PositionableElementReference 
{

	/***/
	public void isEqualsTo ( PositionableElementReference otherRef ) ;
	
	/***/
	class PositionableElementReferenceImpl implements PositionableElementReference
	{
		
		/***/
		private PositionableElement < ? > referenced ;
		
		/***/
		public PositionableElementReferenceImpl ( PositionableElement < ? > referenced ) 
		{
			if ( referenced != null )
				this.referenced = referenced ;
			else
				throw new IllegalArgumentException () ;
		}

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void isEqualsTo ( PositionableElementReference otherRef ) 
		{
			PositionableElementReferenceImpl o ;
			boolean res ;
			if ( otherRef instanceof PositionableElementReferenceImpl )
			{
				o = ( PositionableElementReferenceImpl ) otherRef ;
				res = referenced.equals ( o.referenced ) ;
			}
			else
				res = false ;
		}
		
	}

}
