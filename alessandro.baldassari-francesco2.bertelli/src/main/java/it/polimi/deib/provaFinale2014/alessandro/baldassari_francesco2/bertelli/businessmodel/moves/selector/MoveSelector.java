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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveExecutor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This class is the component that allows a User ( also a remote one ) to select a move during the game.
 * It provides all the info the User needs to decide which moves he wants to do and encapsulates the logic
 * that tells what moves a User can do and what moves can not do. 
 */
public class MoveSelector implements Serializable
{
	
	// ATTRIBUTES.

	/**
	 * A Map that, for each move, says if it is allowed or not. 
	 */
	private Map < GameMoveType , Boolean > movesAllowed ;
	
	/**
	 * The Sheperd associated with this selection. 
	 */
	private Sheperd associatedSheperd ;

	/**
	 * The available money of the current Sheperd. 
	 */
	private int availableMoney ;
	
	/**
	 * The selection done by the User. 
	 */
	private MoveSelection selection ;

	/**
	 * The Roads available for the MoveSheperd move. 
	 */
	private Collection < Road > availableRoadsForMoveSheperd ;
	
	/**
	 * The Regions available for the MoveSheep move. 
	 */
	private Collection < Region > availableRegionForMoveSheep ;
	
	/**
	 * A Map containing, for each RegionType available for selection, the price of the associated Card that can be buyed by the Bank. 
	 */
	private Map < RegionType , Integer > availableRegionsForBuyCard ;
	
	/**
	 * The Regions available for the Mate move. 
	 */
	private Collection < Region > availableRegionsForMate ;
	
	/**
	 * The Regions available for the Breakdown move. 
	 */
	private Collection < Region > availableRegionsForBreakdown ;
	
	// METHODS.
	
	/**
	 * @param associatedSheperd the Sheperd the User has choosen for this turn.
	 * @throws WorkflowException 
	 * @throws WrongStateMethodCallException 
	 */
	public MoveSelector ( Sheperd associatedSheperd ) throws WorkflowException  
	{
		if ( associatedSheperd != null )
		{
			try 
			{
				movesAllowed = new HashMap<GameMoveType, Boolean> ( GameMoveType.values().length );
				this.associatedSheperd = associatedSheperd ;
				availableMoney = associatedSheperd.getOwner().getMoney();
				selection = null ;
				availableRoadsForMoveSheperd = null ;
				availableRegionForMoveSheep = null ;
				availableRegionsForBuyCard = null ;
				availableRegionsForMate = null ;
				availableRegionsForBreakdown = null ;
			} 
			catch (WrongStateMethodCallException e)
			{
				throw new WorkflowException ( e , Utilities.EMPTY_STRING ) ;
			}
		}
		else
			throw new IllegalArgumentException ( "MOVE_SELECTOR - <INIT> :" ) ;
	}
	
	/**
	 * 
	 */
	public void setMovesAllowed ( MoveExecutor exec ) 
	{
		movesAllowed.clear();
		movesAllowed.put ( GameMoveType.BREAK_DOWN , ! availableRegionsForBreakdown.isEmpty()  && exec.canBreakdown()) ;
		movesAllowed.put ( GameMoveType.BUY_CARD , ! availableRegionsForBuyCard.isEmpty() && exec.canBuyCard()  ) ;
		movesAllowed.put ( GameMoveType.MATE , ! availableRegionsForMate.isEmpty() && exec.canMate() ) ;
		movesAllowed.put ( GameMoveType.MOVE_SHEEP , ! availableRegionForMoveSheep.isEmpty() && exec.canMoveSheep() ) ;
		movesAllowed.put ( GameMoveType.MOVE_SHEPERD , ! availableRoadsForMoveSheperd.isEmpty() && exec.canMoveSheperd() ) ;
	}
	
	/**
	 * Setter method for the availableMoney field.
	 * 
	 * @param availableMoney the value for the availableMoney field.
	 */
	public void setAvailableMoney ( int availableMoney )
	{
		this.availableMoney = availableMoney ;
	}
	
	/**
	 * Getter for the availableMoney field.
	 * 
	 * @return the availableMoney field.
	 */
	public int getAvailableMoney () 
	{
		return availableMoney ;
	}
	
	/**
	 * Getter method for the associatedSheperd field.
	 * 
	 * @return the associatedSheperd field.
	 */
	public Sheperd getAssociatedSheperd () 
	{
		return associatedSheperd ;
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
	
	public Iterable < GameMoveType > getAvailableMoves ()
	{
		Collection < GameMoveType > res ;
		res = new ArrayList<GameMoveType> () ;
		for ( GameMoveType g : GameMoveType.values () )
			if ( isMoveAllowed(g) )
				res.add(g);
		return res ;
	}
	
	/***/
	public boolean isMoveAllowed ( GameMoveType g )
	{
		boolean res ;
		if ( g != null )
			res = movesAllowed.get ( g ) ;
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
			if ( isMoveAllowed ( GameMoveType.BREAK_DOWN ) && availableRegionsForBreakdown.contains ( animalToBreak.getPosition() ) )
				selection = new MoveSelection  ( GameMoveType.BREAK_DOWN , Collections.<Serializable>singleton ( animalToBreak ) ) ;
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
				 availableRegionForMoveSheep.contains ( ovineDestinationRegion ) )
			{
				params = new ArrayList < Serializable > ( 2 ) ;
				params.add ( movingOvine ) ;
				params.add ( ovineDestinationRegion ) ;
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
