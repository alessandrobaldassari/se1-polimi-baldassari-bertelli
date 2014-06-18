package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.CollectionsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/***/
public class MoveSelector implements Serializable
{

	/***/
	private Map < GameMoveType , Boolean > movesAllowedDueToRuntimeRules ;
	
	/***/
	private Sheperd associatedSheperd ;

	/***/
	private int availableMoney ;
	
	/***/
	private MoveSelection selection ;
	
	/***/
	private GameMoveType [] selectedMoves ;

	/***/
	private int lastSelectedIndex ;
	
	/***/
	private Collection < Road > availableRoadsForMoveSheperd ;
	
	/***/
	private Collection < Region > availableRegionForMoveSheep ;
	
	/***/
	private Map < RegionType , Integer > availableRegionsForBuyCard ;
	
	/***/
	private Collection < Region > availableRegionsForMate ;
	
	/***/
	private Collection < Region > availableRegionsForBreakdown ;
	
	/**
	 * @param
	 * @param
	 * @param 
	 */
	public MoveSelector ( Sheperd associatedSheperd ) 
	{
		if ( associatedSheperd != null )
		{
			movesAllowedDueToRuntimeRules = new HashMap<GameMoveType, Boolean> ( GameMoveType.values().length );
			this.associatedSheperd = associatedSheperd ;
			availableMoney = associatedSheperd.getOwner().getMoney();
			selection = null ;
			selectedMoves = new GameMoveType [] { null , null , null } ;
			lastSelectedIndex = -1 ;
			availableRoadsForMoveSheperd = null ;
			availableRegionForMoveSheep = null ;
			availableRegionsForBuyCard = null ;
			availableRegionsForMate = null ;
			availableRegionsForBreakdown = null ;
		}
		else
			throw new IllegalArgumentException ( "MOVE_SELECTOR - <INIT> :" ) ;
	}
	
	/***/
	public void setMovesAllowedDueToRuntimeRules () 
	{
		movesAllowedDueToRuntimeRules.clear();
		movesAllowedDueToRuntimeRules.put ( GameMoveType.BREAK_DOWN , availableRegionsForBreakdown.size() > 0 ) ;
		movesAllowedDueToRuntimeRules.put ( GameMoveType.BUY_CARD , availableRegionsForBuyCard.size() > 0  ) ;
		movesAllowedDueToRuntimeRules.put ( GameMoveType.MATE , availableRegionsForMate.size() > 0  ) ;
		movesAllowedDueToRuntimeRules.put ( GameMoveType.MOVE_SHEEP , availableRegionForMoveSheep.size() > 0 ) ;
		movesAllowedDueToRuntimeRules.put ( GameMoveType.MOVE_SHEPERD , availableRoadsForMoveSheperd.size() > 0 ) ;
	}
	
	public void setAvailableMoney ( int availableMoney )
	{
		this.availableMoney = availableMoney ;
	}
	
	/***/
	public int getAvailableMoney () 
	{
		return availableMoney ;
	}
	
	/***/
	public void setAvailableRoadsForMoveSheperd ( Collection < Road > availableRoadsForMoveSheperd )
	{
		this.availableRoadsForMoveSheperd = availableRoadsForMoveSheperd ;
	}
	
	/***/
	public Iterable < Road > getAvailableRoadsForMoveSheperd () 
	{
		return availableRoadsForMoveSheperd ;
	}
	
	/***/
	public void setAvailableRegionsForMoveSheep ( Collection < Region > availableRegionForMoveSheep ) 
	{
		this.availableRegionForMoveSheep = availableRegionForMoveSheep ;
	}
	
	/***/
	public Iterable < Region > getAvailableRegionsForMoveSheep () 
	{
		return availableRegionForMoveSheep ;
	}
	
	/***/
	public void setAvailableRegionsForBuyCard ( Map < RegionType , Integer > availableRegionsForBuyCard )
	{
		this.availableRegionsForBuyCard = availableRegionsForBuyCard ;
	}
	
	/***/
	public Map < RegionType , Integer > getAvailableRegionsForBuyCard () 
	{
		return availableRegionsForBuyCard ;
	}
	
	/***/
	public void setAvailableRegionsForMate ( Collection < Region > availableRegionsForMate ) 
	{
		this.availableRegionsForMate = availableRegionsForMate ;
	}
	
	/***/
	public Collection < Region > getAvailableRegionsForMate () 
	{
		return availableRegionsForMate ;
	}
	
	/***/
	public void setAvailableRegionsForBreakdown ( Collection < Region > availableRegionsForBreakdown ) 
	{
		this.availableRegionsForBreakdown = availableRegionsForBreakdown ;
	}
	
	/***/
	public Collection < Region > getAvailableRegionsForBreakdown () 
	{
		return availableRegionsForBreakdown ;
	}
	
