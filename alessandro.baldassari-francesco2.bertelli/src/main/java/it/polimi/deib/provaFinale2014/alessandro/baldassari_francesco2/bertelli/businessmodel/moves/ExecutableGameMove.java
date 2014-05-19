package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;

public abstract class ExecutableGameMove extends GameMove
{

	public abstract void execute ( Match match ) throws MoveNotAllowedException ;
	
	class MoveNotAllowedException extends Exception {}
	
}
