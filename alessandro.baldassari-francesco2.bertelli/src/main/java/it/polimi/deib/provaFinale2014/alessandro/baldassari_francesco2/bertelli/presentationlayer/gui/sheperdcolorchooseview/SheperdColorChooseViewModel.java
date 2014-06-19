package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.sheperdcolorchooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.NamedColor;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.CollectionsUtilities;

import java.util.List;

class SheperColorChooserViewModel
{

	private List < NamedColor > availableColors ;
	
	private Integer selectedIndex ;
	
	public SheperColorChooserViewModel ( Iterable < NamedColor > in ) 
	{
		availableColors = CollectionsUtilities.newListFromIterable ( in ) ;
		selectedIndex = null ;
	}
	
	public NamedColor getColor ( int index ) 
	{
		return availableColors.get(index) ;
	}
	
	public int getNumberOfColors () 
	{
		return availableColors.size () ;
	}
	
	public void setSelected ( int index ) 
	{
		selectedIndex = index ;
	}
	
	public NamedColor getSelectedColor () 
	{
		if ( selectedIndex != null )
			return availableColors.get ( selectedIndex ) ;
		else
			return null ;
	}
	
}
