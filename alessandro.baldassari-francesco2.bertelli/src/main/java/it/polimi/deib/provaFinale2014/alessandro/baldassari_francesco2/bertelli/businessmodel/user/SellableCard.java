package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;

public class SellableCard extends Card 
{

	// ATTRIBUTES
	
	/**
	 * The minimum selling price for a Card during the Market phase: the value is a game's rule.
	 */
	public static final int MINIMUM_SELLING_PRICE = 1 ;
	
	/**
	 * The maximum selling price for a Card during the Market phase: the value is a game's rule.
	 */
	public static final int MAXIMUM_SELLING_PRICE = 4 ;
	
	/**
	 * The price the Bank requires to sell this Card.
	 */
	private final int initialPrice ;
	
	/**
	 * The price a Player requires to sell this Card ( used when this card belongs to him ).
	 */
	private Integer sellingPrice ;
	
	/**
	 * A flag set by a Player to decide if he wants to sell this Card or not.
	 */
	private boolean sellable ;
	
	// METHODS
	
	/**
	 * @param initialPrice the price the Bank requires to sell this Card. 
	 * @param regionType the Region type represented by this Player.
	 * @param id the UID of this Card
	 * @throws IllegalArgumentException if the initialPrice parameter is <= 0
	 */
	SellableCard ( RegionType type , int id , int initialPrice ) 
	{
		super ( type , id ) ;
		if ( initialPrice > 0 )
		{
			this.initialPrice = initialPrice ;
			sellingPrice = null ;
			sellable = false ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the initial price of this Card.
	 * 
	 * @return the price the Bank requires for sell this Card.
	 */
	public int getInitialPrice () 
	{
		return initialPrice ;
	}
	
	/**
	 * Set the selling price.
	 * 
	 * @param sellingPrice the value the sellingPrice property must be set to.
	 * @throw IllegalArgumentException if the paremeter is not a legal argument ( < of 
	 * 	      MINIMUM_SELLING_PRICE or > of MAXIMUM_SELLING_PRICE ).
	 */
	public void setSellingPrice ( int sellingPrice ) 
	{
		if ( sellingPrice >= MINIMUM_SELLING_PRICE && sellingPrice <= MAXIMUM_SELLING_PRICE )
			this.sellingPrice = sellingPrice ;
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * Getter for the sellingPrice.
	 * 
	 * @return the sellingPrice property.
	 * @throws NotSellableException if the sellable property is set to false.
	 * @throws SellingPriceNotSetException if the sellingPrice property has not been set ( so 
	 * if it is null when this method get called ).
	 */
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
	
	/**
	 * Set the sellable property.
	 * 
	 * @param sellable the new value for the sellable property.
	 */
	public void setSellable ( boolean sellable )
	{
		this.sellable = sellable ;
	}
	
	/**
	 * Getter for the sellable property.
	 * 
	 * @return the value of the sellable property.
	 */
	public boolean isSellable ()
	{
		return sellable ;
	}
	

	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This class model the situation where one try to buy / sell a Card that is not marked
	 * for selling.
	 */
	public class NotSellableException extends Exception 
	{
		
		/***/
		private NotSellableException () 
		{
			super () ;
		}
		
	}
	
	/**
	 * This class model the situation where a Card is going to be sold but it's price has not 
	 * been set.
	 */
	public class SellingPriceNotSetException extends Exception {}
	
}
