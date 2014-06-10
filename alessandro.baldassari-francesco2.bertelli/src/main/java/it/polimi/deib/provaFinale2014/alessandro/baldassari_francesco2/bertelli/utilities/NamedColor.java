package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

import java.awt.Color;

public class NamedColor extends Color
{
	
	private String name ;

	public NamedColor ( int r , int g , int b , String name ) 
	{
		super ( r , g , b ) ;
		if ( name != null )
			this.name = name ;
		else
			throw new IllegalArgumentException () ;
	}	
	
	public String getName () 
	{
		return name ;
	}
	
	@Override
	public String toString () 
	{
		String res ;
		res = getName ().toUpperCase() ;
		return res ;
	}
	
}
