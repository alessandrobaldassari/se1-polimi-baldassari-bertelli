package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements ClientHandler 
{

	private Socket clientChannel ;
	
	private ObjectInputStream ois ;
	
	private ObjectOutputStream oos ;
	
	public SocketClientHandler ( Socket clientChannel ) throws IOException  
	{ 
		if ( clientChannel != null )
		{
			this.clientChannel = clientChannel ;
			oos = new ObjectOutputStream ( clientChannel.getOutputStream () ) ;
			ois = new ObjectInputStream ( clientChannel.getInputStream () ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public String requestName () throws IOException  
	{
		String res ;
		oos.writeUTF ( SocketProtocolAction.NAME_REQUESTING_REQUEST.toString () ) ;
		oos.flush () ;
		System.out.println ( "Socket Client Handler : Name Request Sent" ) ;
		res = ois.readUTF () ;
		if ( SocketProtocolAction.valueOf ( res ) == SocketProtocolAction.NAME_REQUESTING_RESPONSE )
		{
			res = ois.readUTF () ;
		}
		else
		{
			// ERROR MANAGEMENT STRATEGY
		}
		return res ;
	}
	
	@Override
	public Color requestSheperdColor(Iterable<Color> availableColors) throws IOException {
		String res;
		Color choosedColor = null;
		oos.writeUTF(SocketProtocolAction.SHEPERD_COLOR_REQUESTING_REQUEST.toString());
		oos.flush();
		oos.writeObject(availableColors);
		oos.flush();
		res = ois.readUTF();
		if(SocketProtocolAction.valueOf( res ) == SocketProtocolAction.SHEPERD_COLOR_REQUESTING_RESPONSE){
			try {
				choosedColor = (Color) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//error management strategy
		}
			
		return choosedColor;
		
	}
	
	public void dispose () throws IOException  
	{
		oos.close () ;
		ois.close () ;
		clientChannel.close () ;
	}

	@Override
	public void notifyMatchWillNotStart(String message) throws IOException {
		oos.writeUTF(SocketProtocolAction.MATCH_WILL_NOT_START_NOTIFICATION.toString());
		oos.flush();
		oos.writeUTF(message);
		oos.flush();
		
		
	}

	@Override
	public void requestMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genericNotification(String message) throws IOException {
		oos.writeUTF(SocketProtocolAction.GENERIC_NOTIFICATION_NOTIFICATION.toString());
		oos.flush();
		oos.writeUTF(message);
		oos.flush();
	}

	@Override
	public void chooseCardsEligibleForSelling(Iterable<SellableCard> sellablecards) throws IOException 
	{
		oos.writeUTF(SocketProtocolAction.CHOOSE_CARDS_ELEGIBLE_FOR_SELLING_REQUESTING_REQUEST.toString());
		oos.flush();
		oos.writeObject(sellablecards);
		oos.flush();
		//il giocatore modifica sellableCards e il server deve conscere le modifiche
	}

	
	
}
