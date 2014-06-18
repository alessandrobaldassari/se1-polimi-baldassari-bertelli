package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;

/***/
public class MoveSelection implements Serializable
{

	/***/
	private GameMoveType selectedType ;
	
	/***/
	private Iterable < Serializable > params ;
	
	/***/
	public MoveSelection ( GameMoveType selectedType , Iterable < Serializable > params ) 
	{
		this.selectedType = selectedType ;
		this.params = params ;
	}
	
	public GameMoveType getSelectedType () 
	{
		return selectedType ;
	}
	
	public Iterable < Serializable > getParams () 
	{
		return params ;
	}
	
}
