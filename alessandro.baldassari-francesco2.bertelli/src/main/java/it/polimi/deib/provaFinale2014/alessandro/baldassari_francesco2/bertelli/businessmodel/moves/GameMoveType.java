package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

public enum GameMoveType 
{
		
	BREAK_DOWN ( "BREAKDOWN" ) ,
		
	BUY_CARD ( "BUY_CARD" ) ,
		
	MATE ( "MATE" ) ,
		
	MOVE_SHEEP ( "MOVE_SHEEP" ) ,
		
	MOVE_SHEPERD ( "MOVE_SHEPERD" ) ;
	
	private String humanName ;
	
	private GameMoveType ( String humanName ) 
	{
		this.humanName = humanName ; 
	}
	
	public String getHumanName () 
	{
		return humanName ;
	}
	
}
