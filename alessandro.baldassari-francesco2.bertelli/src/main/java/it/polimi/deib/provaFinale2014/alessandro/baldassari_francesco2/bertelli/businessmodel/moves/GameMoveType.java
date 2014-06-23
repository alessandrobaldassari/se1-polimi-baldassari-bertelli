package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

/**
 * This enum describes the possible moves in the Game. 
 */
public enum GameMoveType 
{
		
	BREAK_DOWN ( "BREAKDOWN" ) ,
		
	BUY_CARD ( "BUY_CARD" ) ,
		
	MATE ( "MATE" ) ,
		
	MOVE_SHEEP ( "MOVE_SHEEP" ) ,
		
	MOVE_SHEPERD ( "MOVE_SHEPERD" ) ;
	
	/**
	 * A human readable name for this GameMove. 
	 */
	private String humanName ;
	
	/**
	 * @param humanName the value for the humanName field. 
	 */
	private GameMoveType ( String humanName ) 
	{
		this.humanName = humanName ; 
	}
	
	/**
	 * Getter method for the humanName field.
	 * 
	 * @return the humanName property.
	 */
	public String getHumanName () 
	{
		return humanName ;
	}
	
}
