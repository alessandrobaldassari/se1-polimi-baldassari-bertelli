package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal.AdultOvine.AdultOvineType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.match.Match;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.FactorySupport;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Identifiable;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.SingletonElementAlreadyGeneratedException;

/**
 * This class is the component that is the only one authorized to create Animals instances.
 * Just one Factory can be created per one match - this functionality is implemented
 * with a static approach.
 * The the AnimalFactory relative to this game is returned to the caller which is the responsible
 * of it's use.
 */
public class AnimalFactory 
{

	/**
	 * The default Wolf name, equals for every match. 
	 */
	private static final String WOLF_NAME = "THE_WOLF" ;
	
	/**
	 * The default Black Sheep name, equals for every match. 
	 */
	private static final String BLACK_SHEEP_NAME = "THE_BLACK_SHEEP" ;

	/***/
	private static final String OVINE_BASE_NAME = "OVINE " ;
	
	/**
	 * The support for the Factory behavior of the static part of this class. 
	 */
	private static FactorySupport < Match > globalFactorySupport ;
	
	/**
	 * A flag indicating that, if true, indicates that the Singleton Wolf instance has
	 * alrady been generated for the Match this Animal Factory is associated with. 
	 */
	private boolean wolfGenerated ;
	
	/**
	 * A flag indicating that, if true, indicates that the Singleton BlackSheep instance has
	 * alrady been generated for the Match this Animal Factory is associated with. 
	 */
	private boolean blackSheepGenerated ;
	
	/***/
	private short lastIndexEmitted ;
	
	/***/
	private AnimalFactory () 
	{
		wolfGenerated = false ;
		blackSheepGenerated = false ;
		lastIndexEmitted = - 1 ;
	}
	
	/**
	 * This is the Factory Method for the AnimalFactory class.
	 * It returns a new AnimalFactory instance if, and only if, the requesting match,
	 * represented by its identifier as a parameter, is has not already called this method.
	 * 
	 * @param matchIdentifier the identifier of the Match requesting its AnimalFactory.
	 * @return a new AnimalFactory instance if the requesting Match has not called this
	 * 		   method before.
	 * @throws AnimalFactoryAlreadyGeneratedForThisMatchException if a Match which has 
	 *         already called this method, calls it again.
	 */
	public static synchronized AnimalFactory newAnimalFactory ( Identifiable < Match > matchIdentifier ) throws SingletonElementAlreadyGeneratedException  
	{
		AnimalFactory res ; 
		boolean alreadyCalled ;
		if ( globalFactorySupport == null )
			globalFactorySupport = new FactorySupport < Match > () ;
		if ( matchIdentifier != null )
		{
			alreadyCalled = globalFactorySupport.isAlreadyUser( matchIdentifier ) ;
			if ( alreadyCalled == false )
			{
				res = new AnimalFactory ();
				globalFactorySupport.addUser ( matchIdentifier ) ;
			}
			else 
				throw new SingletonElementAlreadyGeneratedException () ;
		}
		else 
			throw new IllegalArgumentException();
		return res ;
	}
	
	/**
	 * This method generate a new AdultOvine, given  its type and name.
	 * @throws IllegalArgumentException if the name or the type parameter is null.
	 */
	public Ovine newAdultOvine ( String name , AdultOvineType type ) 
	{
		AdultOvine res ;
		if ( name != null && type != null )
			res = new AdultOvine ( name , type ) ;
		else 
			throw new IllegalArgumentException () ;
		return res ;
	}
	
	/***/
	public Ovine newAdultOvine ( AdultOvineType type ) 
	{
		Ovine res ;
		res = null ;
		lastIndexEmitted ++ ;
		res = newAdultOvine ( OVINE_BASE_NAME + lastIndexEmitted , type ) ;
		return res ;
	}
	
	/**
	 * This method generate and returns the Singleton Wolf instance for the Match
	 * this AnimalFactory is associated with if not already generated.
	 * 
	 * @throws WolfAlreadyGeneratedException if the Wolf Singleton instance for the Match
	 *         this AnimalFactory is associated with has already been generated. 
	 */
	public Animal newWolf () throws WolfAlreadyGeneratedException 
	{
		Wolf res ;
		if ( wolfGenerated == false )
		{
			res = new Wolf ( WOLF_NAME ) ;
			wolfGenerated = true ;
		}
		else
			throw new WolfAlreadyGeneratedException () ;
		return res ;
	}
	
	/**
	 * This method generates and returns the Singleton BlackSheep instance for the Match
	 * this AnimalFactory is associated with if not already generated.
	 * 
	 * @throws BlackSheepAlreadyGeneratedException if the BlackSheep Singleton instance 
	 * for the Match this AnimalFactory is associated with has already been generated. 
	 */
	public Ovine newBlackSheep () throws BlackSheepAlreadyGeneratedException 
	{
		BlackSheep res ;
		if ( blackSheepGenerated == false )
		{
			res = new BlackSheep ( BLACK_SHEEP_NAME ) ;
			blackSheepGenerated = true ;
		}
		else
			throw new BlackSheepAlreadyGeneratedException () ;
		return res ;
	}
	
	// INNER CLASSES
	
	// EXCEPTIONS
	
	/**
	 * This class models the situation where a Client try to instantiate the Singleton Wolf
	 * instance for a given Match, but it has already been generated and returned. 
	 */
	public class WolfAlreadyGeneratedException extends SingletonElementAlreadyGeneratedException 
	{
		
		/***/
		private WolfAlreadyGeneratedException () 
		{
			super () ;
		}
		
	}
	
	/**
	 * This class models the situation where a Client try to instantiate the Singleton BlackSheep
	 * instance for a given Match, but it has already been generated and returned. 
	 */
	public class BlackSheepAlreadyGeneratedException extends SingletonElementAlreadyGeneratedException 
	{
		
		/***/
		private BlackSheepAlreadyGeneratedException () 
		{
			super () ;
		}
		
	}
	
}
