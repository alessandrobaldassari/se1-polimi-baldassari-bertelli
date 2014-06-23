package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

public final class TimeConstants 
{

	/**
	 * The time this Controller has to wait w.r.t the Game Controller during the Match starting. 
	 */
	public static final long MATCH_LAUNCHER_SERVER_WAITING_TIME = 15 * Utilities.MILLISECONDS_PER_SECOND ;

	/**
	 * The Timer value about the time to wait before begin a Match. 
	 */
	public static final long MATCH_CONTROLLER_TIMER_TIME = 45 * Utilities.MILLISECONDS_PER_SECOND ;
	
	/***/
	public static final long CONNECTION_LOOSING_SERVER_WAITING_TIME = 300 * Utilities.MILLISECONDS_PER_SECOND ;

	/***/
	public static final long CONNECTION_LOOSING_SERVER_SUSPENSION_TIME = 60 * Utilities.MILLISECONDS_PER_SECOND ;
	
	private TimeConstants (){}
	
	
}
