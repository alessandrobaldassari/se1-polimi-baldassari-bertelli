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
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.GameController;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.MoveFactory;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.Sheperd;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.ClientHandler;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.ConnectionLoosingManager;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.matchconnectionloosingcontroller.MatchConnectionLoosingController;
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
	private transient ClientHandler clientHandler ;
	
	/***/
	private WriteOnceProperty < Boolean > methodCompleted ;
	
	/**
	 * 
	 */
	private transient ExecutorService executorService ;
	
	/**
	 * 
	 */
	private ConnectionLoosingManager connectionLoosingManager ;
	
	/**
	 * @param name the name of this Player
	 * @param clientHandler the value for the ClientHandler property.
	 * @throws IllegalArgumentException if the clientHandler parameter is null.
	 */
	public NetworkCommunicantPlayer ( String name , ClientHandler clientHandler , ConnectionLoosingManager connectionLoosingManager ) 
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
	public void chooseCardsEligibleForSelling () throws TimeoutException
	{
		Future < Boolean > f ;
		Callable < Boolean > realMethodExecutor ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		realMethodExecutor = new Callable < Boolean > () 
		{
			@Override
			public Boolean call () throws IOException
			{
				Iterable < SellableCard > arrived ;
					boolean res ;
					arrived = clientHandler.chooseCardsEligibleForSelling ( getSellableCards () ) ;
					try 
					{
						methodCompleted.setValue ( true ) ;
						for ( SellableCard s : arrived )
							if ( getSellableCards ().contains ( s ) )
							{
								getSellableCards ().remove ( s ) ;
								getSellableCards ().add ( s ) ;
							}
						res = true ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) 
					{
						res = false ;
					}
					return res ;
				}  
		} ;
		f = executorService.submit ( realMethodExecutor ) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue () == false )
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
					throw new TimeoutException () ;
				else
					chooseCardsEligibleForSelling();
			}
			else
				try 
				{
					f.get () ;
				}
				catch ( InterruptedException e ) 
				{
					throw new RuntimeException ( e ) ;
				}
				catch ( ExecutionException e ) 
				{
					if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
						throw new TimeoutException () ;
					else
						chooseCardsEligibleForSelling();
				}
		}
		catch ( PropertyNotSetYetException e ) 
		{
			e.printStackTrace();
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
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		f = executorService.submit ( new Callable < GameMove > () 
			{
				@Override
				public GameMove call () throws IOException 
				{
					GameMove res ;
					res = clientHandler.doMove ( moveFactory , gameMap ) ;
					try 
					{
						methodCompleted.setValue ( true ) ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) {}
					return res ;
				}  
			}
		) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue() == true )
				res = f.get () ;
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
			if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
	public Sheperd chooseSheperdForATurn () throws TimeoutException
	{
		Sheperd res  ;
		Future < Sheperd > f ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		f = executorService.submit ( new Callable < Sheperd > () 
			{
				@Override
				public Sheperd call () throws IOException
				{
					Sheperd res ;
					res = clientHandler.chooseSheperdForATurn ( getSheperds () ) ;
					try 
					{
						methodCompleted.setValue ( true ) ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) {}
					return res ;
				}  
			} 
		) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue() == true )
				res = f.get () ;
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
					throw new TimeoutException () ;
				else
					res = chooseSheperdForATurn();
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
			if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
				throw new TimeoutException () ;
			else
				res = chooseSheperdForATurn();
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
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		f = executorService.submit ( new Callable < SellableCard > () 
			{
				@Override
				public SellableCard call () throws IOException
				{
					SellableCard res ;
					res = clientHandler.chooseCardToBuy ( src ) ;
					try 
					{
						methodCompleted.setValue ( true ) ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) {}
					return res ;
				}  
			}
		) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue() == true )
				res = f.get () ;
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
			if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
	public NamedColor getColorForSheperd ( final Iterable < NamedColor > availableColors ) throws TimeoutException
	{
		NamedColor res  ;
		Future < NamedColor > f ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		f = executorService.submit ( new Callable < NamedColor > () 
			{
				@Override
				public NamedColor call () throws IOException 
				{
					NamedColor res ;
					res = clientHandler.requestSheperdColor(availableColors);
					try 
					{
						methodCompleted.setValue ( true ) ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) {}
					return res ;
				}  
			}
		) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue() == true )
				res = f.get () ;
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
			if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
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
	public Road chooseInitialRegionForASheperd ( final Iterable < Road > availableRoads ) throws TimeoutException
	{
		Road res  ;
		Future < Road > f ;
		methodCompleted = new WriteOnceProperty < Boolean > () ;
		new Timer ().schedule ( new TimeoutExpirationTimerTask () , MatchConnectionLoosingController.WAITING_TIME ) ;
		f = executorService.submit ( new Callable < Road > () 
			{
				@Override
				public Road call () throws IOException 
				{
					Road res ;
					res = null; // to impl
					try 
					{
						methodCompleted.setValue ( true ) ;
					} 
					catch ( WriteOncePropertyAlreadSetException e ) 
					{}
					return res ;
				}  
			}
		) ;
		while ( methodCompleted.isValueSet () == false ) ;
		try 
		{
			if ( methodCompleted.getValue() == true )
				res = f.get () ;
			else
			{
				if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
					throw new TimeoutException () ;
				else
					res = chooseInitialRegionForASheperd(availableRoads);
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
			if ( connectionLoosingManager.manageConnectionLoosing ( this , true ) == false )
				throw new TimeoutException () ;
			else
				res = chooseInitialRegionForASheperd(availableRoads);
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
