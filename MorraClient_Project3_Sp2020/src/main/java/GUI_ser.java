
import java.net.ServerSocket;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;






//fsdfdsf

public class GUI_ser extends Application{

	
	TextField s1,s2,s3,s4, c1, port,ip,win,port_num;
	Button serverChoice,clientChoice,b1,b_login,confirm,again,quit,game_done;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	
	
	Scene startScene;
	BorderPane startPane;
	//Server serverConnection;
	TheServer the_server;
	Client clientConnection;
	Text intro,txt;
	ArrayList<Client> players = new ArrayList<Client>();
	int overall_count =0; 
	int count_only=1; 
	ListView<String> listItems, listItems2;
	 		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		System.out.print("aa");
		this.port_num= new TextField("");
	    this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
				
		this.serverChoice.setOnAction(e->{ 
			
			
			the_server = new TheServer(data->{
		    	Platform.runLater(()->{listItems.getItems().add(data.toString());
		    	}); 
		    	
		    	});
			the_server.start();
			primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("This is a server");  
	   
			
			});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		//login page, where enter port and ip for socketClient to connect ClientThread
		b_login = new Button("login request");
		this.b_login.setOnAction(e-> {
						
			primaryStage.setScene(sceneMap.get("client"));
			primaryStage.setTitle("client "+overall_count+" scene");  
			clientConnection = new Client(data->{
		    Platform.runLater(()->{listItems2.getItems().add(data.toString());
					});
			},ip.getText(),Integer.parseInt(port.getText()));
			clientConnection.start(); 
			
			System.out.print("out of b_login1");			
		      
		    System.out.println("  run_count"+overall_count+ "and "+count_only);
		        
		});
		
