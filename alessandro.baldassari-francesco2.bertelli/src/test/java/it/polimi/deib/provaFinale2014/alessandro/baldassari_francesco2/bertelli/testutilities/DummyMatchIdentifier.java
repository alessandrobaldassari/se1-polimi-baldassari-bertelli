package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;

public class DummyMatchIdentifier implements ObjectIdentifier<Match>{

	public DummyMatchIdentifier() {
		// TODO Auto-generated constructor stub
	}

	public boolean isEqualsTo(ObjectIdentifier<Match> otherObject) {
		// TODO Auto-generated method stub
		return true;
	}
}
