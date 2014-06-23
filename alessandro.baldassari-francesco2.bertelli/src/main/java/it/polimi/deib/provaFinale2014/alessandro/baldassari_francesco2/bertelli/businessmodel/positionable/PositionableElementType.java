package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

/**
 * This enum contains all the types a PositionableElement can be. 
 */
public enum PositionableElementType 
{
	
	EMPTY ,
	
	FENCE ,
	
	SHEPERD ,
	
	RED_SHEPERD ,
	
	BLUE_SHEPERD ,
	
	GREEN_SHEPERD ,
	
	YELLOW_SHEPERD ,
	
	STANDARD_ADULT_OVINE ,
	
	WOLF ,

	LAMB ,

	RAM ,
	
	SHEEP ,
	
	BLACK_SHEEP ;

	/**
	 * Given the name of a color, returns a Sheperd type of this color.
	 * 
	 * @param color the requested color.
	 * @return a Sheperd type whose color is specified by the parameter.
	 * @throws IllegalArgumentException if the color parameter is null.
	 */
	public static PositionableElementType getSheperdByColor ( String color ) 
	{
		PositionableElementType res ;
		if ( color != null )
		{
			if ( color.trim().compareToIgnoreCase ( "red" ) == 0 )
				res = RED_SHEPERD ;
			else
				if ( color.trim ().compareToIgnoreCase ( "yellow" ) == 0 )
					res = YELLOW_SHEPERD ;
				else
					if ( color.trim ().compareToIgnoreCase ( "blue" ) == 0 )
						res = BLUE_SHEPERD ;
					else
						if ( color.trim().compareToIgnoreCase ( "green" ) == 0 )
							res = GREEN_SHEPERD ;
						else
							res = null ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Decide if the p parameter represents a Sheperd.
	 * 
	 * @param p the positionableElementType to test.
	 * @return true, if the p parameter represents a Sheperd, false else.
	 */
	public static boolean isSheperd ( PositionableElementType p )
	{
		boolean res ;
		if ( p != null )
			if ( p == RED_SHEPERD || p == BLUE_SHEPERD || p == GREEN_SHEPERD || p == YELLOW_SHEPERD )
				res = true ;
			else
				res = false ;
		else
			res = false ;
		return res ;
	}
	
	/**
	 * Decide if the p parameter represents an Ovine but not a BlackSheep.
	 * 
	 * @param p the positionableElementType to test.
	 * @return true, if the p parameter represents an Ovine but not a BlackSheep, false else.
	 */
	public static boolean isStandardOvine ( PositionableElementType p ) 
	{
		boolean res ;
		if ( p != null )
		{
			if ( p == LAMB || p == SHEEP || p == RAM )
				res = true ;
			else
				res = false ;
		}
		else
			res = false ;
		return res ;
	}
	
}