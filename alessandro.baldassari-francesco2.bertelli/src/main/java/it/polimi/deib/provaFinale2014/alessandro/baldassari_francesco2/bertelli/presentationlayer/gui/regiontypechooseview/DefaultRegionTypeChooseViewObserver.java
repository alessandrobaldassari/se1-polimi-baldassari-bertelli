package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultRegionTypeChooseViewObserver implements RegionTypeChooseViewObserver 
{

	private AtomicReference < Couple < RegionType , Integer > > type ;
	
	public DefaultRegionTypeChooseViewObserver ( AtomicReference < Couple < RegionType , Integer > > type ) 
	{
		this.type = type ;
	}
	
	@Override
	public void onRegionTypeChoosed ( RegionType type , Integer price ) 
	{
		synchronized ( this.type ) 
		{
			this.type.set ( new Couple < RegionType , Integer > ( type , price ) ) ;
			this.type.notifyAll () ;
		}
	}

	@Override
	public void onDoNotWantToChooseAnyRegion () {}
	
}
