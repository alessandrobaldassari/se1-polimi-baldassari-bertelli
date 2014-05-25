package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server;

import java.rmi.Remote;

public interface RMIServer extends Remote 
{

}

class RMIServerImpl implements RMIServer 
{

	RMIServerImpl ( MasterServer masterServer ) {}

}
