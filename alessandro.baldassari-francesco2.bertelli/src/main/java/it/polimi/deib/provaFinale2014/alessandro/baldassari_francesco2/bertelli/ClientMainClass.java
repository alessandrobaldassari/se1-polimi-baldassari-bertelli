package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.Client;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.RMIClient;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.SocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is the entry class for a Client. 
 */
public class ClientMainClass 
{

	/**
	 * Ask the User which communication mode he wants to use.
	 * When acquired this information, this method create the right client type
	 * 
	 * @param args the CLI arguments
	 * @throws IOException if something goes wrong with the IO Operations
	 */
	public static void main ( String [] args ) throws IOException 
	{
		final String SOCKET_COMMUNICATION_PROTOCOL ;
		final String RMI_COMMUNICATION_PROTOCOL ;
		final byte SOCKET_INDEX = 1 ;
		final byte RMI_INDEX = 2 ;
		final byte EXIT_INDEX = 0 ;
		final byte ERROR_INDEX = -1 ;
		final BufferedReader bufferedReader ;
		final Client client ;
		final Executor executor ;
		int communicationProtocolChoosed ;
		SOCKET_COMMUNICATION_PROTOCOL = "SOCKET" ;
		RMI_COMMUNICATION_PROTOCOL = "RMI" ;
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
			communicationProtocolChoosed = Byte.parseByte ( bufferedReader.readLine ().trim () ) ;			
		}
		if ( communicationProtocolChoosed == EXIT_INDEX )
		{
			System.out.println ( "Grazie di avere utilizzato JSheepland" ) ;
			System.out.println ( "Arrivederci" ) ;
		}
		else
		{
			if ( communicationProtocolChoosed == SOCKET_INDEX )
				client = new SocketClient () ;
			else
				client = new RMIClient () ;
			executor = Executors.newSingleThreadExecutor () ;
			client.openConnection () ;
			executor.execute ( client ) ;
		
		}
	}
}
