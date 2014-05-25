package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler;

import java.io.IOException;

public interface ClientHandler 
{

	public String requestName () throws IOException ;

	public void dispose () throws IOException ;
	
}