		//switch scene to ClientGui game
		this.clientChoice.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("client_login"));
			primaryStage.setTitle("This is a client login");
		}  );
		
		//select enter for port to build server or client login
		this.buttonBox = new HBox(80,serverChoice,clientChoice);
		Text port_enter = new Text("Input port number, then select server.THEN RELOAD THIS THREAD FOR CLIENTS");
		
		VBox a = new VBox(10,port_enter,port_num);
		startPane = new BorderPane();
		startPane.setPrefHeight(200);
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
	
		
		port_num.prefWidth(30);
		startPane.setTop(a);
		
		startScene = new Scene(startPane);
		
		
		//list view for (1)server (2)clients.
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		 win = new TextField();
		 confirm = new Button("Enter");
		 again = new Button("PLAY AGAIN"); 
		  quit = new Button("QUIT");
		  txt = new Text("");
		 game_done = new Button("Game done->");
		 quit.setVisible(false);
		 again.setVisible(false);
		 
		 confirm.prefWidth(60);
		confirm.setOnAction(e->{
				try{
					Integer.parseInt(win.getText()) ;  
					confirm.setVisible(false);
					txt.setText("");
					win.setVisible(false);
					the_server.winset(Integer.parseInt(win.getText()));
				}
				catch(NumberFormatException f){
					System.out.print("invalid num for winning");
				}
		});  
		 
	
		
		game_done.setOnAction(e->{
			if(clientConnection.getDone()==true) {
				again.setVisible(true);
				quit.setVisible(true);
			}
			
			
		});
		
		
		again.setOnAction(e-> {
			
			clientConnection.myoption(1);
			again.setVisible(false);
			quit.setVisible(false);
			
		});
		quit.setOnAction(e-> {
			clientConnection.myoption(0);
			again.setVisible(false);
			quit.setVisible(false);
			
		});
	
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		sceneMap.put("client_login",createClientLog());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		
		
		primaryStage.setScene(startScene);
	
		System.out.print("overall_count "+overall_count);
		primaryStage.show();
		
	}
	
	
	public Scene createServerGui() { //Server display information input from ClientGui game
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		
		VBox log = new VBox(5); 
		log.setAlignment(Pos.CENTER);
		
		pane.setStyle("-fx-background-color: pink");
		
	
		intro = new Text("Morra is a hand game that dates back thousands of years. The rules are simple. If a two\n" + 
				"player game, each player (at the same time) must reveal their hand holding out zero to\n" + 
				"five fingers; at the same time, they must call out their guess about how many fingers\n" + 
				"total will be revealed by both players. If a player guesses correctly, they win a point. If\n" + 
				"both players guess correctly, no points are awarded. If no one guesses correctly, no\n" + 
				"points are awarded. The total number of points to win is determined before the game\n" + 
				"starts.");
		
		log.getChildren().addAll(intro,txt,win,confirm); 
		
		pane.setCenter(listItems);
		pane.setBottom(log);
		pane.prefWidth(600);
      	confirm.setVisible(false);
		win.setVisible(false);
		
		
		return new Scene(pane);
		
		
	}
	public Scene createClientLog() { //Login to connect socketClients to serverSocket
		VBox log = new VBox(15);
		
		Text eport = new Text("Enter Port");
		Text eip = new Text("Enter IP address of your comp");
		port = new TextField("");
		ip = new TextField("");
		
		log.setAlignment(Pos.CENTER);
		log.getChildren().addAll(eport,port,eip,ip,b_login);
		return new Scene(log,400,300);
		
		
	}
	public Scene createClientGui() { //ClientGui game
		HBox insert = new HBox(10);
		HBox option = new HBox(20,game_done,again,quit);
		insert.getChildren().addAll(b1,c1);
		Text remind = new Text("Insert 0-5 only for play, only number for guess after 2 play determined");
		clientBox = new VBox(listItems2,insert,remind,option);
		
		clientBox.setStyle("-fx-background-color: gray");
		return new Scene(clientBox,500,550);
		
	}
	/* public Scene createPointGui() {
		VBox log = new VBox(40); 
		Text text = new Text("Enter number of point that determine winning before start:");
		log.setPadding(new Insets(30));
		
		 
		TextField intro = new TextField("Morra is a hand game that dates back thousands of years. The rules are simple. If a two\n" + 
				"player game, each player (at the same time) must reveal their hand holding out zero to\n" + 
				"five fingers; at the same time, they must call out their guess about how many fingers\n" + 
				"total will be revealed by both players. If a player guesses correctly, they win a point. If\n" + 
				"both players guess correctly, no points are awarded. If no one guesses correctly, no\n" + 
				"points are awarded. The total number of points to win is determined before the game\n" + 
				"starts.");
		text.prefWidth(700);
		log.setAlignment(Pos.CENTER);
		
		log.getChildren().addAll(intro,text,win,confirm); 
		return  new Scene(log);   
		
	}   */
	
  /* public Scene wantMoreGui() {
	   VBox pane = new VBox(40); 
	   
	   pane.setAlignment(Pos.CENTER);
	   pane.getChildren().addAll(again,quit);
	   return new Scene(pane, 300, 400);
	   
   }   */
	
	
class TheServer extends Thread { 
	Consumer<Serializable> callback;
	
	int port;
	Morainfo2 moraTurn = new Morainfo2(); // hold important information of both player, used to know what to send back at particular times
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>(); // collection of ClientThread
	
	TheServer(Consumer<Serializable> call){
		callback =call; // print on Server thread
		
		
		
	}
	//set winning to win one match.
	public void winset(int x) {
		moraTurn.win = x;
		System.out.print("my new win "+moraTurn.win);
		updateClients("Server: The game can start.Prepare for ground 1, please enter your play(0-5)");
	}
	// update listView for ClientGui game
	public void updateClients(String message) {
		for(int i = 0; i < clients.size(); i++) {
		ClientThread t = clients.get(i);
		try {
		 t.out.writeObject(message);
		
		}
		catch(Exception e) {}
		}
	}
	//run() from The Server thread
	public void run() {
		
		//try to connect single Clientsocket once established in while loop 
		try(ServerSocket mysocket = new ServerSocket(Integer.parseInt(port_num.getText()))){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		    	
				ClientThread c = new ClientThread(mysocket.accept(), overall_count+1);
				int x =overall_count+1; 
				callback.accept("client has connected to server: " + "client #" + x);
				clients.add(c); // add to clients array 
				c.start();
				overall_count++; //track overall # clients in server
					
				    
				
			 }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
		
		
		
	}
	
