package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/***/
public class MoveSelector implements Serializable
{

	/***/
	private Sheperd associatedSheperd ;

	/***/
	private Map < RegionType , Integer > bankPriceCards ;
	
	/***/
	private MoveSelection selection ;
	
	/***/
	public MoveSelector ( Sheperd associatedSheperd , Map < RegionType , Integer > bankPriceCards ) 
	{
		if ( associatedSheperd != null && bankPriceCards != null )
		{
			selection = null ;
			this.associatedSheperd = associatedSheperd ;
			this.bankPriceCards = bankPriceCards ;
		}
		else
			throw new IllegalArgumentException ( "MOVE_SELECTOR - <INIT> :" ) ;
	}
	
	public Map < RegionType , Integer > getBankPriceCards () 
	{
		return bankPriceCards ;
	}
	
	/***/
	public Sheperd getAssociatedSheperd () 
	{
		return associatedSheperd ;
	}

	/***/
	public void setSelection ( MoveSelection selection )
	{
		this.selection = selection ;
	}
	
	/***/
	public MoveSelection getSelection () 
	{
		return selection ;
	}
		
	/***/
	public MoveSelection newBreakdown ( Animal animalToBreak )
	{
		return new MoveSelection  ( GameMoveType.BREAK_DOWN , Collections.<Serializable>singleton ( animalToBreak ) ) ;
	} 
	
	/***/
	public MoveSelection newBuyCard ( RegionType buyingCardType ) throws MoveNotAllowedException 
	{
		return new MoveSelection  ( GameMoveType.BUY_CARD , Collections.<Serializable>singleton ( buyingCardType ) ) ;
	} 
	
	/***/
	public MoveSelection newMate ( Region whereMate ) throws MoveNotAllowedException  
	{
		return new MoveSelection  ( GameMoveType.MATE , Collections.<Serializable>singleton ( whereMate ) ) ;
	}
	
	/***/
	public MoveSelection newMoveSheep ( Ovine movingOvine , Region ovineDestinationRegion ) throws MoveNotAllowedException 
	{
		Collection < Serializable > params ;
		params = new ArrayList < Serializable > ( 2 ) ;
		params.add ( movingOvine ) ;
		params.add ( ovineDestinationRegion ) ;
		return new MoveSelection  ( GameMoveType.MOVE_SHEEP , params ) ;	
	}
	
	/***/
	public MoveSelection newMoveSheperd ( Road roadWhereGo ) throws MoveNotAllowedException 
	{
		return new MoveSelection ( GameMoveType.MOVE_SHEPERD , Collections.<Serializable>singleton ( roadWhereGo ) ) ;
	}
	
}
