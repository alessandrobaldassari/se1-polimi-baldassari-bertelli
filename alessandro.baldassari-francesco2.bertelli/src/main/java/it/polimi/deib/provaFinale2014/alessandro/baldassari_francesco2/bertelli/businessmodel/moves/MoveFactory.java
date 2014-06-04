package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/***/
public class MoveFactory implements Serializable
{

	/***/
	private final transient TurnNumberClock clockSource ;
	
	/***/
	private Move lastMove ;
	
	/***/
	private byte numberOfMovesDone ;
	
	/***/
	private boolean sheperdMoved ;
	
	/***/
	private transient LambEvolver lambEvolver ;
	
	/***/
	private Sheperd sheperd ;
	
	/***/
	MoveFactory ( Sheperd sheperd , TurnNumberClock clockSource , LambEvolver lambEvolver ) 
	{
		if ( sheperd != null && clockSource != null && lambEvolver != null )
		{
			this.sheperd = sheperd ;
			this.clockSource = clockSource ;
			this.lambEvolver = lambEvolver ;
			lastMove = null ;
			numberOfMovesDone = 0 ;
			sheperdMoved = true ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public static MoveFactory newInstance ( Sheperd s , TurnNumberClock clockSource , LambEvolver lambEvolver ) 
	{
		MoveFactory res ;
		if ( s != null && clockSource != null && lambEvolver != null )
			res = new MoveFactory ( s , clockSource , lambEvolver ) ;
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/***/
	public GameMove newBreakDownMove ( Animal animalToBreak ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ;
		else 
			if ( lastMove == null || lastMove != Move.BREAK_DOWN )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.BREAK_DOWN ;
				return new BreakDown ( sheperd , animalToBreak ) ;
			}
			else
				throw new CannotDoThisMoveException () ;
	} 
	
	/***/
	public GameMove newBuyCard ( RegionType buyingCardType ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else 
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.BUY_CARD ;
				return new BuyCard ( sheperd , buyingCardType ) ;
			}
			else
				throw new CannotDoThisMoveException () ; 
	}
	
	/***/
	public GameMove newMate ( Region whereMate ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else  
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.MATE ;
				return new Mate ( clockSource , lambEvolver , sheperd , whereMate ) ;
			}
			else
				throw new CannotDoThisMoveException () ; 
	}
	
	/***/
	public GameMove newMoveSheep ( Ovine movingOvine , Region ovineDestinationRegion ) throws CannotDoThisMoveException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new CannotDoThisMoveException () ; 
		else  
			if ( lastMove == null || lastMove != Move.BUY_CARD )
			{
				numberOfMovesDone ++ ;
				lastMove = Move.MATE ;
				return new MoveSheep ( sheperd , movingOvine , ovineDestinationRegion ) ;
			}
			else
				throw new CannotDoThisMoveException () ;  
		}
	
	/***/
	public GameMove newMoveSheperd ( Road roadWhereGo ) throws CannotDoThisMoveException
	{
		sheperdMoved = true ;
		numberOfMovesDone ++ ;
		return new MoveSheperd ( sheperd , roadWhereGo ) ;
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
