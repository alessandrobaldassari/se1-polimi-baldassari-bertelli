package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.movechooseview;

import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMoveType;

public class DefaultMoveChooseViewObserver implements MoveChooseViewObserver 
{

	private AtomicReference < GameMoveType > move ;
	
	public DefaultMoveChooseViewObserver ( AtomicReference < GameMoveType > move ) 
	{
		this.move = move ;
	}
	
	@Override
	public void onMoveChoosed ( GameMoveType move ) 
	{
		synchronized ( this.move ) 
		{
			this.move.set ( move ) ;
			this.move.notifyAll () ;
		}
	}

	@Override
	public void onDoNotWantChooseMove () {}

	
	
}
