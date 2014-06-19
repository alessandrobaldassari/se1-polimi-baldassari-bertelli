package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

/***/
public class MoveSelection implements Serializable
{

	/***/
	private GameMoveType selectedType ;
	
	/***/
	private Collection < Serializable > params ;
	
	/***/
	public MoveSelection ( GameMoveType selectedType , Iterable < Serializable > params ) 
	{ 
		if ( selectedType != null && params != null )
		{
			this.selectedType = selectedType ;
			this.params = Collections.unmodifiableCollection ( CollectionsUtilities.newCollectionFromIterable ( params ) ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter method for the selectedType property.
	 * 
	 * @return the selectedType property.
	 */
	public GameMoveType getSelectedType () 
	{
		return selectedType ;
	}
	
	/***/
	public Iterable < Serializable > getParams () 
	{
		return params ;
	}
	
	@Override
	public boolean equals ( Object obj ) 
	{
		MoveSelection other ;
		boolean res ;
		if ( obj instanceof MoveSelection )
		{
			other = ( MoveSelection ) obj ;
			if ( selectedType == other.getSelectedType () )
				res = CollectionsUtilities.compareIterable ( params , other.getParams () ) ;
			else
				res = false ;
		}
		else
			res = false ;
		return res ;
	}
	
	@Override
	public String toString () 
	{
		StringBuffer s ;
		s = new StringBuffer () ;
		s.append ( "MoveSelection" ) ;
		s.append ( "\n" ) ;
		s.append ( "Selected Type : " ) ;
		s.append ( selectedType ) ;
		s.append ( "\n" ) ;
		s.append ( "Parameters : " ) ;
		s.append ( params ) ;
		return s.toString () ;
	}
	
}