	/***/
	public boolean isMoveAllowed ( GameMoveType g )
	{
		boolean res ;
		boolean extCond ;
		boolean shepCond ;
		boolean twoCond ;
		boolean dinCond ;
		if ( g != null )
		{
			extCond = movesAllowedDueToRuntimeRules.get ( g ) ;
			shepCond = ( lastSelectedIndex == 1 && CollectionsUtilities.arrayLinearSearchPK ( selectedMoves , GameMoveType.MOVE_SHEPERD )  == -1 ) ; 
			twoCond = ( ( selectedMoves [ 0 ] == g && selectedMoves [ 1 ] == g ) 
					|| ( selectedMoves [ 1 ] == g && selectedMoves [ 2 ] == g ) ||
					( selectedMoves [ 0 ] == g && selectedMoves [ 2 ] == g && selectedMoves [ 1 ] != GameMoveType.MOVE_SHEPERD ) )  ;
			res = extCond && ! ( shepCond || twoCond ) ;
		}
		else
			throw new IllegalArgumentException() ;
		return res ;
	}
	
	/**
	 * @throws MoveNotAllowedException */
	public void selectBreakdown ( Animal animalToBreak ) throws MoveNotAllowedException
	{		
		if ( animalToBreak != null )
		{
			if ( isMoveAllowed ( GameMoveType.BREAK_DOWN ) && availableRegionsForBreakdown.contains ( animalToBreak ) )
			{
				lastSelectedIndex ++ ;
				selectedMoves [ lastSelectedIndex ] = GameMoveType.BREAK_DOWN ;
				selection = new MoveSelection  ( GameMoveType.BREAK_DOWN , Collections.<Serializable>singleton ( animalToBreak ) ) ;
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		}
		else
			throw new IllegalArgumentException();
	}
	
	/***/
	public void selectBuyCard ( RegionType buyingCardType ) throws MoveNotAllowedException 
	{
		if (  buyingCardType != null  )
		{
			if ( isMoveAllowed ( GameMoveType.BUY_CARD ) && availableRegionsForBuyCard.containsKey ( buyingCardType ) )
			{
				lastSelectedIndex ++ ;
				selectedMoves [ lastSelectedIndex ] = GameMoveType.BUY_CARD ;
				selection = new MoveSelection  ( GameMoveType.BUY_CARD , Collections.<Serializable>singleton ( buyingCardType ) ) ;
				
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		}
		else
			throw new IllegalArgumentException();
	} 
	
	/***/
	public void selectMate ( Region whereMate ) throws MoveNotAllowedException  
	{
		if ( whereMate != null  )
		{
			if ( isMoveAllowed ( GameMoveType.MATE ) && availableRegionsForMate.contains ( whereMate ) )
			{
				lastSelectedIndex ++ ;
				selectedMoves [ lastSelectedIndex ] = GameMoveType.MATE ;
				selection = new MoveSelection  ( GameMoveType.MATE , Collections.<Serializable>singleton ( whereMate ) ) ;
				
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		}
		else
			throw new IllegalArgumentException();
	}
	
	/***/
	public void selectMoveSheep ( Ovine movingOvine , Region ovineDestinationRegion ) throws MoveNotAllowedException 
	{
		Collection < Serializable > params ;
		if (  movingOvine != null && ovineDestinationRegion != null )
		{
			if ( isMoveAllowed ( GameMoveType.MOVE_SHEEP ) && 
				 availableRegionForMoveSheep.contains ( ovineDestinationRegion ) 
				 && CollectionsUtilities.contains ( ovineDestinationRegion.getContainedAnimals() , movingOvine ) )
			{
				params = new ArrayList < Serializable > ( 2 ) ;
				params.add ( movingOvine ) ;
				params.add ( ovineDestinationRegion ) ;
				lastSelectedIndex ++ ;
				selectedMoves [ lastSelectedIndex ] = GameMoveType.MOVE_SHEEP ;
				selection = new MoveSelection  ( GameMoveType.MOVE_SHEEP , params ) ;
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		}
		else
			throw new IllegalArgumentException();
	}
	
	/***/
	public void selectMoveSheperd ( Road roadWhereGo ) throws MoveNotAllowedException 
	{
		if ( roadWhereGo != null )
		{
			if ( isMoveAllowed ( GameMoveType.MOVE_SHEPERD ) && availableRoadsForMoveSheperd.contains ( roadWhereGo ) )
			{
				lastSelectedIndex ++ ;
				selectedMoves [ lastSelectedIndex ] = GameMoveType.MOVE_SHEPERD ;
				selection = new MoveSelection  ( GameMoveType.MOVE_SHEPERD , Collections.<Serializable>singleton ( roadWhereGo ) ) ;
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		}
		else
			throw new IllegalArgumentException();
	}
	
	/***/
	public MoveSelection getSelectedMove () throws WrongStateMethodCallException 
	{
		MoveSelection res ;
		if ( selection != null )
			res = selection ;
		else
			throw new WrongStateMethodCallException () ;
		return res ;
	}
	
}
