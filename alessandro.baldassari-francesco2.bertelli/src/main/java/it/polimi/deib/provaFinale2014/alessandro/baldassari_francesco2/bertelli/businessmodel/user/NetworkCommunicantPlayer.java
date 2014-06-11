package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMap;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.PropertyNotSetYetException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOnceProperty;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.WriteOncePropertyAlreadSetException;

/***/
public class NetworkCommunicantPlayer extends Player  
{

	/**
	 * The ClientHandler object this Player object will use to obtain data when the System will
	 * query each Player during the Game. 
	 */
	private transient ClientHandler < ? > clientHandler ;
	
	/**
	 * A synchronization variable used to wait for the Client responses. 
	 */
	private WriteOnceProperty < Boolean > methodCompleted ;
	
	/**
	 * An ExecutorService object to execute threads.
	 */
	private transient ExecutorService executorService ;
	
	/**
	 * A timer object used to manage the situation where a Client does not answer to a request in a given time. 
	 */
	private transient Timer requestTimeoutTimer ;
	
	/**
	 * An object used when communication fails ( a Client does not answer ).
	 */
	private transient ConnectionLoosingController connectionLoosingManager ;
	
	/**
	 * @param name the name of this Player
	 * @param clientHandler the value for the ClientHandler property.
	 * @throws IllegalArgumentException if the clientHandler parameter is null.
	 */
	public NetworkCommunicantPlayer ( String name , ClientHandler clientHandler , ConnectionLoosingController connectionLoosingManager ) 
	{
		super ( name ) ;
		if ( clientHandler != null && connectionLoosingManager != null )
		{
			this.clientHandler = clientHandler;
			this.connectionLoosingManager = connectionLoosingManager ;
			executorService = Executors.newCachedThreadPool () ;
		}
		else 
			throw new IllegalArgumentException();
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public NamedColor getColorForSheperd ( final Iterable < NamedColor > availableColors ) throws TimeoutException
	{
		NamedColor res  ;
		Future < NamedColor > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < NamedColor > () 
		{
			@Override
			public NamedColor call () throws IOException 
			{
				NamedColor res ;
				res = clientHandler.requestSheperdColor(availableColors);
				setMethodCompleted();
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{
				requestTimeoutTimer.cancel () ;
				res = f.get () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = getColorForSheperd ( availableColors );
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (InterruptedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = getColorForSheperd ( availableColors );
		}
		return res ;
		
	}
	
	/**
	 * AS THE SUPER'S ONE.  
	 */
	@Override
	public void chooseCardsEligibleForSelling () throws TimeoutException
	{
		Future < Boolean > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < Boolean > () 
		{
			@Override
			public Boolean call () throws IOException
			{
				Iterable < SellableCard > arrived ;
				boolean res ;
				arrived = clientHandler.chooseCardsEligibleForSelling ( getSellableCards () ) ;
				res = setMethodCompleted () ;
				if ( res )
					for ( SellableCard s : arrived )
						if ( getSellableCards ().contains ( s ) )
						{
							getSellableCards ().remove ( s ) ;
							getSellableCards ().add ( s ) ;
						}
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue () == false )
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					chooseCardsEligibleForSelling();
			}
			else
				try 
				{
					requestTimeoutTimer.cancel () ;
					f.get () ;
				}
				catch ( InterruptedException e ) 
				{
					throw new RuntimeException ( e ) ;
				}
				catch ( ExecutionException e ) 
				{
					if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
						throw new TimeoutException () ;
					else
						chooseCardsEligibleForSelling();
				}
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
	public GameMove doMove ( final MoveFactory moveFactory , final GameMap gameMap ) throws TimeoutException 
	{
		GameMove res  ;
		Future < GameMove > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < GameMove > () 
		{
			@Override
			public GameMove call () throws IOException 
			{
				GameMove res ;
				res = clientHandler.doMove ( moveFactory , gameMap ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{
				res = f.get () ;
				requestTimeoutTimer.cancel () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = doMove ( moveFactory , gameMap ) ;
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (InterruptedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = doMove ( moveFactory , gameMap ) ;
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Sheperd chooseSheperdForATurn ( final Iterable < Sheperd > sheperds ) throws TimeoutException
	{
		Sheperd res  ;
		Future < Sheperd > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < Sheperd > () 
		{
			@Override
			public Sheperd call () throws IOException
			{
				Sheperd res ;
				res = clientHandler.chooseSheperdForATurn ( sheperds ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{
				requestTimeoutTimer.cancel () ;
				res = f.get () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = chooseSheperdForATurn ( sheperds ) ;
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (InterruptedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = chooseSheperdForATurn ( sheperds );
		}
		return res ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public SellableCard chooseCardToBuy ( final Iterable<SellableCard > src ) throws TimeoutException 
	{
		SellableCard res  ;
		Future < SellableCard > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < SellableCard > () 
		{
			@Override
			public SellableCard call () throws IOException
			{
				SellableCard res ;
				res = clientHandler.chooseCardToBuy ( src ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{
				requestTimeoutTimer.cancel () ;
				res = f.get () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = chooseCardToBuy ( src );
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (InterruptedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = chooseCardToBuy ( src );
		}
		return res ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public void genericNotification ( String message ) 
	{
		try 
		{
			clientHandler.genericNotification ( message ) ;
		}
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	public Road chooseInitialRoadForASheperd ( final Iterable < Road > availableRoads ) throws TimeoutException
	{
		Road res  ;
		Future < Road > f ;
		createAndLaunchRequestTimetoutTimer () ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		f = executorService.submit ( new Callable < Road > () 
		{
			@Override
			public Road call () throws IOException 
			{
				Road res ;
				res = clientHandler.chooseInitialRoadForSheperd ( availableRoads ) ;
				setMethodCompleted () ;
				return res ;
			}  
		} ) ;
		waitForMethodCompletedSet () ;
		try 
		{
			if ( methodCompleted.getValue() == true )
			{	
				requestTimeoutTimer.cancel();
				res = f.get () ;
			}
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
					throw new TimeoutException () ;
				else
					res = chooseInitialRoadForASheperd(availableRoads);
			}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			throw new RuntimeException ( e ) ;
		} 
		catch (InterruptedException e) 
		{
			throw new RuntimeException ( e ) ;
		}
		catch ( ExecutionException e ) 
		{
			if ( connectionLoosingManager.manageConnectionLoosing ( this , clientHandler , true ) == false )
				throw new TimeoutException () ;
			else
				res = chooseInitialRoadForASheperd(availableRoads);
		}
		return res ;
	}

	/**
	 * Set up the requestTimeoutTimer field before a communication session. 
	 */
	private void createAndLaunchRequestTimetoutTimer () 
	{
		requestTimeoutTimer = new Timer () ;
		requestTimeoutTimer.schedule ( new TimeoutExpirationTimerTask () , ConnectionLoosingController.WAITING_TIME ) ;
	}
	
	/**
	 * Allowes a method to wait for the methodCompleted property's value set property to become true.
	 * It's a blocking method. 
	 */
	private void waitForMethodCompletedSet () 
	{
		while ( methodCompleted.isValueSet () ) ;
	}
	
	/**
	 * This method allows to set the methodCompleted property. 
	 */
	private boolean setMethodCompleted () 
	{
		boolean res ;
		try 
		{
			methodCompleted.setValue ( true ) ;
			res = true ;
		} 
		catch ( WriteOncePropertyAlreadSetException e ) 
		{
			res = false ;
			e.printStackTrace () ;
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
			try
			{
				methodCompleted.setValue ( false ) ;
			}
			catch ( WriteOncePropertyAlreadSetException e ) {}
			finally
			{
				cancel () ;
			}
		}		
		
	}
	
}
