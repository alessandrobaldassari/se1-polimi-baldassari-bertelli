package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.util.Collection;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

/**
 * This class is a Factory component for the Card entities.
 * It allows to create just the needed cards for each Match that is created, and no more.
 * The only instance of the CardFactory class is exposed from here with a Singleton Method. 
 */
public class CardFactory 
{

	/**
	 * The only instance for this class to implemement the Singleton pattern. 
	 */
	private static final CardFactory instance = new CardFactory () ;
	
	/**
	 * The number of non initial Cards to be created for each Region. 
	 * It's a business rule.
	 */
	public final int NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE = 5 ;
	
	/**
	 * The total number of initial cards.
	 * It's a business rule. 
	 */
	public final int NUMBER_OF_INITIAL_CARD = 6 ;
	
	/**
	 * An object needed to know and manage the Match objects that already created 
	 * their initial cards. 
	 */
	private final FactorySupport factorySupportInitialCards ;

	/**
	 * An object needed to know and manage the Match objects that already created
	 * their sellable cards. 
	 */
	private final FactorySupport factorySupportSellableCards ;
	
	/***/
	private CardFactory () 
	{
		factorySupportInitialCards = new FactorySupport () ;
		factorySupportSellableCards = new FactorySupport () ;
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
	public Iterable < Card > generatedInitialCards ( Identifiable < Match > caller ) throws SingletonElementAlreadyGeneratedException 
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
	public Iterable < SellableCard > generatedSellableCards ( Identifiable < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Collection < SellableCard > res ;
		byte counter ;
		byte index ; 
		if ( caller != null )
			if ( factorySupportSellableCards.isAlreadyUser ( caller ) == false )
			{
				res = new LinkedList < SellableCard > () ;
				counter = NUMBER_OF_INITIAL_CARD ;
				for ( RegionType type : RegionType.values () )
					if ( type != RegionType.SHEEPSBURG )
						for ( index = NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE-1 ; index >= 0 ; index -- )
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
