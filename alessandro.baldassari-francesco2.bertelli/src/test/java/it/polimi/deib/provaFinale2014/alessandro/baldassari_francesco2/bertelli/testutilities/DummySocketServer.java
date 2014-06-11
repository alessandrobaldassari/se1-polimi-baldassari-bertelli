package it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.testutilities;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;

import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.GameProtocolMessage;
import it.polimi.deib.provaFinale2014.alessandro.baldassari_francesco2.bertelli.communication.server.handler.Message;

public class DummySocketServer extends Thread  {
	
	private static final int port = 3333;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectOutputStream oos;
	
	public void run(){
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dummy Server Online");
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dummy Server Accepted Request");
		System.out.println("Dummy Server Working");
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Dummy Socket Server oos");
		try {
			oos.writeObject(Message.newInstance(GameProtocolMessage.UID_NOTIFICATION, (Iterable <Serializable>) new ArrayList<Serializable>()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Dummy Socket Server Writes Object");
		try {
			oos.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Dummy Socket Server Flush");
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dummy Server Down");
		

	}

}
