package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.net;

/**
 * Interface that defines an object that is able to unbind RMI objects 
 */
public interface RMIObjectUnbinder 
{

	/**
	 * Effective method for the RMI unbinding functionality; tries to remove from an RMI Registry
	 * an object with the name passed by parameter.
	 * 
	 * @param rmiName the RMI name of the object that has to be unbinded.
	 * @throws UnbindingException if something goes wrong during the unbinding process.
	 */
	public void unbind ( String rmiName ) throws UnbindingException ;
	
	/**
	 * This class models the situation where an error occurs during the unbinding process. 
	 */
	public class UnbindingException extends Exception 
	{
		
		private String objectName ;
		
		/**
		 * @param cause the Throwable that generated this Exception. 
		 * @param objectName the RMI name of the object that caused this Exception.
		 * @throws IllegalArgumentException if the objectName parameter is null.
		 */
		public UnbindingException ( Throwable cause , String objectName ) 
		{
			super ( cause ) ;
			if ( objectName != null )
				this.objectName = objectName ;
			else
				throw new IllegalArgumentException () ;
		}
		
	}
	
}
