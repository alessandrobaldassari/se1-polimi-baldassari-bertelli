package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.character.animal;


public abstract class AdultOvine < PartnerType extends AdultOvine > extends Ovine
{

	public AdultOvine ( String name ) 
	{
		super ( name ) ;
	}

	public Lamb mate ( PartnerType partner ) 
	{
		return null ;
	}
	
}
