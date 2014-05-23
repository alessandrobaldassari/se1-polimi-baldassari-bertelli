package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodelTest;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Bank.NoMoreFenceOfThisTypeException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Fence.FenceType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Card;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.SellableCard;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.*;

public class BankTest {
	private ArrayList <Fence> fences;
	private ArrayList <Card> initCards;
	private ArrayList <SellableCard> otherCards;
	private int initialMoneyReserve;
	private Bank bank;
	
	@Before public void setUp(){
		fences = new ArrayList();
		initCards = new ArrayList();
		otherCards = new ArrayList();	
		fences.add(new Fence(FenceType.NON_FINAL));
		fences.add(new Fence(FenceType.FINAL));
		initCards.add(new Card(RegionType.CULTIVABLE, 1));
		otherCards.add(new SellableCard(RegionType.DESERT, 2, 3));
		initialMoneyReserve = 100;
		bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);
	}
	
	
	@Test public void getMoneyReserve(){
		assertTrue(bank.getMoneyReserve() == initialMoneyReserve);
	}
	
	@Test public void receiveMoney(){
		bank.receiveMoney(50);
		assertTrue(bank.getMoneyReserve()== 150);
	}
	
	@Test (expected = NoMoreFenceOfThisTypeException.class) public void getAFence() throws ArrayIndexOutOfBoundsException, NoMoreFenceOfThisTypeException {
		assertFalse(bank.getAFence(FenceType.NON_FINAL).isFinal());
		assertTrue(bank.getAFence(FenceType.FINAL).isFinal());
		bank.getAFence(FenceType.FINAL);
	}
	
	@Test public void takeInitialCard(){
		assertTrue(bank.takeInitialCard(RegionType.CULTIVABLE).getRegionType() == RegionType.CULTIVABLE);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void BankFirstCondition() {
		fences = null;
		bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void BankSecondCondition() {
		initCards = null;
		bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void BankThirdCondition() {
		otherCards = null;
		bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void BankFourthCondition() {
		initialMoneyReserve = -1;
		bank = new Bank(initialMoneyReserve, fences, initCards, otherCards);	
	}

}
