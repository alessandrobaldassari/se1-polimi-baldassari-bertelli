package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.Match.MatchState;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Lamb;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.Ovine;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController
{

	/***/
	private static final String REGIONS_FILE_PATH = "regions.csv" ;
	
	/***/
	private static final String ROADS_FILE_PATH = "roads.csv" ;
	
	/***/
	
	
	private static final long DELAY = 150000;
	
	private static final int MAX_NUMBER_OF_PLAYERS = 4;
	private Match match ;
	
	private Timer timer;
	GameController () 
	{
	
	}
	
	/***/
	private void creatingPhase () throws IOException 
	{
		InputStream regionsCSVInputStream ;
		InputStream roadsCSVInputStream ;
		GameMap gameMap ;
		Bank bank;
		regionsCSVInputStream = Files.newInputStream ( Paths.get ( REGIONS_FILE_PATH ) , StandardOpenOption.READ  ) ; 
		roadsCSVInputStream = Files.newInputStream ( Paths.get ( ROADS_FILE_PATH ) , StandardOpenOption.READ ) ;
		gameMap = GameMap.newInstance ( regionsCSVInputStream, roadsCSVInputStream ) ;
		bank = Bank.newInstance();
		timer = new Timer();
		match = new Match(gameMap, bank);
		timer.schedule(new WaitingPlayersTimerTask(), DELAY);
		
		
	}
	
	public void addPlayerAndCheck(Player newPlayer) throws WrongStateMethodCallException{
		if(match.getMatchState() == Match.MatchState.WAIT_FOR_PLAYERS){
			match.getPlayers().add(newPlayer);
			if(match.getPlayers().size() == MAX_NUMBER_OF_PLAYERS){
				timer.cancel();
				match.setMatchState(MatchState.INITIALIZATION);
			}
			
		}
		else
			throw new WrongStateMethodCallException();
			
		
	}
	
	public void initializationPhase(){
		placeSheeps();
	}
	
	private void placeSheeps(){
		Ovine ovine;
		double chooseOvineType;
		chooseOvineType = Math.random();
		for(Region r : match.getGameMap().getRegions()){
			if(chooseOvineType < 0.4)
				ovine = new AdultOvine("", AdultOvineType.SHEEP);
			else
				if(chooseOvineType >= 0.4 && chooseOvineType < 0.8)
					ovine = new AdultOvine("", AdultOvineType.RAM);
				else
					ovine = new Lamb("", 0);
			r.getContainedAnimals().add(ovine);
		}
	}
	
	private void distributeInitialCard(){
		List <RegionType> regions;
		regions = new ArrayList <RegionType> ( RegionType.values().length ) ;
		for(RegionType type : RegionType.values())
			regions.add(type);
		regions.remove(RegionType.SHEEPSBURG);
		//randomic card mesh
		for( Player player : match.getPlayers()){
			player.getCards().add(match.getBank().takeInitialCard(regions.get(0)));
		regions.remove(0);
		}
	}
		
	private void moneyDistribution(){
		int moneyToDistribute;
		if(match.getPlayers().size() == 2)
			moneyToDistribute=30;
		else
			moneyToDistribute=20;
		for( Player player : match.getPlayers())
			player.receiveMoney(moneyToDistribute);
	}
	
	
	public class WrongStateMethodCallException extends Exception{
		
	}
	
	private class WaitingPlayersTimerTask extends TimerTask{

		@Override
		public void run() {
			if(match.getPlayers().size() >= 2){
				match.setMatchState(MatchState.INITIALIZATION);
			}
			else{
				//Notify network controller that this match will not start
			}
				
			
		}
		
	}
}
