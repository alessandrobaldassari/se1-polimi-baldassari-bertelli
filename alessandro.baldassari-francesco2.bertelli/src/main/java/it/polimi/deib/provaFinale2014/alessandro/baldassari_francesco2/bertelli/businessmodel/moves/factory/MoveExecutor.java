package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.factory;

import java.io.Serializable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.BreakDown;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.BuyCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.Mate;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveNotAllowedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveSheep;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveSheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

/***/
public class MoveExecutor implements Serializable
{

	/***/
	private final TurnNumberClock clockSource ;
	
	/***/
	private GameMoveType lastMove ;
	
	/***/
	private byte numberOfMovesDone ;
	
	/***/
	private boolean sheperdMoved ;
	
	/***/
	private LambEvolver lambEvolver ;
	
	/***/
	private Sheperd sheperd ;
	
	/***/
	public Sheperd getAssociatedSheperd ()
	{
		return sheperd;
	}
	
	/***/
	protected MoveExecutor ( Sheperd sheperd , TurnNumberClock clockSource , LambEvolver lambEvolver ) 
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
	public static MoveExecutor newInstance ( Sheperd s , TurnNumberClock clockSource , LambEvolver lambEvolver ) 
	{
		MoveExecutor res ;
		if ( s != null && clockSource != null && lambEvolver != null )
			res = new MoveExecutor ( s , clockSource , lambEvolver ) ;
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/***/
	public void executeBreakdown ( Match match , Animal animalToBreak ) throws MoveNotAllowedException
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new MoveNotAllowedException ( "" ) ;
		else 
			if ( lastMove == null || lastMove != GameMoveType.BREAK_DOWN )
			{
				new BreakDown ( sheperd , animalToBreak ).execute ( match ) ;
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.BREAK_DOWN ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ; 
	} 
	
	/***/
	public void executeBuyCard ( Match match , RegionType buyingCardType ) throws MoveNotAllowedException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new MoveNotAllowedException ( "" ) ; 
		else 
			if ( lastMove == null || lastMove != GameMoveType.BUY_CARD )
			{
				new BuyCard ( sheperd , buyingCardType ).execute ( match ) ;
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.BUY_CARD ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ; 
	}
	
	/***/
	public void executeMate ( Match match , Region whereMate ) throws MoveNotAllowedException  
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new MoveNotAllowedException ( "THE " ) ; 
		else  
			if ( lastMove == null || lastMove != GameMoveType.BUY_CARD )
			{
				new Mate ( clockSource , lambEvolver , sheperd , whereMate ).execute ( match ); ;
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.MATE ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ; 
	}
	
	/***/
	public void executeMoveSheep ( Match match , Ovine movingOvine , Region ovineDestinationRegion ) throws MoveNotAllowedException 
	{
		if ( numberOfMovesDone == 2 && sheperdMoved == false )
			throw new MoveNotAllowedException ( "" ) ; 
		else  
			if ( lastMove == null || lastMove != GameMoveType.BUY_CARD )
			{
				new MoveSheep ( sheperd , movingOvine , ovineDestinationRegion ).execute ( match ) ;
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.MATE ;
			}
			else
				throw new MoveNotAllowedException ( "" ) ; 
		}
	
	/***/
	public void executeMoveSheperd ( Match match , Road roadWhereGo ) throws MoveNotAllowedException 
	{
		new MoveSheperd ( sheperd , roadWhereGo ).execute ( match ); 
		sheperdMoved = true ;
		numberOfMovesDone ++ ;
	}
	
}
