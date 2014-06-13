package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview;

import java.util.concurrent.atomic.AtomicReference;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;

public class DefaultSheperdColorChooseViewObserver implements SheperdColorChooseViewObserver
{

	private AtomicReference < NamedColor > color ;
	
	public DefaultSheperdColorChooseViewObserver ( AtomicReference < NamedColor > color ) 
	{
		this.color = color ;
	}
	
	@Override
	public void onColorChoosed ( NamedColor selectedColor ) 
	{
		synchronized ( color ) 
		{
			color.set ( selectedColor ) ;
			color.notifyAll();
		}
	}

	@Override
	public void onDoNotWantChooseColor() {}

}
