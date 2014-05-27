package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Animal;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;

public class TwoPlayersMatchMoveFactory extends MoveFactory 
{

	private Sheperd choosenSheperd ;
	
	public TwoPlayersMatchMoveFactory ( Sheperd choosenSheperd ) 
	{
		super () ;
		if ( choosenSheperd != null )
			this.choosenSheperd = choosenSheperd ;
		else
			throw new IllegalArgumentException () ;
	}
	
	private boolean sheperdCorrect ( Sheperd otherSh ) 
	{
		return otherSh.equals ( choosenSheperd ) ;
	}
	
	@Override
	public GameMove newBreakDownMove ( Sheperd breaker , Animal animalToBreak ) throws CannotDoThisMoveException 
	{
		if ( sheperdCorrect ( breaker ) )
			return super.newBreakDownMove ( breaker , animalToBreak ) ;
		else
			throw new CannotDoThisMoveException () ;
	}
	
	@Override
	public GameMove newBuyCard ( Sheperd buyer , RegionType buyingCardType ) throws CannotDoThisMoveException 
	{
		if ( sheperdCorrect ( buyer ) )
			return super.newBuyCard ( buyer , buyingCardType ) ;
		else
			throw new CannotDoThisMoveException () ;
	}
	
	@Override
	public GameMove newMate ( Sheperd theOneWhoWantsTheMate , Region whereMate ) throws CannotDoThisMoveException 
	{
		if ( sheperdCorrect ( theOneWhoWantsTheMate ) )
			return super.newMate ( theOneWhoWantsTheMate , whereMate ) ;
		else
			throw new CannotDoThisMoveException () ;
	}
	
	@Override
	public GameMove newMoveSheep ( Sheperd moverSheperd , Ovine movingOvine , Region ovineDestinationRegion ) throws CannotDoThisMoveException 
	{
		if ( sheperdCorrect ( moverSheperd ) )
		{
			return super.newMoveSheep ( moverSheperd , movingOvine , ovineDestinationRegion ) ;
		}
		else
			throw new CannotDoThisMoveException () ;
	}
	
	public GameMove newMoveSheperd ( Sheperd sheperdToMove , Road roadWhereGo ) throws CannotDoThisMoveException
	{
		if ( sheperdCorrect ( sheperdToMove ) ) 
		{
			return super.newMoveSheperd ( sheperdToMove , roadWhereGo ) ;
		}
		else 
			throw new CannotDoThisMoveException () ;
	}
	
}
