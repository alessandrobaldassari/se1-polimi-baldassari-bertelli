package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import java.util.ArrayList;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.CardFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

public class BankFactory 
{

	/**
	 * Instance variable to implement to Singleton Pattern. 
	 */
	private static BankFactory instance = new BankFactory () ;
	
	/**
	 * The object needed to implement the Factory pattern 
	 */
	private FactorySupport < Match > factorySupport ;
	
	/***/
	private BankFactory () 
	{
		factorySupport = new FactorySupport < Match > () ; 
	}
	
	/**
	 * Method that implements the Singleton pattern
	 * 
	 * @return the only existing instance of the BankFactory class.
	 */
	public static BankFactory getInstance () 
	{
		return instance ;
	}
	
	/**
	 * Factory method.
	 * 
	 * @param caller the one who requires a new Bank object.
	 * @return a new Bank instance
	 * @throws SingletonElementAlreadyGeneratedException if the caller parameter already
	 *         tried to call this method.
	 */
	public Bank newInstance ( Identifiable < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Bank res ;
		Collection < Fence > initFences ;
		Iterable < Card > initialCards ;
		Iterable < SellableCard > otherCards ;
		byte i ;
		if ( factorySupport.isAlreadyUser ( caller ) == false )
		{
			factorySupport.addUser ( caller ) ;
			initFences = new ArrayList < Fence > ( Bank.NON_FINAL_FENCE_NUMBER + Bank.FINAL_FENCE_NUMBER ) ;
			for ( i = 0 ; i < Bank.NON_FINAL_FENCE_NUMBER ; i ++ )
				initFences.add ( new Fence ( FenceType.NON_FINAL ) ) ;
			for ( i = 0 ; i < Bank.FINAL_FENCE_NUMBER ; i ++ )
				initFences.add ( new Fence ( FenceType.FINAL ) ) ;
			initialCards = CardFactory.getInstance().generatedInitialCards ( caller ) ;
			otherCards = CardFactory.getInstance().generatedSellableCards ( caller ) ;
			res = new Bank ( Bank.INITIAL_MONEY_RESERVE , initFences , initialCards , otherCards ) ;
		}
		else 
			throw new SingletonElementAlreadyGeneratedException () ;
		return res ;
	}
	
}
