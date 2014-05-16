package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Bank 
{

	private Map < RegionType , Stack < Card > > cards ;
	private List < Fence > fences ;
	private int moneyReserve ;
	
	public Bank ( int initialMoneyReserve , Iterable < Fence > fences , Iterable < Card > cards ) 
	{
		if ( initialMoneyReserve >= 0 )
		{
			moneyReserve = initialMoneyReserve ;
			this.cards = new LinkedHashMap < RegionType , Stack < Card > > ( RegionType.values().length ) ;
			this.fences = new ArrayList < Fence > () ;
			for ( RegionType regionType : RegionType.values() )
				this.cards.put ( regionType , new Stack < Card > () ) ;
			this.cards.remove ( RegionType.SHEEPSBURG ) ;
			for ( Fence fence : fences )
				this.fences.add ( fence ) ;
			for ( Card card : cards )
				this.cards.get ( card.getRegionType () ).push ( card ) ;
		}
	}
	
	public int getMoneyReserve () 
	{
		return moneyReserve ;
	}
	
	public Fence getAFence ( FenceType fenceType ) throws NoMoreFenceOfThisTypeException 
	{
		Fence res ;
		if ( fenceType == FenceType.NON_FINAL )
			res = fences.get ( 0 ) ;
		else
			res = fences.get ( fences.size () - 1 ) ;
		if ( res.isFinal() && fenceType == FenceType.FINAL )
		{
			if ( fenceType == FenceType.NON_FINAL ) 
				fences.remove ( 0 ) ;
			else
				fences.remove ( fences.size () - 1 ) ;
		}
		else
			throw new NoMoreFenceOfThisTypeException ( fenceType ) ;
		return res ;
	}

	public Card sellACard ( int price , RegionType regionType ) throws CardPriceNotRightException, NoMoreCardOfThisTypeException 
	{
		Card res ;
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
	
	class NoMoreFenceOfThisTypeException extends Exception 
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

	class NoMoreCardOfThisTypeException extends Exception 
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
	
	class CardPriceNotRightException extends Exception 
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
