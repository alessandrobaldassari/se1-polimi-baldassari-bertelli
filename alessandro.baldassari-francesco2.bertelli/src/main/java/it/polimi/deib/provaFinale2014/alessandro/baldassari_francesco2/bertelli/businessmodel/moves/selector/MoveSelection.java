package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

/**
 * This class describes a MoveSelection done by a User. 
 */
public class MoveSelection implements Serializable
{

	// ATTRIBUTES
	
	/**
	 * The type of the selected move. 
	 */
	private GameMoveType selectedType ;
	
	/**
	 * The parameters to perform the selected move. 
	 */
	private Collection < Serializable > params ;
	
	// METHODS
	
	/**
	 * @param selectedType the value for the selectedType field.
	 * @param params the value for the params property. 
	 */
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
	
	/**
	 * Getter method for the params field.
	 * 
	 * @return the params field.
	 */
	public Iterable < Serializable > getParams () 
	{
		return params ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
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
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
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
