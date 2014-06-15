package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.observer.Observer;

/***/
public interface RegionTypeChooseViewObserver extends Observer
{

	/***/
	public void onRegionTypeChoosed ( RegionType regionType , Integer price ) ;
	
	/***/
	public void onDoNotWantToChooseAnyRegion () ;
	
}
