package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.GameConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.BankFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.bank.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.CardFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.MatchIdentifier;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.*;

/*
 * This jUnit test class tests Bank.class
 */
public class BankTest 
{
	/*
	 * Declaring all the variables needed to the correct execution of setUp() 
	 */
	private static int idInstance ;
	private ArrayList <Fence> fences;
	private ArrayList <Card> initCards;
	private ArrayList <SellableCard> otherCards;
	private Bank bank;
	
	@BeforeClass
	public static void setUpBeforeClass () 
	{
		idInstance = 0 ;
	}
	
	/*
	 * Initializing the previous variables to invoke Bank() and initialize bank variable
	 * for the test
	 */
	@Before 
	public void setUp()
	{
		fences = new ArrayList();
		initCards = new ArrayList();
		otherCards = new ArrayList();	
		fences.add(new Fence(FenceType.NON_FINAL));
		fences.add(new Fence(FenceType.FINAL));
		//initCards.add(new Card(RegionType.CULTIVABLE, 1));
		//otherCards.add(new SellableCard(RegionType.DESERT, 3, 2));
		//otherCards.add(new SellableCard(RegionType.DESERT, 2, 3));
		try 
		{
			bank = BankFactory.getInstance().newInstance ( MatchIdentifier.newInstance() );
			idInstance ++ ;
		}
		catch (SingletonElementAlreadyGeneratedException e) 
		{
			e.printStackTrace();
			throw new RuntimeException ( e ) ; 
		}
	}
	
	/*
	 * This function tests getMoneyReserve()
	 */
	@Test 
	public void getMoneyReserve ()
	{	
		assertTrue ( bank.getMoneyReserve() == GameConstants.INITIAL_MONEY_RESERVE ) ;
	}
	
	/*
	 * This function tests receiveMoney()
	 */
	@Test 
	public void receiveMoney()
	{
		int increment ;
		increment = 50 ;
		bank.receiveMoney ( increment ) ;
		assertTrue ( bank.getMoneyReserve() == GameConstants.INITIAL_MONEY_RESERVE + increment );
	}
	
	@Test
	public void hasAFenceOfThisType1 () 
	{
		assertTrue ( bank.hasAFenceOfThisType ( FenceType.FINAL ) ) ;
		assertTrue ( bank.hasAFenceOfThisType ( FenceType.NON_FINAL ) ) ;
	}
	
	/***/
	@Test
	public void hasAFenceOfThisType2 () 
	{
		for ( int i = 0 ; i < GameConstants.FINAL_FENCE_NUMBER ; i ++ )
			try {
				bank.getAFence ( FenceType.FINAL ) ;
			} catch (NoMoreFenceOfThisTypeException e) {
				e.printStackTrace();
				fail ( "GET_A_FENCE_IMPLEMENTATION_ERROR" ) ;
			}
		assertFalse ( bank.hasAFenceOfThisType ( FenceType.FINAL ) ) ;
		assertTrue ( bank.hasAFenceOfThisType ( FenceType.NON_FINAL ) ) ;
	}
	
	@Test
	public void hasAFenceOfThisType3 () 
	{
		for ( int i = 0 ; i < GameConstants.NON_FINAL_FENCE_NUMBER ; i ++ )
			try {
				bank.getAFence ( FenceType.NON_FINAL ) ;
			} catch (NoMoreFenceOfThisTypeException e) {
				e.printStackTrace();
				fail ( "GET_A_FENCE_IMPLEMENTATION_ERROR" ) ;
			}
		assertTrue ( bank.hasAFenceOfThisType ( FenceType.FINAL ) ) ;
		assertFalse ( bank.hasAFenceOfThisType ( FenceType.NON_FINAL ) ) ;
	}
	
	/*
	 * This function tests getAFence() exploring all the possibles method's executions then forcing the method to throw a NoMoreFenceOfThisTypeException
	 */
	@Test ( expected = NoMoreFenceOfThisTypeException.class ) 
	public void getAFence() throws ArrayIndexOutOfBoundsException, NoMoreFenceOfThisTypeException 
	{
		int i ;
		assertFalse ( bank.getAFence(FenceType.NON_FINAL).isFinal () ) ;
		assertTrue ( bank.getAFence(FenceType.FINAL).isFinal () ) ;
		for ( i = 0 ; i < GameConstants.FINAL_FENCE_NUMBER - 1 ; i ++ )
			bank.getAFence ( FenceType.FINAL ) ;
		bank.getAFence ( FenceType.FINAL ) ;
	}
	
	/*
	 * This function tests takeInitialCard() 
	 */
	@Test ( expected = NoMoreCardOfThisTypeException.class )
	public void takeInitialCard () throws NoMoreCardOfThisTypeException
	{
		assertTrue ( bank.takeInitialCard(RegionType.CULTIVABLE).getRegionType() == RegionType.CULTIVABLE ) ;
		bank.takeInitialCard ( RegionType.CULTIVABLE ) ;
	}
	
	/*
	 * This function tests getPeekCardPrice() exploring all the possibles method's executions then forcing the method to throw a NoMoreCardOfThisTypeException
	 */
	@Test ( expected = NoMoreCardOfThisTypeException.class )
	public void getPeekCardPrice () throws NoMoreCardOfThisTypeException
	{
		int i ;
		assertEquals ( bank.getPeekCardPrice ( RegionType.DESERT ) , 0 ) ;
		try 
		{
			for ( i = 0 ; i < GameConstants.NUMBER_OF_NON_INITIAL_CARDS_PER_REGION_TYPE ; i ++ )
				bank.sellACard ( i , RegionType.DESERT ) ;
			bank.sellACard ( 0 , RegionType.DESERT ) ;
		} 
		catch (CardPriceNotRightException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * This function tests sellaACard() exploring all the possibles method's executions then forcing the method to throw a CardPriceNotRightException
	 */
	@Test ( expected = CardPriceNotRightException.class )
	public void sellACArd() throws CardPriceNotRightException, NoMoreCardOfThisTypeException
	{
		SellableCard card ;
		int price ;
		price = bank.getPeekCardPrice ( RegionType.DESERT ) ;
		card = bank.sellACard ( price , RegionType.DESERT ) ;
		assertTrue ( card.getInitialPrice() == price ) ;
		assertTrue(card.getRegionType() == RegionType.DESERT ) ;
		price = bank.getPeekCardPrice ( RegionType.CULTIVABLE ) ;
		card = bank.sellACard ( price + 1 , RegionType.CULTIVABLE ) ;
	}
	
}
