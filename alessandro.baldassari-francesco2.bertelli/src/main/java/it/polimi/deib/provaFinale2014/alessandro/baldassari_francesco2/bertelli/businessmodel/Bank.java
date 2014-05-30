package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class models the Component of the Game which is the Bank, a background element
 * that contains the not yet selled cards, the not placed fences and the reserve of money.
 */
public class Bank 
{
	
	// ATTRIBUTES
	
	/**
	 * The initial money amount, total. 
	 * It's a business rule. 
	 */
	public static final int INITIAL_MONEY_RESERVE = 80 ;
	
	/**
	 * The total number of non final fences.
	 * It's a business rule. 
	 */
	public static final int NON_FINAL_FENCE_NUMBER = 20 ;
	
	/**
	 * The number of final fences initially in the Bank.
	 * It's a business rule. 
	 */
	public static final int FINAL_FENCE_NUMBER = 12 ;
	
	/**
	 * The container for the initial Card objects. 
	 */
	private final Map < RegionType , Card > initialCards ;
	
	/**
	 * The container for the non initial Card Objects. 
	 */
	private final Map < RegionType , Stack < SellableCard > > cards ;
	
	/**
	 * The list of Fence objects this Bank holds. 
	 */
	private final List < Fence > fences ;
	
	/**
	 * The amount of money this Bank holds at any moment in time. 
	 */
	private int moneyReserve ;
	
	// METHODS
	
