package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.gameview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.GameMapElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Counter;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Couple;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that manages the measures of the Elements which are on the GameMap. 
 */
public class PositionableElementCoordinatesManager
{
	
	// ATTRIBUTES
	
	/**
	 * A Map object that contains the element to show on the Map; the semantics is :
	 * key : first object an Element Type ( Sheep, Wolf, ... ), second object it's uid.
	 * value: first object a GameElementType ( Region or Roads), second object it's uid.  
	 */
	private Map < Couple < PositionableElementType , Integer > , Couple < GameMapElementType , Integer > > elems ; 
	
	// METHODS
	
	/***/
	public PositionableElementCoordinatesManager () 
	{
		elems = new LinkedHashMap < Couple < PositionableElementType , Integer > , Couple < GameMapElementType , Integer > > () ;
	}

	/***/
	public void addElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId  ) 
	{
		elems.put ( new Couple < PositionableElementType , Integer > ( whoType , whoId ) , new Couple < GameMapElementType , Integer > ( whereType , whereId ) ) ;
	}
	
	/***/
	public void removeElement ( GameMapElementType whereType , int whereId , PositionableElementType whoType , int whoId  ) 
	{
		elems.remove ( new Couple < PositionableElementType , Integer > ( whoType , whoId ) ) ;

	}
	
	/**
	 * This method finds in this Object data all the entries that matches the whereType parameter and returns
	 * the information about this data.
	 *
	 * @param whereType the GameMapElementType to search data about.
	 * @return a Map whose semantics is the sequent :
	 * 	       key : the uid of a GameMapElementType 
	 * 		   value : a Map containing, for each ElementType, the number of instances in the selected place.
	 */
	public Map < Integer , Map < PositionableElementType , Counter > > getData ( GameMapElementType whereType ) 
	{
		Map < Integer , Map < PositionableElementType , Counter > > res ;
		Couple < GameMapElementType , Integer > out ;
		res = new LinkedHashMap < Integer, Map < PositionableElementType , Counter > > () ;
		for ( Couple < PositionableElementType , Integer > in : elems.keySet () )
		{
			out = elems.get ( in ) ;
			// if the out object is involved into the request
			if ( out.getFirstObject() == whereType )
			{
				// if there is no entry for this Position
				if ( res.containsKey ( out.getSecondObject() ) == false )
					res.put ( out.getSecondObject() , new LinkedHashMap < PositionableElementType , Counter > () ) ;
				// if, in the selected position index, there is a type as the one specified by in yet, incrent
				if ( res.get ( out.getSecondObject() ).containsKey ( in.getFirstObject() ) )
					res.get ( out.getSecondObject() ).get ( in.getFirstObject () ).increment () ;
				else
					res.get ( out.getSecondObject () ).put ( in.getFirstObject () , new Counter ( 1 ) ) ;
			}
		}
		return res ;
	}
	
	/**
	 * @param
	 * @param
	 * @param
	 * @param
	 * @return 
	 */
	public Integer findPlaceOf ( GameMapElementType whereType , int whereId , PositionableElementType targetType ) 
	{
		Couple < GameMapElementType , Integer > out ;
		Integer res ;
		res = null ;
		for ( Couple < PositionableElementType , Integer > in : elems.keySet () )
		{
			out = elems.get ( in ) ;
			if ( out.getFirstObject() == whereType && out.getSecondObject () == whereId )
				if ( in.getFirstObject () == targetType )
				{
					res = in.getSecondObject () ;
					break ;
				}
		}
		return res ;
	}
	
}
