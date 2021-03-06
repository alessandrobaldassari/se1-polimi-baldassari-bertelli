package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.TimeConstants;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.NotSellableException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.card.SellableCard.SellingPriceNotSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelection;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.selector.MoveSelector;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosing.ConnectionLoosingManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.PropertyNotSetYetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOnceProperty;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WrongStateMethodCallException;

/**
 * This class represents a Player that decide which commands do via network. 
 */
public class NetworkCommunicantPlayer extends Player  
{

	// ATTRIBUTES
	
	/**
	 * The ClientHandler object this Player object will use to obtain data when the System will
	 * query each Player during the Game. 
	 */
	private transient ClientHandler < ? > clientHandler ;
	
	/**
	 * A synchronization variable used to wait for the Client responses. 
	 */
	private transient WriteOnceProperty < Boolean > methodCompleted ;
	
	/**
	 * A timer object used to manage the situation where a Client does not answer to a request in a given time. 
	 */
	private transient Timer requestTimeoutTimer ;
	
	/**
	 * An object used when communication fails ( a Client does not answer ).
	 */
	private transient ConnectionLoosingManager connectionLoosingManager ;
	
	// METHODS
	
	/**
	 * @param name the name of this Player
	 * @param clientHandler the value for the ClientHandler property.
	 * @throws IllegalArgumentException if the clientHandler parameter is null.
	 */
	public NetworkCommunicantPlayer ( String name , ClientHandler < ? > clientHandler , ConnectionLoosingManager connectionLoosingManager ) 
	{
		super ( name ) ;
		if ( clientHandler != null && connectionLoosingManager != null )
		{
			this.clientHandler = clientHandler;
			this.connectionLoosingManager = connectionLoosingManager ;
		}
		else 
			throw new IllegalArgumentException();
	}
	
	/**
	 * Return the ClientHandler associated with this Player. 
	 */
	public ClientHandler < ? > getClientHandler () 
	{
		return clientHandler ;
	}
	
