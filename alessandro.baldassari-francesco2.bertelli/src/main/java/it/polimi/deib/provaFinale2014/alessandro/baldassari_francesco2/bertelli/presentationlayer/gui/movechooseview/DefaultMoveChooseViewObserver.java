package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultMoveChooseViewObserver implements MoveChooseViewObserver 
{

	/***/
	private AtomicReference < GameMoveType > move ;
	
	/***/
	public DefaultMoveChooseViewObserver ( AtomicReference < GameMoveType > move ) 
	{
		this.move = move ;
	}
	
	/***/
	@Override
	public void onMoveChoosed ( GameMoveType choosenMove ) 
	{
		synchronized ( move ) 
		{
			move.set ( choosenMove ) ;
			move.notifyAll () ;
		}
	}

	/***/
	@Override
	public void onDoNotWantChooseMove () {}
	
}
