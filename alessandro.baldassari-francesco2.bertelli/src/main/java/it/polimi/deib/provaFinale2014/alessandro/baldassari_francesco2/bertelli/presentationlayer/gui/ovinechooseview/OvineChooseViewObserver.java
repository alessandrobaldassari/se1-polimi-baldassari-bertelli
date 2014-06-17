package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.ovinechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.positionable.PositionableElementType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/**
 * This interface defines the object an OvineChooseViewObserver can receive. 
 */
public interface OvineChooseViewObserver extends Observer 
{

	/***/
	public void onOvineTypeChoosed ( PositionableElementType type );
	 
	/***/
	public void onDoNotWantToChooseAnOvineType ();
	
}