	/**
	 * @param initialMoneyReserve the initial amount of money this Bank holds.
	 * @param fences the list of Fences this Bank will manage.
	 * @param initCards the initCard objects this Bank will manage.
	 * @param otherCards the sellable Card objects this Bank will manage.
	 * @throws IllegalArgumentException if the fences or initCards or otherCards parameter
	 *         is null or the initialMoneyReserve is < 0
	 */
	Bank ( int initialMoneyReserve , Iterable < Fence > fences , Iterable < Card > initCards , Iterable < SellableCard > otherCards ) 
	{
		if ( fences != null && initCards != null && otherCards != null && initialMoneyReserve >= 0 )
		{
			moneyReserve = initialMoneyReserve ;
			initialCards = new LinkedHashMap < RegionType , Card > ( RegionType.values ().length )  ;
			cards = new LinkedHashMap < RegionType , Stack < SellableCard > > ( RegionType.values().length ) ;
			this.fences = new ArrayList < Fence > () ;
			for ( Card card : initCards )
				this.initialCards.put ( card.getRegionType() , card ) ;
			for ( RegionType regionType : RegionType.values() )
				if ( regionType != RegionType.SHEEPSBURG )
					cards.put ( regionType , new Stack < SellableCard > () ) ;
			for ( SellableCard card : otherCards )
				cards.get ( card.getRegionType () ).push ( card ) ;
			for ( Fence fence : fences )
				this.fences.add ( fence ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter property for the moneyReserve property.
	 * 
	 * @return the moneyReserve property.
	 */
	public int getMoneyReserve () 
	{
		return moneyReserve ;
	}
	
	/**
	 * Add to this Bank the specified amount of money.
	 * 
	 * @param amount the money to add to this Bank.
	 * @throws IllegalArgumentException if the parameter is <= 0.
	 */
	public void receiveMoney ( int amount ) 
	{
		if ( amount > 0 )
			moneyReserve = moneyReserve + amount ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Querying method to know if this Bank holds a Fence of the type which is specified
	 * by the parameter.
	 * 
	 * @param fenceType the intersted type 
	 * @return true if this Bank has a Fence whose type is fenceType, else false.
	 * @throws IllegalArgumentException if the fenceType parameter is null.
	 */
	public boolean hasAFenceOfThisType ( FenceType fenceType ) 
	{
		boolean res ;
		if ( fenceType != null )
		{
			if ( fenceType == FenceType.FINAL )
			{
				if ( fences.get ( fences.size () - 1 ).isFinal() )
					res = true ;
				else
					res = false ;
			}
			else
			{
				if ( fences.get ( 0 ).isFinal () == false )
					res = true ;
				else
					res = false ;
			}
		}
		else 
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Returns a Fence object of the fenceType parameter type ( if it exists in this Bank ).
	 * 
	 * @param fenceType the type of Fence the client wants.
	 * @return a fence of the specified type if it exists in this Bank.
	 * @throws NoMoreFenceOfThisTypeException if this Bank does not have a Fence of the type
	 * 	       specified by the parameter.
	 * @throws IllegalArgumentExeption if the fenceType parameter is null.
	 */
	public Fence getAFence ( FenceType fenceType ) throws NoMoreFenceOfThisTypeException 
	{
		Fence res ;
		if ( fenceType == null )
			throw new IllegalArgumentException();
		if ( fences.isEmpty () == false )
		{
			if ( fenceType == FenceType.NON_FINAL )
			{
				res = fences.get(0);
				if( ! res.isFinal() )
					fences.remove(0);
				else
					throw new NoMoreFenceOfThisTypeException ( fenceType ) ;
			}
			else{
				res = fences.get( fences.size () - 1 ) ;
				if ( res.isFinal () ) 
					fences.remove( fences.size() - 1 );
				else	
					throw new NoMoreFenceOfThisTypeException ( fenceType );
			} 
		}
		else
			throw new NoMoreFenceOfThisTypeException ( fenceType );
		return res ;
	}

	/**
	 * Return the initial Card of the specified type.
	 * 
	 * @return the Initial Card of the type specified by the parameter.
	 * @throws NoMoreCardOfThisTypeException if this Bank does not contain an Initial Card
	 *         of type specified by the parameter.
	 */
	public Card takeInitialCard ( RegionType regionType ) throws NoMoreCardOfThisTypeException 
	{
		Card res ;
		if ( initialCards.containsKey ( regionType ) )
			res = initialCards.remove ( regionType ) ;
		else 
			throw new NoMoreCardOfThisTypeException ( regionType ) ;
		return res ;
	}
	
	/**
	 * Return the price of the next Card sellable of type specified by the parameter.
	 * 
	 * @param regionType the type of the interested Card.
	 * @return the price of the interested Card.
	 * @throws NoMoreCardOfThisTypeException if there is no more Cards which type is 
	 *         the one specified by the parameter.
	 */
	public int getPeekCardPrice ( RegionType regionType ) throws NoMoreCardOfThisTypeException 
	{
		int res ;
		if ( cards.get ( regionType ).isEmpty () == false )
		{
			res = cards.get ( regionType ).peek ().getInitialPrice () ;
		}
		else
			throw new NoMoreCardOfThisTypeException ( regionType ) ;
		return res ;
	}
	
	/**
	 * Sell a Card to a User.
	 * 
	 * @param price the price of the Card that the buying User has to pay.
	 * @param regionType the type of Region the buying Card is.
	 * @throws CardPriceNotRightException if the User supply the wrong price value.
	 * @throws NoMoreCardOfThisTypeException if the User wants to buy a Card whose type
	 *         has no more representant in this Bank.
	 * @throws IllegalArgumentException if the regionType parameter is null or equals
	 *         to Sheepsburg. 
	 */
	public SellableCard sellACard ( int price , RegionType regionType ) throws CardPriceNotRightException, NoMoreCardOfThisTypeException 
	{
		SellableCard res ;
		if ( regionType != null && regionType != RegionType.SHEEPSBURG )
		{
			if ( cards.get ( regionType ).isEmpty () == false )
			{ 
				res = cards.get ( regionType ).pop () ;
				if ( price != res.getInitialPrice() )
					throw new CardPriceNotRightException ( price , res.getInitialPrice() ) ;
			}
			else
				throw new NoMoreCardOfThisTypeException ( regionType ) ;
		}
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	// INNER CLASSES 
	
	// EXCEPTIONS
	
	/**
	 * This class models the situation where a User wants to buy a Card whose type
	 * has no more representant in a Bank.
	 */
	public class NoMoreFenceOfThisTypeException extends Exception 
	{
		
		/**
		 * The Fence type a User wants to buy.
		 */
		private FenceType type ;
		
		/**
		 * @param type the Fence type a User wants to buy.
		 * @throws IllegalArgumentException if the type parameter is null. 
		 */
		private NoMoreFenceOfThisTypeException ( FenceType type ) 
		{
			if ( type != null )
				this.type = type ;
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * Getter method for the type property.
		 * 
		 * @return the type property. 
		 */
		public FenceType getFenceType () 
		{
			return type ;
		}
		
	}

	/**
	 * This class models the situation where a User wants to buy a Card whose type
	 * has no more representant in a Bank.
	 */
	public class NoMoreCardOfThisTypeException extends Exception 
	{
		
		/**
		 * The type of the Region of the Card a User wants to buy. 
		 */
		private RegionType regionType ;
		
		/**
		 * @param regionType the type of the Region of the Card a User wants to buy. 
		 * @throws IllegalArgumentException if the regionType parameter is null
		 */
		private NoMoreCardOfThisTypeException ( RegionType regionType ) 
		{
			if ( regionType != null && regionType != RegionType.SHEEPSBURG )
				this.regionType = regionType ;
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * Getter method for the regionType property.
		 * 
		 * @return the regionType property. 
		 */
		public RegionType getRegionType () 
		{
			return regionType ;
		}
		
	}
	
	/**
	 * This class models the situation where a User try to buy a Card but does not 
	 * supply the right amount of money for it. 
	 */
	public class CardPriceNotRightException extends Exception 
	{
		
		/**
		 * The amount of money a User try to pay for a Card. 
		 */
		private int proposedPrice ;
		
		/**
		 * The amount of money that is required by the Bank to sell a Card. 
		 */
		private int rightPrice ;
		
		/**
		 * @param priceProposed the amount of money a User try to pay for a Card. 
		 * @param rightPrice the amount of money that is required by the Bank to sell a Card. 
		 * @throws IllegalArgumentException if the priceProposed or the rightPrice parameter is 
		 * <= 0 or the priceProposed and rightPrice parameters are equals.
		 */
		private CardPriceNotRightException ( int priceProposed , int rightPrice ) 
		{
			if ( priceProposed > 0 && rightPrice > 0 && priceProposed != rightPrice )
			{
				this.proposedPrice = priceProposed ;
				this.rightPrice = rightPrice ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		/**
		 * Getter property for the proposedPrice property.
		 * 
		 * @return the proposedPrice property. 
		 */
		public int getProposedPrice () 
		{
			return proposedPrice ;
		}
		
		/**
		 * Getter property for the rightPrice property.
		 * 
		 * @return the rightPrice property.
		 */
		public int getRightPrice () 
		{
			return rightPrice ;
		}
		
	}
	
}
