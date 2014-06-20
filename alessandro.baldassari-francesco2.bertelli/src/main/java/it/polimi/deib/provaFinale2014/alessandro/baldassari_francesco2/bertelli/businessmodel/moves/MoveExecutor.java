package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.LambEvolver;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.TurnNumberClock;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WorkflowException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * A component that executes GameMove.
 * It also does minimal controls over which Moves are executed.
 * It is intended to manage just one Player turn ( create one of this object for each turn of a Player ). 
 */
public class MoveExecutor 
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
	
	/**
	 * @param
	 * @param
	 * @param
	 * @throws {@link IllegalArgumentException} 
	 */ 
	protected MoveExecutor ( Sheperd sheperd , TurnNumberClock clockSource , LambEvolver lambEvolver ) 
	{
		if ( sheperd != null && clockSource != null && lambEvolver != null )
		{
			this.sheperd = sheperd ;
			this.clockSource = clockSource ;
			this.lambEvolver = lambEvolver ;
			lastMove = null ;
			numberOfMovesDone = 0 ;
			sheperdMoved = false ;
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
	
	/**
	 * Getter method for the sheperd property. 
	 */
	public Sheperd getAssociatedSheperd ()
	{
		return sheperd;
	}
	
	/**
	 * @throws {@link WorkflowException } 
	 * @throws {@link MoveNotAllowedException } 
	 */
	public void executeBreakdown ( Match match , Animal animalToBreak ) throws MoveNotAllowedException, WorkflowException
	{
		if ( match != null && animalToBreak != null )
			if ( numberOfMovesDone < 3 && ! ( numberOfMovesDone == 2 && sheperdMoved == false ) && lastMove != GameMoveType.BREAK_DOWN )
			{
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.BREAK_DOWN ;
				new BreakDown ( sheperd , animalToBreak ).execute ( match ) ;
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		else
			throw new IllegalArgumentException () ;
	} 
	
	/**
	 * @throws WorkflowException 
	 */
	public void executeBuyCard ( Match match , RegionType buyingCardType ) throws MoveNotAllowedException, WorkflowException 
	{
		if ( numberOfMovesDone < 3 && ! ( numberOfMovesDone == 2 && sheperdMoved == false ) && lastMove != GameMoveType.BUY_CARD  )
		{
			numberOfMovesDone ++ ;
			lastMove = GameMoveType.BUY_CARD ;
			new BuyCard ( sheperd , buyingCardType ).execute ( match ) ;
		}
		else
			throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ; 
	}
	
	/**
	 * @param match
	 * @param whereMate
	 * @throws WorkflowException 
	 * @throws {@link MoveNotAllowedException}  
	 */
	public void executeMate ( Match match , Region whereMate ) throws MoveNotAllowedException, WorkflowException  
	{
		if ( match != null && whereMate != null )
			if ( numberOfMovesDone < 3 && ! ( numberOfMovesDone == 2 && sheperdMoved == false ) && lastMove != GameMoveType.MATE )
			{
				numberOfMovesDone ++ ; 
				lastMove = GameMoveType.MATE ;
				new Mate ( clockSource , lambEvolver , sheperd , whereMate ).execute ( match ); 
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ; 
		else
			throw new IllegalArgumentException () ;
	}
	
	/***/
	public void executeMoveSheep ( Match match , Ovine movingOvine , Region ovineDestinationRegion ) throws MoveNotAllowedException 
	{
		if ( numberOfMovesDone < 3 && ! ( numberOfMovesDone == 2 && sheperdMoved == false ) &&  lastMove != GameMoveType.MOVE_SHEEP )
		{
			numberOfMovesDone ++ ;
			lastMove = GameMoveType.MOVE_SHEEP ;
			new MoveSheep ( sheperd , movingOvine , ovineDestinationRegion ).execute ( match ) ;	
		}
		else
			throw new MoveNotAllowedException ( "Can not do two equals moves sequentially." ) ; 
		}
	
	/**
	 * @throws WrongStateMethodCallException 
	 */
	public void executeMoveSheperd ( Match match , Road roadWhereGo ) throws MoveNotAllowedException, WorkflowException 
	{
		if  ( match != null && roadWhereGo != null )
			if ( numberOfMovesDone < 3 )
			{
				numberOfMovesDone ++ ;
				lastMove = GameMoveType.MOVE_SHEPERD ;
				sheperdMoved = true ;
				new MoveSheperd ( sheperd , roadWhereGo ).execute ( match ); 
			}
			else
				throw new MoveNotAllowedException ( PresentationMessages.MOVE_NOT_ALLOWED_MESSAGE ) ;
		else
			throw new IllegalArgumentException () ;
	}
	
}
