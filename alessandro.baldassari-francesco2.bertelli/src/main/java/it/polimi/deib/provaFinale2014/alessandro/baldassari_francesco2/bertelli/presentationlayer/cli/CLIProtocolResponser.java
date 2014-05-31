package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.cli;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;

public class CLIProtocolResponser implements CommunicationProtocolResponser
{

	private BufferedReader reader ;
	
	private PrintStream writer ;
	
	public CLIProtocolResponser () 
	{
		reader = new BufferedReader ( new InputStreamReader ( System.in ) ) ;
		writer = System.out ;
	}
	
	@Override
	public String onNameRequest () 
	{
		String res = null ;
		try 
		{
			writer.println ( "Prego, inserisci il nome con cui vuoi giocare : " );
			res = reader.readLine ().trim () ;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return res ;
	}

	@Override
	public Color onSheperdColorRequest ( Iterable < Color > availableColors ) 
	{
		List < Color > colors = CollectionsUtilities.newListFromIterable ( availableColors ) ;
		byte res ;
		byte i ;
		System.out.println ( "Prego, inserisci il numero del colore che vuoi scegliere per il tuo pastore" ) ;
		i = 1 ;
		for ( Color c : colors )
		{
			System.out.println ( i + ". " + c.toString () ) ;
			i ++ ;
		}
		try
		{
			res = Byte.parseByte ( reader.readLine () ) ;
		}
		catch ( NumberFormatException | IOException e ) 
		{
			res = -1 ;
		} 
		while ( res < 1 || res > colors.size () )
		{
			System.err.println ( "Scelta non valida." );
			System.out.println ( "Prego, inserisci il numero del colore che vuoi scegliere per il tuo pastore" ) ;
			i = 1 ;
			for ( Color c : colors )
			{
				System.out.println ( i + ". " + c.toString () ) ;
				i ++ ;
			}
			try
			{
				res = Byte.parseByte ( reader.readLine () ) ;
			}
			catch ( NumberFormatException | IOException e ) 
			{
				res = - 1 ;
			}
		}
		return colors.get ( res - 1 ) ;
	}

	@Override
	public void onMatchWillNotStartNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generationNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseCardsEligibleForSelling() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseSheperdForATurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChoseCardToBuy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameMove onDoMove() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}