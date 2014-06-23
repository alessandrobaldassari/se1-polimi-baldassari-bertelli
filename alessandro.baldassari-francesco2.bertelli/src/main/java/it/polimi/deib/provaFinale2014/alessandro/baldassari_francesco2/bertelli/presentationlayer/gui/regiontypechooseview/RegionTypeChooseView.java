package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui.regiontypechooseview;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.SheeplandClientApp;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.map.Region.RegionType;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.PresentationMessages;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.MethodInvocationException;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.Utilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.datastructure.Couple;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.FrameworkedWithGridBagLayoutPanel;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.GraphicsUtilities;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.InputView;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.ObservableFrameworkedWithGridBagLayoutDialog;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.utilities.graphic.WithBackgroundImagePanel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class RegionTypeChooseView extends ObservableFrameworkedWithGridBagLayoutDialog < RegionTypeChooseViewObserver >
{

	/***/
	private InputView inputView ;
	
	/***/
	private RegionTypeViewPanel regionTypeViewPanel ;
	
	/***/
	private int moneyLimit ;
	
	/**
	 * @param observer 
	 * @param moneyLimit 
	 * @throw {@link IllegalArgumentException}
	 */
	public RegionTypeChooseView ( DefaultRegionTypeChooseViewObserver observer , int moneyLimit ) 
	{ 
		super ( ( Frame ) null , PresentationMessages.APP_NAME , true ) ;
		if ( moneyLimit > 0 )
		{
			this.moneyLimit = moneyLimit ;
			addObserver ( observer ) ;
		}
		else
			throw new IllegalArgumentException () ;
	}
	
	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void createComponents () 
	{
		inputView = new InputView () ;
		regionTypeViewPanel = new RegionTypeViewPanel () ;
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void manageLayout () 
	{
		Insets insets ;
		insets = new Insets ( 0 , 0 , 0 , 0 ) ;
		layoutComponent ( inputView , 0 , 0 , 1 , 1 , 1 , 1 ,0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets ) ;
		setDefaultCloseOperation ( DO_NOTHING_ON_CLOSE ) ;
		setAlwaysOnTop ( true ) ;	
		inputView.setShowKo(false); 
		setUndecorated(false);
 		setSize ( GraphicsUtilities.getThreeFourthVgaResolution() ) ;
		setLocation ( GraphicsUtilities.getCenterTopLeftCorner ( getSize () ) ) ;
		inputView.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCoverImage(true) ) ; 
		inputView.setTitle ( "Scegli il tipo di regione : " ) ;
		setResizable(false);

	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void bindListeners () 
	{
		inputView.setOkAction ( new Couple < Boolean , Runnable > ( false , new OkAction () ) ) ; 
		inputView.setKoAction ( new Couple < Boolean , Runnable > ( false , new KoAction () ) ) ;		
	}

	/**
	 * AS THE SUPER'S ONE. 
	 */
	@Override
	protected void injectComponents () 
	{
		inputView.setContentsPanel ( regionTypeViewPanel ) ;
		add ( inputView ) ;
	}
	
	/***/
	public void setData ( Map < RegionType , Integer > inData )
	{
		regionTypeViewPanel.setData(inData); 
	}
	
	/**
	 * This class manages the ok action. 
	 */
	private class OkAction implements Runnable 
	{
	
		private static final String NAME_OF_THE_METHOD_TO_CALL = "onRegionTypeChoosed" ;
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		public void run () 
		{
			Couple < RegionType , Integer > res ;
			res = regionTypeViewPanel.getSelectedElem () ;
			if ( res != null )
			{
				if ( res.getSecondObject () <= moneyLimit )
					try 
					{
						notifyObservers ( NAME_OF_THE_METHOD_TO_CALL , res.getFirstObject () , res.getSecondObject () );
					}
					catch (MethodInvocationException e) 
					{
						e.printStackTrace();
					}
				else
					inputView.setErrors ( "Troppi pochi soldi, non te lo puoi permettere..." );
			}
			else
				inputView.setErrors ( PresentationMessages.INVALID_CHOOSE_MESSAGE ) ;
		}
		
	}
	
	/**
	 * This class manages the ko action. 
	 */
	private class KoAction implements Runnable 
	{
	
		private static final String NAME_OF_THE_METHOD_TO_CHOOSE = "onDoNotWantToChooseAnyRegion" ;
		
		/**
		 * AS THE SUPER'S ONE. 
		 */
		public void run () 
		{
			try 
			{
				notifyObservers ( NAME_OF_THE_METHOD_TO_CHOOSE );
			}
			catch (MethodInvocationException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***/
	public static Couple < RegionType , Integer > showDialog ( Map < RegionType , Integer > inData , int moneyLimit ) 
	{
		RegionTypeChooseView regionTypeChooseView ;
		AtomicReference < Couple < RegionType , Integer > > move ;
		move = new AtomicReference < Couple < RegionType , Integer > > ( null ) ;
		regionTypeChooseView = new RegionTypeChooseView ( new DefaultRegionTypeChooseViewObserver ( move ) , moneyLimit ) ;
		regionTypeChooseView.setData(inData); 
		GraphicsUtilities.showUnshowWindow ( regionTypeChooseView , false , true ) ;
		synchronized ( move ) 
		{
			while ( move.get() == null )
				try 
				{
					move.wait () ;
				}
				catch (InterruptedException e ) 
				{
					e.printStackTrace();
				}
		}
		GraphicsUtilities.showUnshowWindow ( regionTypeChooseView , false , false ) ;
		return move.get() ;
	}
	
	public static void main ( String [] args ) 
	{
		Map < RegionType , Integer >  in ;
		Couple < RegionType , Integer > x ;
		in = new HashMap < RegionType , Integer > () ;
		in.put ( RegionType.CULTIVABLE , 0 )  ;
		in.put ( RegionType.FOREST , 3 ) ;
		x = RegionTypeChooseView.showDialog ( in , 1 )  ;
		System.out.println ( x ) ;
	}
	
}

/***/
class RegionTypeViewPanel extends FrameworkedWithGridBagLayoutPanel 
{

	/***/
	private Couple < RegionType , Integer > selected ;
	
	/***/
	private List < WithBackgroundImagePanel > imagePanels ;

	/***/
	private List < JRadioButton > selectors ;
		
	/***/
	public RegionTypeViewPanel () 
	{
		super () ;
	}
	

	@Override
	protected void createComponents () 
	{
		selected = null ;
		imagePanels = new ArrayList < WithBackgroundImagePanel > () ;
		selectors = new ArrayList < JRadioButton > () ;
	}

	@Override
	protected void manageLayout () 
	{
		setOpaque ( false ) ;
	}

	@Override
	protected void bindListeners () 
	{
		Logger.getGlobal().log ( Level.INFO , Utilities.EMPTY_STRING );
	}

	@Override
	protected void injectComponents () 
	{
		Logger.getGlobal().log ( Level.INFO , Utilities.EMPTY_STRING );
	}
	
	public void setData ( Map < RegionType , Integer > inData )
	{
		Insets insets ;
		WithBackgroundImagePanel panel ;
		JRadioButton r ;
		ButtonGroup buttonGroup ;
		int i ;
		insets = new Insets(5,5,5,5) ;
		buttonGroup = new ButtonGroup () ;
		i = 0 ;
		for ( RegionType rt : inData.keySet() )
		{
			panel = new WithBackgroundImagePanel () ;
			r = new JRadioButton ( rt + " [ " + inData.get ( rt ) + " danari ]" ) ;
			imagePanels.add(panel);
			selectors.add(r);
			r.addItemListener ( new SelectorItemListener ( new Couple < RegionType , Integer > ( rt , inData.get(rt) ) ) ); 
			layoutComponent ( panel , i , 0 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.BOTH , GridBagConstraints.CENTER , insets) ;
			layoutComponent ( r , i , 1 , 1 , 1 , 1 , 1 , 0 , 0 , GridBagConstraints.HORIZONTAL , GridBagConstraints.CENTER , insets) ;
			r.setHorizontalAlignment ( SwingConstants.CENTER ) ;
			add ( panel ) ;
			add ( r ) ;
			buttonGroup.add(r); 
			r.setOpaque(false); 
			panel.setOpaque(false);
			panel.setBackgroundImage ( SheeplandClientApp.getInstance().getImagesHolder().getCardImage ( rt ) ) ;
			i ++ ;
		}
		repaint () ;
	}
	
	/***/
	public Couple < RegionType , Integer > getSelectedElem () 
	{
		return selected ;
	}
	
	/***/
	private class SelectorItemListener implements ItemListener 
	{
		
		private Couple < RegionType , Integer > myElem ;
		
		public SelectorItemListener ( Couple < RegionType , Integer > myElem ) 
		{
			this.myElem = myElem ;
		}

		@Override
		public void itemStateChanged ( ItemEvent e ) 
		{
			selected = myElem ;
		}
		
		
		
	}
	
}