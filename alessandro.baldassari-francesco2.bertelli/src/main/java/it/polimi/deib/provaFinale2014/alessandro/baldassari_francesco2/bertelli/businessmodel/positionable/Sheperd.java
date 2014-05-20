package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Road;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.user.Player;

import java.awt.Color;

/**
 * This class models a Sheperd, which is one of the fundamental actors of the game.
 * More in depth, the Sheperd is just a pawn for a Game's Player in the GameMap, Player which is the one
 * who actually decide the moves, make decision and so on.
 */
public class Sheperd extends Character < Road > 
{

	//ATTRIBUTES
	
	/**
	 * The player owning this Sheperd, actually his logical controller.
	 */
	private Player owner ;
	
	/**
	 * The color of the pawn representative of the Sheperd. 
	 */
	private final Color color ;
	
	// METHODS
	
	/**
	 * @param name the Name of this Sheperd.
	 * @param color the color of the pawn associated with this Sheperd.
	 * @param owner the Player object bound to this Sheperd ( his owner ).
	 * @throw IllegalArgumentException if the color or the owner property is null.
	 */
	public Sheperd ( String name , Color color , Player owner ) 
	{
		super ( name ) ;
		if ( color != null && owner != null )
		{
			this.color = color ;
			this.owner = owner ;
		}
		else
			throw new IllegalArgumentException () ;
	}

	/**
	 * Getter for the owner property.
	 * 
	 * @return the owner property.
	 */
	public Player getOwner () 
	{
		return owner ;
	}
	
	/**
	 * Getter for the color property.
	 * 
	 * @return the color property.
	 */
	public Color getColor () 
	{
		return color ;
	}
	
}
