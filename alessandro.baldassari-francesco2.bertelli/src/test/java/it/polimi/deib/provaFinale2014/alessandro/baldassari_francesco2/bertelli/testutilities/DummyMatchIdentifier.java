package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;

public class DummyMatchIdentifier implements Identifiable < Match > 
{

	private final int id ;
	
	public DummyMatchIdentifier ( int  id ) 
	{
		this.id = id ;
	}
	
	@Override
	public boolean isEqualsTo ( Identifiable<Match> otherObject ) 
	{
		if ( otherObject instanceof DummyMatchIdentifier )
		return id == ( ( DummyMatchIdentifier ) otherObject ).id ;
		else
			return false ;
	}
	
}
	
