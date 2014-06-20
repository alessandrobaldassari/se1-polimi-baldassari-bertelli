package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;

public final class PresentationMessages 
{

	/**
	 * The name of the App to display. 
	 */
	public static final String APP_NAME = "JSheepland" ;
	
	/**
	 * The message to show to welcome the User. 
	 */
	public static final String WELCOME_MESSAGE = "Benvenuto in JSheepland\nBeeee" ;
	
	/**
	 * The message to display during the time the App is connecting to the Server. 
	 */
	public static final String SERVER_CONNECTION_MESSAGE = "Connessione al server, prego attendere ..." ;
	
	/**
	 * The message to show to ask the User his username.
	 */
	public static final String NAME_REQUEST_MESSAGE = "Prego, inserisci il nome con cui vuoi giocare a JSheepland : " ;
	
	/**
	 * The message to display while verifying the correctness of the User name. 
	 */
	public static final String NAME_VERIFICATION_MESSAGE = "Verifico che il tuo nome non sia già usato..." ;
	
	/**
	 * The message to display if the name the user proposed is accepted. 
	 */
	public static final String NAME_ACCEPTED_MESSAGE = "Complimenti!\nIl tuo nome è stato accettato dal Server di JSheeplan.\nOra aspetta pochi istanti l'arrivo degli altri giocatori per iniziare a giocare" ;
	
	/**
	 * The message to display if the name the user proposed is not accepted. 
	 */
	public static final String NAME_REJECTED_MESSAGE = "Siamo spiacenti, ma il nome che hai proposto è già in uso nel contesto di JSheepland in questo momento.\nPrego, prova con un altro nome!" ;
	
	/**
	 * The message to display during the time we are waiting for other Players to arrive. 
	 */
	public static final String WAITING_FOR_OTHER_PLAYERS_MESSAGE = "Attendendo gli altri giocatori..." ;
	
	/**
	 * The message to display when everything is ready to start. 
	 */
	public static final String MATCH_STARTING_MESSAGE = "Tutto è pronto!\nGli avversari sono arrivati!\nPreparati alla partita!\nBeeeeeeeee" ;
	
	/**
	 * The message to display if he message will not start. 
	 */
	public static final String MATCH_WILL_NOT_START_MESSAGE = "Siamo veramente costernati ( o sarebbe meglio dire tosati... )\nPurtroppo nessun altro giocatore in rete ora ha voglia di fare il pastore, e quindi per il momento non puoi giocare...\nL'applicazione verrà ora chiusa.\nRiprova più tardi ( magari dopo avere inviato i tuoi amici ! )" ; ;

	/**
	 * The message to display to ask the user a color. 
	 */
	public static final String CHOOSE_COLOR_FOR_SHEPERD_MESSAGE = "Scegli un colore per il tuo pastore tra quelli proposti!" ;
	
	/**
	 * The message to display when the user has to choose an initial Road for one of his Sheperds. 
	 */
	public static final String CHOOSE_INITIAL_ROAD_FOR_A_SHEPERD_MESSAGE = "Scegli, cliccando sulla mappa, la strada dove vuoi posizionare il tuo pastore" ;

	/**
	 * The message to display when the user has to choose a Sheperd for a turn. 
	 */
	public static final String CHOOSE_SHEPERD_FOR_A_TURN_MESSAGE = "Scegli, cliccandolo sulla mappa, il pastore che vuoi usare questo turno." ;
	
	/**
	 * The message to display when the user makes an incorrect choice. 
	 */
	public static final String INVALID_CHOOSE_MESSAGE = "Sorry, scelta non valida." ;
	
	/**
	 * The message to display when the User has to choose a GameMove during the Match. 
	 */
	public static final String DO_MOVE_MESSAGE = "Scegli una mossa da effettuare!" ;
	
	/**
	 * The message to display when the User did a move and this move has gone well. 
	 */
	public static final String MOVE_SUCCEED_MESSAGE = "Mossa riuscita perfettamente!" ;
	
	/**
	 * The message to display when the User try to do a not allowed move. 
	 */
	public static final String MOVE_NOT_ALLOWED_MESSAGE = "Sorry, ma non puoi fare questa mossa!" ;
	
	/**
	 * The message to display when a Player tries to execute an action but does not have enough money to do it. 
	 */
	public static final String NOT_ENOUGH_MONEY_MESSAGE = "Sorry, ma non hai abbastanza soldi per fare questo..." ;

	/**
	 * The message to show when an unexpected error appears. 
	 */
	public static final String UNEXPECTED_ERROR_MESSAGE = "Sorry, ma c'è stato un errore imprevisto" + Utilities.CARRIAGE_RETURN + "Purtroppo il gioco verrà chiuso ma..." + Utilities.CARRIAGE_RETURN + "Torna presta a belare con noi!" ;
	
	/**
	 * The message to display to allow the user to stop the Match and exit the App.
	 */
	public static final String STOP_GAME_AND_EXIT = "Termina la partita ed esci da JSheepland" ;
	
	/**
	 * The message to show when the App is closed. 
	 */
	public static final String BYE_MESSAGE = "Grazie per avere utilizzato JSheepland.\nTorna presto a belare!\nBeeeeee" ;
	
	/***/
	private PresentationMessages () {}
	
}
