package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities;

/***/
public class WorkflowException extends Exception 
{
	
	/***/
	public WorkflowException () {}

	public WorkflowException ( String message ) 
	{
		super ( message ) ;
	}
	
	public WorkflowException ( Throwable cause , String message ) 
	{
		super ( message , cause ) ;
	}
	
}
