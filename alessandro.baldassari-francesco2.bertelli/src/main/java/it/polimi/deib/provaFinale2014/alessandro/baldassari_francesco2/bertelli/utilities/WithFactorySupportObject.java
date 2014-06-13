package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class WithFactorySupportObject < T > 
{

	/***/
	private FactorySupport < T > factorySupport ;
	
	/***/
	public WithFactorySupportObject () 
	{
		factorySupport = new FactorySupport < T > () ;
	}
	
	/***/
	protected FactorySupport < T > getFactorySupport () 
	{
		return factorySupport ;
	}
	
}
