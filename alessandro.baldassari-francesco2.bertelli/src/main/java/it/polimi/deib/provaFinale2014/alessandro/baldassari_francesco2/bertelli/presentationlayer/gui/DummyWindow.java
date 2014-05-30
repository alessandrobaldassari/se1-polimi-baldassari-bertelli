package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.presentationlayer.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.businessmodel.moves.GameMove;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.client.CommunicationProtocolResponser;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DummyWindow extends JFrame implements CommunicationProtocolResponser
{

	private JTextArea textArea ;
	private JButton button ;
	private JTextField f ;
	private boolean nameChoosed ;
	
	public DummyWindow () 
	{
		super () ;
		setLayout ( new GridLayout ( 3 , 0 ) ) ;
		textArea = new JTextArea () ;
		button = new JButton ( "write" ) ;
		f = new JTextField () ;
		add ( textArea ) ;
		add ( f ) ;
		add ( button ) ;
		button.addActionListener ( new A () ) ;
	}
	
	private class A implements ActionListener 
	{
		
		public void actionPerformed ( ActionEvent e ) 
		{
			System.out.println(Thread.currentThread().getName());
			nameChoosed = true ;
		}
		
	}
	
	@Override
	public String onNameRequest () 
	{
		System.out.println(Thread.currentThread().getName());
		textArea.append ( "NAME REQUEST" ) ;
		nameChoosed = false ;
		while ( nameChoosed == false ) ;
		System.out.println ( "DUMMY WINDOW : PASSED" ) ;
		return f.getText () ;
	}

	@Override
	public Color onSheperdColorRequest(Iterable<Color> availableColors) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMatchWillNotStartNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generationNotification(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseCardsEligibleForSelling() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChooseSheperdForATurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChoseCardToBuy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameMove onDoMove() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
