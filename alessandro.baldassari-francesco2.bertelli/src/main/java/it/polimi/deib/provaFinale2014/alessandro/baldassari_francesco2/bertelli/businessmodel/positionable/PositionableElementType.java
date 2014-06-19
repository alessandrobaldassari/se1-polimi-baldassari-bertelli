package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

/***/
public enum PositionableElementType 
{
	
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
	
	public static boolean isStandardAdultOvine ( PositionableElementType p ) 
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