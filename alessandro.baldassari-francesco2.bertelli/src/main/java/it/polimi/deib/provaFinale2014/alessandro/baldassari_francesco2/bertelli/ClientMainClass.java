package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.Client;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.RMIClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.SocketClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.ViewPresenter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.cli.CLIController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.GUIController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is the entry class for a Client. 
 */
public final class ClientMainClass 
{

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
		final String SOCKET_COMMUNICATION_PROTOCOL ;
		final String RMI_COMMUNICATION_PROTOCOL ;
		final String CLI_VIEW;
		final String GUI_VIEW ;
		final byte SOCKET_INDEX = 1 ;
		final byte RMI_INDEX = 2 ;
		final byte EXIT_INDEX = 0 ;
		final byte ERROR_INDEX = -1 ;
		final byte CLI_INDEX = 1 ;
		final byte GUI_INDEX = 2 ;
		final BufferedReader bufferedReader ;
		final CommunicationProtocolResponser communicationProtocolResponser ;
		final Client client ;
		final Executor executor ;
		ViewPresenter viewPresenter ;
		int communicationProtocolChoosed ;
		int viewChoosed ;
		SOCKET_COMMUNICATION_PROTOCOL = "SOCKET" ;
		RMI_COMMUNICATION_PROTOCOL = "RMI" ;
		CLI_VIEW = "CLI" ;
		GUI_VIEW = "GUI" ;
		bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) ) ;
		System.out.println ( "Benvenuto in JSheepland." ) ;
		System.out.println ( "Prego, seleziona il metodo di comunicazione che vuoi utilizzare per giocare : " ) ;
		System.out.println ( SOCKET_INDEX + ". " + SOCKET_COMMUNICATION_PROTOCOL ) ;
		System.out.println ( RMI_INDEX + ". " + RMI_COMMUNICATION_PROTOCOL ) ;
		System.out.println ( EXIT_INDEX + ". USCITA" ) ;
		try
		{
			communicationProtocolChoosed = Byte.parseByte ( bufferedReader.readLine ().trim () ) ;
		}
		catch ( NumberFormatException n ) 
		{
			communicationProtocolChoosed = ERROR_INDEX ;
		}
		while ( communicationProtocolChoosed != SOCKET_INDEX && communicationProtocolChoosed != RMI_INDEX && communicationProtocolChoosed != EXIT_INDEX )
		{
			System.err.println ( "Scelta non valida" ) ;
			System.out.println ( "Prego, seleziona il metodo di comunicazione che vuoi utilizzare per giocare : " ) ;
			System.out.println ( SOCKET_INDEX + ". " + SOCKET_COMMUNICATION_PROTOCOL ) ;
			System.out.println ( RMI_INDEX + ". " + RMI_COMMUNICATION_PROTOCOL ) ;
			System.out.println ( EXIT_INDEX + ". USCITA" ) ;
			try
			{
				communicationProtocolChoosed = Byte.parseByte ( bufferedReader.readLine ().trim () ) ;			
			}
			catch ( NumberFormatException n ) 
			{
				communicationProtocolChoosed = ERROR_INDEX ;
			}
		}
		if ( communicationProtocolChoosed == EXIT_INDEX )
		{
			System.out.println ( "Grazie di avere utilizzato JSheepland" ) ;
			System.out.println ( "Arrivederci" ) ;
		}
		else
		{
			System.out.println ( "Prego, seleziona il tipo di interfaccia grafica che vuoi usare per giocare : " ) ;
			System.out.println ( CLI_INDEX + ". " + CLI_VIEW ) ;
			System.out.println ( GUI_INDEX + ". " + GUI_VIEW ) ;
			try 
			{
				viewChoosed = Byte.parseByte ( bufferedReader.readLine ().trim () ) ;
			}
			catch ( NumberFormatException n ) 
			{
				viewChoosed = ERROR_INDEX ;
			}
			while ( viewChoosed != CLI_INDEX && viewChoosed != GUI_INDEX && viewChoosed != ERROR_INDEX )
			{
				System.err.println ( "Scelta non valida" ) ;
				System.out.println ( "Prego, seleziona il tipo di interfaccia grafica che vuoi usare per giocare : " ) ;
				System.out.println ( CLI_INDEX + ". " + CLI_VIEW ) ;
				System.out.println ( GUI_INDEX + ". " + GUI_VIEW ) ;
				try 
				{
					viewChoosed = Byte.parseByte ( bufferedReader.readLine ().trim () ) ;
				}
				catch ( NumberFormatException n ) 
				{
					viewChoosed = ERROR_INDEX ;
				}
			}
			if ( viewChoosed == ERROR_INDEX )
			{
				System.out.println ( "Grazie di avere utilizzato JSheepland" ) ;
				System.out.println ( "Arrivederci" ) ;
			}
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
				} 
				catch ( WriteOncePropertyAlreadSetException e ) 
				{
					e.printStackTrace();
					throw new RuntimeException ( e ) ;
				}
				executor = Executors.newSingleThreadExecutor () ;
				client.openConnection () ;
				executor.execute ( client ) ;
				viewPresenter.startApp () ;
			}
		}
	}
}
