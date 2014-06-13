package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.Client;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.RMIClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.SocketClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.cli.CLIController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.GUIController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphics.GraphicsUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is the entry class for a Client. 
 */
public final class ClientMainClass 
{

	/***/
	final static String SOCKET_COMMUNICATION_PROTOCOL = "SOCKET" ;
	
	/***/
	final static String RMI_COMMUNICATION_PROTOCOL = "RMI" ;
	
	/***/
	final static String CLI_VIEW = "CLI" ;
	
	/***/
	final static String GUI_VIEW = "GUI" ;
	
	/***/
	final static byte SOCKET_INDEX = 1 ;
	
	/***/
	final static byte RMI_INDEX = 2 ;
	
	/***/
	final static byte EXIT_INDEX = 0 ;
	
	/***/
	final static byte ERROR_INDEX = -1 ;
	
	/***/
	final static byte CLI_INDEX = 1 ;
	
	/***/
	final static byte GUI_INDEX = 2 ;
	
	/***/
	static String netModeStr ;
	
	/***/
	static String presMoveStr ;
	
	/***/
	private ClientMainClass () {}
	
	/**
	 * Ask the User which communication mode he wants to use.
	 * When acquired this information, this method create the right client type, the 
	 * right CommunicationProtocolResponser and the right View.
	 * Then leaves the View the control.
	 * 
	 * @param args the CLI arguments
	 * @throws IOException if something goes wrong with the IO Operations
	 */
	public static void main ( String [] args ) throws IOException 
	{
		final BufferedReader in ;
		final PrintStream out ;
		final Client client ;
		final Executor executor ;
		final ViewPresenter viewPresenter ;	
		int communicationProtocolChoosed ;
		int viewChoosed ;
		initializeStrings () ;
		in = new BufferedReader ( new InputStreamReader ( System.in ) ) ;
		out = System.out ;
		communicationProtocolChoosed = GraphicsUtilities.checkedIntInputWithEscape ( SOCKET_INDEX, RMI_INDEX ,EXIT_INDEX , ERROR_INDEX , netModeStr , PresentationMessages.INVALID_CHOOSE_MESSAGE , out , in ) ;
		if ( communicationProtocolChoosed == EXIT_INDEX )
			out.println ( PresentationMessages.BYE_MESSAGE ) ;
		else
		{
			viewChoosed = GraphicsUtilities.checkedIntInputWithEscape( CLI_INDEX, GUI_INDEX ,EXIT_INDEX , ERROR_INDEX , presMoveStr , PresentationMessages.INVALID_CHOOSE_MESSAGE , out , in ) ;
			if ( viewChoosed == ERROR_INDEX )
				out.println ( PresentationMessages.BYE_MESSAGE ) ;
			else
			{
				if ( viewChoosed == CLI_INDEX )
					viewPresenter = new CLIController () ;
				else
					viewPresenter = new GUIController () ;
				if ( communicationProtocolChoosed == SOCKET_INDEX )
					client = new SocketClient ( viewPresenter ) ;
				else
					client = new RMIClient ( viewPresenter ) ;
				try 
				{
					viewPresenter.setClientToTerminate ( client ) ;
					executor = Executors.newSingleThreadExecutor () ;
					client.openConnection () ;
					executor.execute ( client ) ;
					viewPresenter.startApp () ;
				} 
				catch ( WriteOncePropertyAlreadSetException e ) 
				{
					e.printStackTrace();
					throw new RuntimeException ( e ) ;
				}
			}
		}
	}
	
	private static void initializeStrings () 
	{
		netModeStr = "Benvenuto in JSheepland." + Utilities.CARRIAGE_RETURN ;
		netModeStr = netModeStr + "Prego, seleziona il metodo di comunicazione che vuoi utilizzare per giocare " + Utilities.CARRIAGE_RETURN ;
		netModeStr = netModeStr + SOCKET_INDEX + ". " + SOCKET_COMMUNICATION_PROTOCOL + Utilities.CARRIAGE_RETURN ;
		netModeStr = netModeStr + RMI_INDEX + ". " + RMI_COMMUNICATION_PROTOCOL + Utilities.CARRIAGE_RETURN ;
		netModeStr = netModeStr + EXIT_INDEX + ". USCITA." + Utilities.CARRIAGE_RETURN ; 
		presMoveStr = "Prego, seleziona il tipo di interfaccia grafica che vuoi usare per giocare :" + Utilities.CARRIAGE_RETURN ;
		presMoveStr = presMoveStr + CLI_INDEX + ". " + CLI_VIEW + Utilities.CARRIAGE_RETURN ;
		presMoveStr = presMoveStr + GUI_INDEX + ". " + GUI_VIEW + Utilities.CARRIAGE_RETURN ;
		presMoveStr = presMoveStr + EXIT_INDEX + ". USCITA." + Utilities.CARRIAGE_RETURN ; 
		
	}
}
