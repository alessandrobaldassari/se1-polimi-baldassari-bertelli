package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank;

import java.util.ArrayList;
import java.util.Collection;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.CardFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.ObjectIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.factory.WithFactorySupportObject;

/**
 * This class implements the Factory pattern for the Bank object to control the creation of
 * Bank objects. 
 */
public class BankFactory extends WithFactorySupportObject < Match > 
{

	/**
	 * Instance variable to implement to Singleton Pattern. 
	 */
	private static BankFactory instance ;
	
	/***/
	private BankFactory () 
	{
		super () ;
	}
	
	/**
	 * Method that implements the Singleton pattern
	 * 
	 * @return the only existing instance of the BankFactory class.
	 */
	public synchronized static BankFactory getInstance () 
	{
		if ( instance == null )
			instance = new BankFactory () ;
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
	public Bank newInstance ( ObjectIdentifier < Match > caller ) throws SingletonElementAlreadyGeneratedException 
	{
		Bank res ;
		Collection < Fence > initFences ;
		Iterable < Card > initialCards ;
		Iterable < SellableCard > otherCards ;
		byte i ;
		if ( getFactorySupport().isAlreadyUser ( caller ) == false )
		{
			getFactorySupport().addUser ( caller ) ;
			initFences = new ArrayList < Fence > ( GameConstants.NON_FINAL_FENCE_NUMBER + GameConstants.FINAL_FENCE_NUMBER ) ;
			for ( i = 0 ; i < GameConstants.NON_FINAL_FENCE_NUMBER ; i ++ )
				initFences.add ( new Fence ( FenceType.NON_FINAL ) ) ;
			for ( i = 0 ; i < GameConstants.FINAL_FENCE_NUMBER ; i ++ )
				initFences.add ( new Fence ( FenceType.FINAL ) ) ;
			initialCards = CardFactory.getInstance().generatedInitialCards ( caller ) ;
			otherCards = CardFactory.getInstance().generatedSellableCards ( caller ) ;
			res = new Bank ( GameConstants.INITIAL_MONEY_RESERVE , initFences , initialCards , otherCards ) ;
		}
		else 
			throw new SingletonElementAlreadyGeneratedException () ;
		return res ;
	}
	
}
