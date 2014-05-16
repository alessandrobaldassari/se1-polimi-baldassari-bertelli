package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;

public class Card 
{

	public static final int MINIMUM_SELLING_PRICE = 1 ;
	public static final int MAXIMUM_SELLING_PRICE = 4 ;
	private final RegionType regionType ;
	private final int id ;
	private final int initialPrice ;
	private Integer sellingPrice ;
	private boolean sellable ;
	
	public Card ( RegionType regionType , int id , int initialPrice ) 
	{
		if ( regionType != null && regionType != RegionType.SHEEPSBURG && id >=0 && initialPrice >= 0 )
		{
			this.regionType = regionType ;
			this.id = id ;
			this.initialPrice = initialPrice ;
			sellingPrice = null ;
			sellable = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	public RegionType getRegionType () 
	{
		return regionType ;
	}
	
	public int getId () 
	{
		return id ;
	}
	
	public int getInitialPrice () 
	{
		return initialPrice ;
	}
	
	public void setSellingPrice ( int sellingPrice ) 
	{
		if ( sellingPrice >= MINIMUM_SELLING_PRICE && sellingPrice <= MAXIMUM_SELLING_PRICE )
			this.sellingPrice = sellingPrice ;
		else
			throw new IllegalArgumentException () ;
	}
	
	public int getSellingPrice () throws NotSellableException , SellingPriceNotSetException 
	{
		if ( sellable )
		{
			if ( sellingPrice != null )
				return sellingPrice ;
			else
				throw new SellingPriceNotSetException () ;
		}
		else
			throw new NotSellableException () ;
	}
	
	public void setSellable ( boolean sellable )
	{
		this.sellable = sellable ;
	}
	
	public boolean isSellable ()
	{
		return sellable ;
	}
	
	class NotSellableException extends Exception {}
	
	class SellingPriceNotSetException extends Exception {}
	
}
