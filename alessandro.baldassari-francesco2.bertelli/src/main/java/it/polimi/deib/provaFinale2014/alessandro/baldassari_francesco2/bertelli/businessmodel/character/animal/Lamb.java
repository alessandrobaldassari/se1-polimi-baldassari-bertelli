package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

public class Lamb extends Ovine {

	private int birthTurn ;
	
	public Lamb ( String name , int birthTurn ) 
	{
		super ( name ) ;
		if ( birthTurn > 0 )
			this.birthTurn = birthTurn ;
		else
			throw new IllegalArgumentException () ;
	}

	public int getBirthTurn () 
	{
		return birthTurn ;
	}
	
}
