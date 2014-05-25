package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodelTest;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.CardPriceNotRightException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreCardOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.*;

/*
 * This jUnit test class tests Bank.class
 */

public class BankTest {
	/*
	 * Declaring all the variables needed to the correct execution of setUp() 
	 */
	private ArrayList <Fence> fences;
	private ArrayList <Card> initCards;
	private ArrayList <SellableCard> otherCards;
	private int initialMoneyReserve;
	private Bank bank;
	
	
	/*
	 * Initializing the previous variables to invoke Bank() and initialize bank variable
	 * for the test
	 */
	@Before 
	public void setUp(){
		fences = new ArrayList();
		initCards = new ArrayList();
		otherCards = new ArrayList();	
		fences.add(new Fence(FenceType.NON_FINAL));
		fences.add(new Fence(FenceType.FINAL));
		initCards.add(new Card(RegionType.CULTIVABLE, 1));
		otherCards.add(new SellableCard(RegionType.DESERT, 3, 2));
		otherCards.add(new SellableCard(RegionType.DESERT, 2, 3));
		initialMoneyReserve = 100;
		//bank = Bank.newInstance ( null );
	}
	
	/*
	 * This function tests getMoneyReserve()
	 */
	@Test 
	public void getMoneyReserve(){
		assertTrue(bank.getMoneyReserve() == initialMoneyReserve);
	}
	
	/*
	 * This function tests receiveMoney()
	 */
	@Test 
	public void receiveMoney(){
		bank.receiveMoney(50);
		assertTrue(bank.getMoneyReserve()== 150);
	}
	
	/*
	 * This function tests getAFence() exploring all the possibles method's executions then forcing the method to throw a NoMoreFenceOfThisTypeException
	 */
	@Test (expected = NoMoreFenceOfThisTypeException.class) 
	public void getAFence() throws ArrayIndexOutOfBoundsException, NoMoreFenceOfThisTypeException {
		assertFalse(bank.getAFence(FenceType.NON_FINAL).isFinal());
		assertTrue(bank.getAFence(FenceType.FINAL).isFinal());
		bank.getAFence(FenceType.FINAL);
	}
	
	/*
	 * This function tests takeInitialCard() 
	 */
	@Test 
	public void takeInitialCard(){
		//assertTrue(bank.takeInitialCard(RegionType.CULTIVABLE).getRegionType() == RegionType.CULTIVABLE);
	}
	
	/*
	 * This function tests getPeekCardPrice() exploring all the possibles method's executions then forcing the method to throw a NoMoreCardOfThisTypeException
	 */
	@Test (expected = NoMoreCardOfThisTypeException.class)
	public void getPeekCardPrice() throws NoMoreCardOfThisTypeException{
		assertTrue(bank.getPeekCardPrice(RegionType.DESERT) == 3);
		bank.getPeekCardPrice(RegionType.MOUNTAIN);
	}
	
	/*
	 * This function tests sellaACard() exploring all the possibles method's executions then forcing the method to throw a CardPriceNotRightException
	 */
	@Test (expected = CardPriceNotRightException.class)
	public void sellACArd() throws CardPriceNotRightException, NoMoreCardOfThisTypeException{
		SellableCard card = (SellableCard) bank.sellACard(3, RegionType.DESERT);
		assertTrue(card.getInitialPrice() == 3);
		assertTrue(card.getRegionType() == RegionType.DESERT);
		assertTrue(card.getId() == 2);
		card = (SellableCard) bank.sellACard(3, RegionType.DESERT);
		
	}
	
	/*
	 * This function tests Bank() exploring the first method's if condition forcing it to throw a IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void BankFirstCondition() {
		fences = null;
		//bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	/*
	 * This function tests Bank() exploring the second method's if condition forcing it to throw a IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void BankSecondCondition() {
		initCards = null;
	//	bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	/*
	 * This function tests Bank() exploring the third method's if condition forcing it to throw a IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void BankThirdCondition() {
		otherCards = null;
		//bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	/*
	 * This function tests Bank() exploring the fourth method's if condition forcing it to throw a IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void BankFourthCondition() {
		initialMoneyReserve = -1;
		//bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}

}