	//
	class ClientThread extends Thread{
		ObjectInputStream in;
		ObjectOutputStream out;
		Socket serverClient;
		int count;	
		private long my_ID =0;
		ClientThread(Socket a, int b){
			serverClient = a;
			count =b;
			
			callback.accept("Server connected to client #"+count);
		}
		
	   // from UID to deterine position in clients array for sending right information to which Client thread
		public int getIndexbyUID(long UID) {
			for(int i = 0; i < clients.size(); i++) {
				if(clients.get(i).my_ID ==UID) {
					return i;
				}
			}
			return -1;
		}
		
		
		
		
		
		// allow pink box insert #win for each match, when either 2 players initially enter or continue new match
		public void insertMatch(int option,boolean done){
			if(done==true && option==1) {
				moraTurn.insert_again -=1;
				if(moraTurn.insert_again ==0) {
				updateClients("Two player connected, please look back pink server thread to determine winning of the match");
				
				confirm.setVisible(true);
				win.setVisible(true);
				txt.setText("Enter number of points for wining the match");
				moraTurn.insert_again = 2;
				}
				
				
			}
		}
		public int oppIndex(int x) {
			if(x==0) {
				return 1; 
			}
			if(x==1) {
				return 0;
			}
			return -1;
		}
		
		//big informative if after each game once have player1 and player 2 guess. Determine who guess correct from result of moraTurn.play1 + moraTurn.play2
		public void evalGuess(int curindex) throws IOException {
			ClientThread t = clients.get(curindex);
			t.out.writeObject("Conclude:");
		
				 
			if( moraTurn.guess[curindex] == moraTurn.play[0] + moraTurn.play[1] && moraTurn.guess[0] == moraTurn.guess[1]){
											 
				t.out.writeObject("  Draw,both correct :| your play is "+moraTurn.play[curindex]+" and guess is "+moraTurn.guess[curindex]);
			}
			else if(moraTurn.guess[0] != moraTurn.play[0] + moraTurn.play[1] && moraTurn.guess[1] != moraTurn.play[0] + moraTurn.play[1]) {
				t.out.writeObject("  Draw,both wrong :| your play is "+moraTurn.play[curindex]+" and guess is "+moraTurn.guess[curindex]);
			}
				 
		
			else if(moraTurn.guess[curindex] == moraTurn.play[0] + moraTurn.play[1] ) {					 
					 t.out.writeObject("  Your guess correct! your play is "+moraTurn.play[curindex]+" and guess is "+moraTurn.guess[curindex]);
					 moraTurn.point[curindex] +=1;
		   }
		   else if(moraTurn.guess[curindex] != moraTurn.play[0] + moraTurn.play[1]) {
					 t.out.writeObject("  Your guess no correct :( your play is "+moraTurn.play[curindex]+" and guess is "+moraTurn.guess[curindex]);
			}
			
			
			int x= oppIndex(curindex);
			t.out.writeObject("  Your oppenent play is "+moraTurn.play[x] +" and guess is "+ moraTurn.guess[x]);
				
		 }
		
		
		
		
		//evaluate which player privilage win the match from reach #win determined first
		public boolean evalWin(int curindex){
			
				ClientThread t = clients.get(curindex); //spot right client for write back purpose
				int oppindex  = oppIndex(curindex);
				ClientThread t_op = clients.get(oppindex); // opponent client thread in this case
				
				if(moraTurn.win == moraTurn.point[curindex]) {
				
					try {
						t.out.writeObject("Game: +++ Congratulation! you win the MATCH");
						t_op.out.writeObject("Game: +++ We found the winner, better luck next time :)");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callback.accept("One client is missing");
						
					}
					
					updateClients("Now the (Gamedone->) is activated for options to play again or quit ");
					
					return true;
					
				}
				else {
					
					return false;
				}
		
   
		  
		}
		// remove and display right information of a player leave after the MATCH
		public void evalOption(int option,boolean done){
		  if(done==true) {
			if(option == 0) { // MoraTurn.option = 0 (leave) 1 (remain) 
				System.out.println(count+" opto "+option);
				updateClients("Server: s/o LEAVE.The game can't be start, due to only one player");
				
				clients.remove(this);
				System.out.println(clients.size());
				Thread.currentThread().interrupt();
				overall_count--; 
				
			}	
			else if(option ==1) {
				updateClients("Server: Clients "+count+" stay");
			}
				
		   
		   else {callback.accept("No option selected");}
		  }
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		public void run() {
		
				try { 
					System.out.println("CLientThread established");
					in = new ObjectInputStream(serverClient.getInputStream());
					out = new ObjectOutputStream(serverClient.getOutputStream());
					serverClient.setTcpNoDelay(true);
				
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Streams not open");
				}
			 
			  
			try {
				// take advantage to synch UID of ClientThread to Client.
				// So not to guest clients array when who leave and what information lost, but can correctly spot position and information.
				System.out.println("infoPass established ");
				Morainfo2 infoPass = (Morainfo2)in.readObject(); 
				System.out.println("infoPass established "+infoPass.my_ID);
				System.out.println("infoPass play established "+infoPass.play[0]);
				
				 my_ID  =infoPass.my_ID;
				 
				
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				callback.accept("class Morainfo player not  good from Clients");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				callback.accept("Clients with UID disturbed");
			}     
			System.out.println("run_count "+overall_count);
			
			
			//when the game initially start. if 2 connected, display the pink box for #win determined
			if(overall_count==1) {
				updateClients("Server: Please wait for other player, before game start");
			}
			if(overall_count==2) {
				updateClients("Server: 2 player connected, please look back pink server thread to determine winning of the match");
				
				confirm.setVisible(true);
				win.setVisible(true);
				txt.setText("Enter number of points for wining the match");
				moraTurn.insert_again =2;
			}
			 
			 
			 
			  while(true) {
				 		
				  
				try {
								
					Morainfo2 player = (Morainfo2)in.readObject();
					
					
					
					callback.accept("client #" +count+" serial "+player.my_ID +" sent: " + player.myinfo);
					int cur_index  =getIndexbyUID(player.my_ID); //spot correct on clients array by UID
					evalOption(player.option, player.done); // evaluate if anyone out.
					insertMatch(player.option,player.done);// evaluate if #win box should be displayed in serverGui
					if(player.option== 0) {
						break;   // break run() when leave
					}
				if(overall_count ==2) {		//shall no to pass if 1 clients or >2
						
				    if(moraTurn.win !=-1) { // shall no to pass #win box not used to determine point reached for a match
				    	
				    		
				    	
				    
					
						
				
					  //In one round, have 4 main branch
				    	
				      // moraTurn.play[0] or moraTurn.play[1] should be determined with whoever insert first.	
					 if(moraTurn.turn%2==1 && moraTurn.guess_turn ==false  ) {//odd turn
						 
						  moraTurn.play[cur_index] =player.myinfo; 
						  System.out.println("moraTurn 1.1"+moraTurn.play[0]);
						  System.out.println("moraTurn 1.1"+moraTurn.play[1]);
						
						 clients.get(cur_index).out.writeObject("Reply: Please wait for other client to enter his/her play....."); 
						 moraTurn.turn +=1;
				      }
					 
					  // moraTurn.play1 or moraTurn.play2 should be determined second, not allow a player overwrite first play in moraTurn
					   //Meanwhile wait until player who not input their play.
					 else if(moraTurn.turn%2==0 && moraTurn.guess_turn ==false) { //even turn
						 if(cur_index ==1 || cur_index ==0) {
							 if(moraTurn.play[cur_index] ==-1) {
								 moraTurn.play[cur_index] = player.myinfo;
								 updateClients("Game: Now, you can enter your guess of total play counted on your and opponent hands");
								 moraTurn.turn  +=1;
								 moraTurn.guess_turn =true;
								 System.out.println("moraTurn 2.1 "+moraTurn.play[0]);
								 System.out.println("moraTurn 2.2 "+moraTurn.play[1]);
							 }
							 else { clients.get(cur_index).out.writeObject("You already enter play,please wait other client.....");  }
						 }
						 
		 
						 
						 else { updateClients("Reply: No clients resolved");  }
							 
							 
					 }
					 
					 
					 
					 
					 
					 
					 // moraTurn.guess1 or moraTurn.guess2 should be determined with whoever insert first.
					 else if(moraTurn.turn%2==1 && moraTurn.guess_turn ==true) {
						 
						
							 moraTurn.guess[cur_index] =player.myinfo; 
							// System.out.println("moraTurn 3.1 "+moraTurn.play1);
							 clients.get(cur_index).out.writeObject("Reply: Please wait for other client to enter his/her guess...."); 
							 moraTurn.turn +=1;
					 }
					 
					 
					 
					 
					 
					 
					 
					 
					// moraTurn.guess1 or moraTurn.guess2 should be determined second, not allow a player overwrite first play in moraTurn
					   //Meanwhile wait until player who not input their guess.
					 else if(moraTurn.turn%2==0 && moraTurn.guess_turn ==true) { //even turn, but with guess turn activated
					    
							 if(moraTurn.guess[cur_index] ==-1) {
								 moraTurn.guess[cur_index] = player.myinfo;
								 
								 evalGuess(0);
								 evalGuess(1);
								 boolean m1 = evalWin(0);
								 boolean m2 = evalWin(1);
								 clients.get(0).out.writeObject("  Your point: "+moraTurn.point[0]+" Opponent point: "+moraTurn.point[1]);
								 clients.get(1).out.writeObject("  Your point: "+moraTurn.point[1]+" Opponent point: "+moraTurn.point[0]);
									
								 
								 if(m1==false && m2==false) {// if not found a winner, new round
									 
									 callback.accept("Player winning not resolved");
									 updateClients("-----Prepare for round "+((moraTurn.turn/4)+1)+"Please enter your play(0-5)");
									
								 }
								 else {  // found one, reset so the outter loop check for point1,2 or round and win understand new match should occur
									 moraTurn.win =-1; 
									 moraTurn.point[0] = 0;
									 moraTurn.point[1] = 0;
									 moraTurn.turn =0;
									
									 
								 }
									
								
								
								 moraTurn.guess_turn =false; //now back to branch1 with odd turn
								 
								 moraTurn.guess[0] =-1; //reset play1 and play2 for next round
								 moraTurn.guess[1] = -1; 
								 moraTurn.play[0] =-1;
								 moraTurn.play[1]=-1;
								 moraTurn.turn  +=1;
							 
							 }
							 else { clients.get(cur_index).out.writeObject("Reply: You already enter guess,please wait your opponent...");  }
						   
						
					    }
					 	
					  
						 
					    
					 
					 
				       }//not found winner
				    
				       else {
				    	   updateClients("Server: please look back pink server thread to determine winning of the match");
				       }
						
						
				     }
				
				
					 else if(overall_count ==1) {
						 updateClients("Server: The game can't be start, due to only one player");
					 }
					 else {
						 updateClients("Server: The game can't be start, due to more than two player");
					 }
					 
					 
					 
				
						 
						 
					 //clients.get(count-1).out.writeObject(data.toUpperCase());
					 System.out.println("I am done line at ClientThread");
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
			    	updateClients("Client #"+count+" has left the server!");
			    	clients.remove(this);
			    	if(overall_count > 0) {
			    		overall_count--;
			    	}
			    	break;
				}
				} 
			      
			
			  
			  
			  
		
		}
		
	 
    
		
	}

   }
	
	
		
	
	
}
