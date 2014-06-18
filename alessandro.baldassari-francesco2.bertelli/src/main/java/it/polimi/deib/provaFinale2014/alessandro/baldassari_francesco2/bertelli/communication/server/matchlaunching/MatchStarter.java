package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchlaunching;

/**
 * Communication interface between a generic Initial Network Controller and a Game Controller.
 * It allows the Game Controller to communicate to the Initial Network Controller about the initial phases
 * of the Game, so the Initial Network Controller can know when the Phase Start is finished ( and
 * also why ) and act as a consequence. 
 */
public interface MatchStarter 
{

	/**
	 * Method that manages the situation where a GameController notifies that 
	 * a timeout expires and there is a number of Player < 2, so the match will not start.
	 * The methods cancel the Match and notify the added Client that his request has been
	 * rejected.
	 */
	public void notifyFailStartMatch () ;
	
	/**
	 * Method that manages the situation where the current GameController notifies that
	 * it has reached the situation where a Match can start.
	 * Standing the contract of the GameController class, it will start the workflow 
	 * automatically, so the only thing this method has to do is to set the 
	 * currentGameController property to null.
	 */
	public void notifyFinishAddingPlayers () ;
	
}
