package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Bank 
{
	
	// ATTRIBUTES

	/***/
	private static final int INITIAL_MONEY_RESERVE = 80 ;
	
	/***/
	private static final int NON_FINAL_FENCE_NUMBER = 20 ;
	
	/***/
	private static final int FINAL_FENCE_NUMBER = 12 ;
	
	/***/
	private static final int NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE = 5 ;
	
	/***/
	private final Map < RegionType , Card > initialCards ;
	
	/***/
	private final Map < RegionType , Stack < SellableCard > > cards ;
	
	/***/
	private final List < Fence > fences ;
	
	/***/
	private int moneyReserve ;
	
	// METHODS
	
	public Bank ( int initialMoneyReserve , Iterable < Fence > fences , Iterable < Card > initCards , Iterable < SellableCard > otherCards ) 
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
	
	public static Bank newInstance () 
	{
		Bank res ;
		Collection < Fence > initFences ;
		Collection < Card > initialCards ;
		Collection < SellableCard > otherCards ;
		byte i ;
		byte counter ;
		initFences = new ArrayList < Fence > ( NON_FINAL_FENCE_NUMBER + FINAL_FENCE_NUMBER ) ;
		for ( i = 0 ; i < NON_FINAL_FENCE_NUMBER ; i ++ )
			initFences.add ( new Fence ( FenceType.NON_FINAL ) ) ;
		for ( i = 0 ; i < FINAL_FENCE_NUMBER ; i ++ )
			initFences.add ( new Fence ( FenceType.FINAL ) ) ;
		initialCards = new LinkedList < Card > () ;
		otherCards = new LinkedList < SellableCard > () ;
		counter = 0 ;
		for ( RegionType type : RegionType.values() )
		{
			if ( type != RegionType.SHEEPSBURG )
			{
				initialCards.add ( new Card ( type , counter ) ) ;
				counter ++ ;
			}
		}
		for ( RegionType type : RegionType.values () )
			if ( type != RegionType.SHEEPSBURG )
			for ( i = NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE-1 ; i >= 0 ; i -- )
			{
					otherCards.add ( new SellableCard ( type , counter , i ) ) ;
					counter ++ ;
			}
		res = new Bank ( INITIAL_MONEY_RESERVE , initFences , initialCards , otherCards ) ;
		return res ;
	}
	
	public int getMoneyReserve () 
	{
		return moneyReserve ;
	}
	
	public void receiveMoney ( int amount ) 
	{
		if ( amount > 0 )
			moneyReserve = moneyReserve + amount ;
		else
			throw new IllegalArgumentException () ;
	}
	
	
	public Fence getAFence ( FenceType fenceType ) throws NoMoreFenceOfThisTypeException, ArrayIndexOutOfBoundsException 
	{
		Fence res ;
		if(fenceType == null)
			throw new IllegalArgumentException();
		if(fences.isEmpty() == false){
			if ( fenceType == FenceType.NON_FINAL ){
				res = fences.get(0);
				if(res.isFinal())
					throw new NoMoreFenceOfThisTypeException ( fenceType ) ;
				else
					fences.remove(0);
			}
			else{
				res = fences.get( fences.size () - 1 ) ;
				if(res.isFinal())
					fences.remove( fences.size() - 1 );
				else	
					throw new NoMoreFenceOfThisTypeException ( fenceType );
			} 
		}
		else
			throw new NoMoreFenceOfThisTypeException ( fenceType );
		return res ;
	}

	public Card takeInitialCard ( RegionType regionType ) 
	{
		Card res ;
		return res = initialCards.remove ( regionType ) ;
	}
	
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
	
	public Card sellACard ( int price , RegionType regionType ) throws CardPriceNotRightException, NoMoreCardOfThisTypeException 
	{
		SellableCard res ;
		if ( regionType != RegionType.SHEEPSBURG )
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
	
	public class NoMoreFenceOfThisTypeException extends Exception 
	{
		
		private FenceType type ;
		
		public NoMoreFenceOfThisTypeException ( FenceType type ) 
		{
			if ( type != null )
				this.type = type ;
			else
				throw new IllegalArgumentException () ;
		}
		
		public FenceType getFenceType () 
		{
			return type ;
		}
		
	}

	public class NoMoreCardOfThisTypeException extends Exception 
	{
		
		private RegionType regionType ;
		
		public NoMoreCardOfThisTypeException ( RegionType regionType ) 
		{
			if ( regionType != null && regionType != RegionType.SHEEPSBURG )
				this.regionType = regionType ;
			else
				throw new IllegalArgumentException () ;
		}
		
		public RegionType getRegionType () 
		{
			return regionType ;
		}
		
	}
	
	public class CardPriceNotRightException extends Exception 
	{
		
		private int proposedPrice ;
		private int rightPrice ;
		
		public CardPriceNotRightException ( int priceProposed , int rightPrice ) 
		{
			if ( priceProposed > 0 && rightPrice > 0 && priceProposed != rightPrice )
			{
				this.proposedPrice = priceProposed ;
				this.rightPrice = rightPrice ;
			}
			else
				throw new IllegalArgumentException () ;
		}
		
		public int getProposedPrice () 
		{
			return proposedPrice ;
		}
		
		public int getRightPrice () 
		{
			return rightPrice ;
		}
		
	}
	
}
