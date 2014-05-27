package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.util.Collection;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

public class MoveFactory 
{

	private Move lastMove ;
	private byte numberOfMovesDone ;
	private boolean sheperdMoved ;
	
	public MoveFactory () 
	{
		lastMove = null ;
		numberOfMovesDone = 0 ;
		sheperdMoved = true ;
	}
	
	public GameMove newBreakDownMove ( Sheperd breaker , Animal animalToBreak ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ;
		else 
			if ( lastMove == null || lastMove != Move.BREAK_DOWN )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.BREAK_DOWN ;
				return new BreakDown ( breaker , animalToBreak ) ;
			}
			else
				throw new CannotDoThisMoveException () ;
	} 
	
	public GameMove newBuyCard ( Sheperd buyer , RegionType buyingCardType ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else 
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.BUY_CARD ;
				return new BuyCard ( buyer , buyingCardType ) ;
			}
			else
				throw new CannotDoThisMoveException () ; 
	}
	
	public GameMove newMate ( Sheperd theOneWhoWantsTheMate , Region whereMate ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else  
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.MATE ;
				return new Mate ( theOneWhoWantsTheMate , whereMate ) ;
			}
			else
				throw new CannotDoThisMoveException () ; 
	}
	
	public GameMove newMoveSheep ( Sheperd moverSheperd , Ovine movingOvine , Region ovineDestinationRegion ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else  
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.MATE ;
				return new MoveSheep ( moverSheperd , movingOvine , ovineDestinationRegion ) ;
			}
			else
				throw new CannotDoThisMoveException () ;  
		}
	
	public GameMove newMoveSheperd ( Sheperd sheperdToMove , Road roadWhereGo ) throws CannotDoThisMoveException
	{
		sheperdMoved = true ;
		numberOfMovesDone ++ ;
		return new MoveSheperd ( sheperdToMove , roadWhereGo ) ;
	}
	
	// ENUMERATIONS
	
	private enum Move 
	{
		
		BREAK_DOWN ,
		
		BUY_CARD ,
		
		MATE ,
		
		MOVE_SHEEP ,
		
		MOVE_SHEPERD 
		
	}
	
}
