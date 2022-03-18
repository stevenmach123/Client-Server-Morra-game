import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;



public class Client extends Thread {
     
	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	String ip;
	int port;
	Scanner scan= new Scanner(System.in);
	String info;
	private Consumer<Serializable> callback;
	Morainfo2 player = new Morainfo2(); // Morainfo with unique serialID. used for server to recognize, and send back string evluated
	Client(Consumer<Serializable> call,String ip, int port){
		this.ip =ip;
		this.port =port;
		callback = call;
	}
	
		public void run() {
		
		try {
		//client socket + input/output stream established
		System.out.println("CLient run1 established");
		
	    socketClient= new Socket(ip,port);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
	    System.out.println("before player established");
	      
	    System.out.println("player established"+player );
	    out.writeObject(player);
	    out.reset();
	   
		}
		catch(Exception e ) {
			callback.accept("bad socketClient");
		}
		
	
		/*
		while(scan.hasNextLine()) {
			info = scan.nextLine();		
			System.out.println(info+" here!");
			try {
		    out.writeObject(info);
			//String message = in.readObject().toString();		
		    String input = (String)in.readObject();
		    callback.accept(input);
			
			System.out.println("i am done line at Client");
			}
			catch(Exception e) {}
		}
		System.out.println("Out of Client_Run()");  */
		
		
		
		try {
			while(true) {
			
				
			String message = in.readObject().toString();//take string from server
			callback.accept(message);
			if(message.contains("+++") ) {
				System.out.println("wow got winner");
				player.done =true;
			}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-gene	rated catch block
			callback.accept("class player in Client not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			callback.accept("I/O in Client is distrubed");
		}
		
    
	
    }  
		public void send(String data) {
			
			
			try {
				player.myinfo = Integer.parseInt(data);  // take information 0-5 from send textfield
				//System.out.println("from send" +player.play1);
				
				out.writeObject(player);  //output as Morainfo object, where store info.
				out.reset();
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				callback.accept("invalid/disturbed user output/input from Client");
			}  
			
		}
		public void myoption(int a) {
			try {
				player.option=a;
				out.writeObject(player);
						
				out.reset();
				out.flush();
				player.done =false;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				callback.accept("invalid/disturbed user output/input from Client");
			}
		}

	
		public boolean getDone() {
			return player.done;
		}
	
	
	


}
