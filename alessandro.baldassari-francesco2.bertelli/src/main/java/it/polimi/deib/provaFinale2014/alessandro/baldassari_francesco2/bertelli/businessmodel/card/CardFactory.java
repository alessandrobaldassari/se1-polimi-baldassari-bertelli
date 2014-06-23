package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card;

import java.util.Collection;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.FactorySupport;

/**
 * This class is a Factory component for the Card entities.
 * It allows to create just the needed cards for each Match that is created, and no more.
 * The only instance of the CardFactory class is exposed from here with a Singleton Method. 
 */
public class CardFactory 
{
	
	// ATTRIBUTES

	/**
	 * The only instance for this class to implements the Singleton pattern. 
	 */
	private static final CardFactory instance = new CardFactory () ;
	
	/**
	 * An object needed to know and manage the Match objects that already created 
	 * their initial cards. 
	 */
	private final FactorySupport < Match > factorySupportInitialCards ;

	/**
	 * An object needed to know and manage the Match objects that already created
	 * their sellable cards. 
	 */
	private final FactorySupport < Match > factorySupportSellableCards ;
	
	// METHODS
	
	/***/
	private CardFactory () 
	{
		factorySupportInitialCards = new FactorySupport < Match > () ;
		factorySupportSellableCards = new FactorySupport < Match > () ;
	}
	
	/**
	 * The singleton method that returns the only instance of this class. 
	 *
	 * @return the only instance of this class.
	 */
	public static CardFactory getInstance () 
	{
		return instance ;
	}
	
	/**
	 * Generated and returns the right initial cards for a given match, whose identity
	 * is passed by parameter.
	 * 
	 * @param caller the identity of the match that request to use this method.
	 * @return an Iterable < Card > containing the initial cards for a Match.
	 * @throws SingletonElementAlreadyGeneratedException if this method has already
	 *         been called with the caller parameter as the parameter.
	 * @throws IllegalArgumentException if the caller parameter is null
	 */
	public Iterable < Card > generatedInitialCards ( ObjectIdentifier < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Collection < Card > res ;
		byte counter ; 
		if ( caller != null )
			if ( factorySupportInitialCards.isAlreadyUser ( caller ) == false )
			{
				res = new LinkedList < Card > () ;
				counter = 0 ;
				for ( RegionType type : RegionType.values() )
					if ( type != RegionType.SHEEPSBURG )
					{
						res.add ( new Card ( type , counter ) ) ;
						counter ++ ;
					}
				factorySupportInitialCards.addUser ( caller ) ;
			}
			else
				throw new SingletonElementAlreadyGeneratedException () ;
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/**
	 * Generated and returns the right sellable cards for a given match, whose identity
	 * is passed by parameter.
	 * 
	 * @param caller the identity of the match that request to use this method.
	 * @return an Iterable < Card > containing the sellable cards for a Match.
	 * @throws SingletonElementAlreadyGeneratedException if this method has already
	 *         been called with the caller parameter as the parameter.
	 * @throws IllegalArgumentException if the caller parameter is null
	 */
	public Iterable < SellableCard > generatedSellableCards ( ObjectIdentifier < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Collection < SellableCard > res ;
		byte counter ;
		byte index ; 
		if ( caller != null )
			if ( factorySupportSellableCards.isAlreadyUser ( caller ) == false )
			{
				res = new LinkedList < SellableCard > () ;
				counter = GameConstants.NUMBER_OF_INITIAL_CARD ;
				for ( RegionType type : RegionType.values () )
					if ( type != RegionType.SHEEPSBURG )
						for ( index =GameConstants.NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE-1 ; index >= 0 ; index -- )
						{
							res.add ( new SellableCard ( type , counter , index ) ) ;
							counter ++ ;
						}	
				factorySupportSellableCards.addUser ( caller ) ;
			}
			else 
				throw new SingletonElementAlreadyGeneratedException () ;
		else
			throw new IllegalArgumentException () ;
		return res ;
	}
	
}