	/**
	 * Executes a command and wait for a response.
	 * 
	 * @param commandToExecute the command to execute.
	 * @return the results of the call.
	 * @throws TimeoutException if the execution exceeds a Timer.
	 */
	private < T > T executeLater ( Callable < T > commandToExecute ) throws TimeoutException 
	{
		T res ;
		Future < T > future ; 
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;	
		future = Executors.newSingleThreadExecutor().submit ( commandToExecute ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{
				requestTimeoutTimer.cancel () ;
				res = future.get () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( NetworkCommunicantPlayer.this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = executeLater ( commandToExecute ) ;
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch ( InterruptedException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( NetworkCommunicantPlayer.this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = executeLater ( commandToExecute ) ;
		}
		return res ;
	}
	
	/**
	 * Execute a command without a return value.
	 * 
	 * @param commandToExecute the command to execute.
	 * @throws TimeoutException if the execution time exceeds a Timer.
	 */
	private void executeNow ( Runnable commandToExecute ) throws TimeoutException 
	{
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		Executors.newSingleThreadExecutor().execute ( commandToExecute ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue () == false )
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( NetworkCommunicantPlayer.this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					executeNow(commandToExecute); 
			}
			else
				requestTimeoutTimer.cancel ();
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		}
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor getColorForSheperd ( final Iterable < NamedColor > availableColors ) throws TimeoutException
	{
		Callable < NamedColor > commandToExecute ;
		NamedColor res ;
		commandToExecute =  new Callable < NamedColor > () 
		{
			@Override
			public NamedColor call () throws IOException 
			{
				NamedColor res ;
				res = clientHandler.requestSheperdColor ( availableColors ) ;
				setMethodCompleted();
				return res ;
			}  
		} ;
		res = executeLater(commandToExecute);
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitialRoadForASheperd ( final Iterable < Road > availableRoads ) throws TimeoutException
	{
		Callable < Road > commandToExecute ;
		Road res  ;
		commandToExecute = new Callable < Road > () 
		{
			@Override
			public Road call () throws IOException 
			{
				Road res ;
				res = clientHandler.chooseInitialRoadForSheperd ( availableRoads ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ;
		res = executeLater(commandToExecute);
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd chooseSheperdForATurn ( final Iterable < Sheperd > sheperds ) throws TimeoutException
	{
		Callable < Sheperd > commandToExecute ;
		Sheperd res  ;
		commandToExecute = new Callable < Sheperd > () 
		{
			@Override
			public Sheperd call () throws IOException
			{
				Sheperd res ;
				res = clientHandler.chooseSheperdForATurn ( sheperds ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ;
		res = executeLater(commandToExecute);
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public MoveSelection doMove ( final MoveSelector moveSelector , final GameMap gameMap ) throws TimeoutException 
	{
		Callable < MoveSelection > commandToExecute ;
		MoveSelection res  ;
		commandToExecute = new Callable < MoveSelection > () 
		{
			@Override
			public MoveSelection call () throws IOException 
			{
				MoveSelection res ;
				res = clientHandler.doMove ( moveSelector , gameMap ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ;
		res = executeLater ( commandToExecute ) ;
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE.  
	 */
	@Override
	protected void chooseCardsEligibleForSellingIfThereAreSellableCards () throws TimeoutException
	{
		Runnable commandToExecute ;
		commandToExecute = new Runnable () 
		{
			@Override
			public void run ()  
			{
				Iterable < SellableCard > arrived ;
				SellableCard sc ;
				boolean res ;
				System.out.println ( "NETWORK_COMMUNICANT_PLAYER - chooseCardsEligibleForSelling : " + getSellableCards () ) ;
				try 
				{
					arrived = clientHandler.chooseCardsEligibleForSelling ( getSellableCards () ) ;
					res = setMethodCompleted () ;
					if ( res )
						for ( SellableCard s : arrived )
							if ( hasCard( s.getUID() )  )
							{
								sc = getCard(s.getUID());
								sc.setSellable(true); 
								sc.setSellingPrice ( s.getSellingPrice() ) ;
							}
				}
				catch ( IOException e ) 
				{
					System.err.println ( e.getMessage() ) ;
					synchronized ( methodCompleted ) 
					{
						try 
						{
							methodCompleted.setValue(false);
						} 
						catch (WriteOncePropertyAlreadSetException e1 )
						{
							e1.printStackTrace();
						} 
					}
 					e.printStackTrace();
				}
				catch (NotSellableException e) 
				{
					Logger.getGlobal().log ( Level.SEVERE, Utilities.EMPTY_STRING, e ) ; 
				} 
				catch (SellingPriceNotSetException e )
				{
					Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e ) ;
				}
			}  
		} ;
		executeNow ( commandToExecute ) ;
 	}

	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Iterable < SellableCard > chooseCardToBuy ( final Iterable<SellableCard > src ) throws TimeoutException 
	{
		Callable < Iterable < SellableCard > > commandToExecute ;
		Iterable < SellableCard > res  ; 
		commandToExecute = new Callable < Iterable < SellableCard > > () 
		{
			@Override
			public Iterable < SellableCard > call () throws IOException
			{
				try 
				{
					Iterable < SellableCard > res ;
					System.out.println ( "NETWORK_COMMUNICANT_PLAYER - chooseCardsEligibleForSelling : " + src ) ;
					res = clientHandler.chooseCardToBuy ( src , getMoney() ) ;
					setMethodCompleted () ;
					return res ;
				} 
				catch ( WrongStateMethodCallException e ) 
				{
					throw new IOException ( e ) ;
				}
			}  
		} ;
		res = executeLater ( commandToExecute ) ;
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void genericNotification ( String message ) throws TimeoutException 
	{
		try 
		{
			clientHandler.genericNotification ( message ) ;
		}
		catch ( IOException e ) 
		{
			Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e ) ;
			if ( connectionLoosingManager.manageConnectionLoosing ( NetworkCommunicantPlayer.this , clientHandler , true ) == false )
				throw new TimeoutException () ;
		}
	}

	/**
	 * Set up the requestTimeoutTimer field before a communication session. 
	 */
	private void createAndLaunchRequestTimetoutTimer () 
	{
		requestTimeoutTimer = new Timer () ;
		requestTimeoutTimer.schedule ( new TimeoutExpirationTimerTask () , TimeConstants.CONNECTION_LOOSING_SERVER_WAITING_TIME ) ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void matchEndNotification ( String cause ) 
	{
		try 
		{
			clientHandler.gameConclusionNotification ( cause ) ;
			clientHandler.dispose () ;
		}
		catch (IOException e) 
		{
			Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e ) ;
		}
	}
	
	/**
	 * Allowes a method to wait for the methodCompleted property's value set property to become true.
	 * It's a blocking method. 
	 */
	private void waitForMethodCompletedSet () 
	{
		while ( methodCompleted.isValueSet () == false ) 
			synchronized ( methodCompleted )
			{
				try 
				{
					methodCompleted.wait () ;
				}
				catch (InterruptedException e) 
				{
					Logger.getGlobal().log ( Level.SEVERE , Utilities.EMPTY_STRING , e ) ;
				}
			}
	}
	
	/**
	 * This method allows to set the methodCompleted property. 
	 * 
	 * @return true if the method set the methodCompleted property to true, false if someone already 
	 *         set that variable.
	 */
	private boolean setMethodCompleted () 
	{
		boolean res ;
		synchronized ( methodCompleted )
		{
			try 
			{
				methodCompleted.setValue ( true ) ;
				res = true ;
			} 
			catch ( WriteOncePropertyAlreadSetException e ) 
			{
				res = false ;
			}
			finally
			{
				methodCompleted.notifyAll () ;
			}
		}
		
		return res ;
	}
	
	/**
	 * TimerTask implementation to manage the situation where Clients may not answer in acceptable times
	 * or can not do it at all. 
	 */
	private class TimeoutExpirationTimerTask extends TimerTask 
	{

		/**
		 * AS THE SUPER'S ONE. 
		 */
		@Override
		public void run () 
		{
			synchronized ( methodCompleted )
			{
				try
				{
					methodCompleted.setValue ( false ) ;
				}
				catch ( WriteOncePropertyAlreadSetException e ) {}
				finally
				{
					cancel () ;
					methodCompleted.notifyAll () ;
				}
			}
		}		
		
	}
	
}
