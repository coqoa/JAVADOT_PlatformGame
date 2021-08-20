package JAVADOT_MVC;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JAVADOT_Main extends Application {
	
	Pane root = new Pane();
	AudioClip bgm = new AudioClip(Paths.get("/Users/coqoa/eclipse-workspace/JAVADOT_project/src/JAVADOT_MVC/source/javadotBGM.mp3").toUri().toString());
	
	//introMessage출력
	public void introMessage() {
			
		final Text text = new Text(480, 570, "Please press space bar");
		text.setFill(Color.WHITE);
		text.setFont(Font.font("verdana", FontWeight.BOLD, 20));
		root.getChildren().add(text);
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (text.getText().length() != 0) {
					text.setText("");
				}else{
					text.setText("Please press space bar");
				}
			}
		};
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(700), eventHandler));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}
	
	// BGM 출력
		public void startMusic() {
			bgm.play();
		}
		
	
	@Override
	public void start(Stage stage) throws IOException {

		startMusic();
		
		root = FXMLLoader.load(getClass().getResource("view/introPage.fxml")); //컨테이너에 FXML파일 등록
		Scene scene = new Scene(root);
		introMessage();
		stage.setTitle("JAVADOT");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show(); 
	}
	public static void main(String[] args) {
		launch(args);
	}
}
