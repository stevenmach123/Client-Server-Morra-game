import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

//import com.sun.javafx.scene.paint.GradientUtils.Point;

public class Morainfo2  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Random rand = new Random();
	

	
	int my_ID = rand.nextInt(1000);
	int point[] = new int[2];
	int guess[] = new int[2]; 
	int play[]=  new int[2];  
	
	int myinfo =0; // information (0-5) should be sent from send button
	boolean done = false;
	int option=-3; //1 to stay, 0 to leave
	boolean guess_turn = false; //guess turn taken in 3rd and 4rd branch
    int turn =1; 
    int insert_again =2;
    
    
    
	
    int win =-1;
    
    
    public Morainfo2() {
    	Arrays.fill(play, -1);
    	Arrays.fill(point, 0);
    	Arrays.fill(guess, -1); 
    	
    	
    }
    
      
	/*public int getguess() {
		return guess;
	}

	
	public void setguess(int a) {
		guess= a;
	}
	public int getplay() {
		return play;
	}
	public void setplay(int c) {
		play = c;
	}
	public int getpoint() {
		return point;
	}
	public void setpoint(int d) {
		point = d;
	}  */
  
}

