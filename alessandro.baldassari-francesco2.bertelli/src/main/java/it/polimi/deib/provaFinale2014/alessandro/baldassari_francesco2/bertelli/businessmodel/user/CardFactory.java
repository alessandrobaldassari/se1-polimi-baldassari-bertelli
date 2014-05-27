package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.util.Collection;
import java.util.LinkedList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class CardFactory 
{

	private static final CardFactory instance = new CardFactory () ;
	
	private final FactorySupport factorySupportInitialCards ;

	private final FactorySupport factorySupportSellableCards ;
	
	/**
	 * The number of non initial Cards to be created for each Region. 
	 */
	public final int NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE = 5 ;
	
	public final int NUMBER_OF_INITIAL_CARD = 6 ;
	
	private CardFactory () 
	{
		factorySupportInitialCards = new FactorySupport () ;
		factorySupportSellableCards = new FactorySupport () ;
	}
	
	public static CardFactory getInstance () 
	{
		return instance ;
	}
	
	public Iterable < Card > generatedInitialCards ( Identifiable < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Collection < Card > res ;
		byte counter ; 
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
		return res ;
	}
	
	public Iterable < SellableCard > generatedSellableCards ( Identifiable < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Collection < SellableCard > res ;
		byte counter ;
		byte index ; 
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
		return res ;
	}
	
}
