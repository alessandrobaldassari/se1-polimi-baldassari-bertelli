package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.ovinechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultOvineChooseViewObserver implements OvineChooseViewObserver
{

	/***/
	private AtomicReference < PositionableElementType > move ;
	
	/***/
	public DefaultOvineChooseViewObserver ( AtomicReference < PositionableElementType > move ) 
	{
		this.move = move ;
	}
	
	/***/
	@Override
	public void onOvineTypeChoosed ( PositionableElementType choosenMove ) 
	{
		synchronized ( move ) 
		{
			move.set ( choosenMove ) ;
			move.notifyAll () ;
		}
	}

	/***/
	@Override
	public void onDoNotWantToChooseAnOvineType () {}
	
	
}
