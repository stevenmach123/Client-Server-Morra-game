import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TheGameOfMorraServer extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Server) Let's Play Morra!!!");
		
		Scene scene = new Scene(new HBox(),600,600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
