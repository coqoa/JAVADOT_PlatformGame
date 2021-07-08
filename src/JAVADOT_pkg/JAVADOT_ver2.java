//7월8일
//ArrayList를 여러개만들어서 관리하기? - 완



package JAVADOT_pkg;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class JAVADOT_ver2 extends Application {

	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Node> blocks = new ArrayList<Node>();
	private ArrayList<Node> energys = new ArrayList<Node>();
	private ArrayList<Node> doors = new ArrayList<Node>();
	private Pane mainContainer = new Pane();
	private Pane blockContainer = new Pane();
	
	private Node player;
	
	private int levelWidth;
	private Point2D playerVelocity = new Point2D(0, 0);
	private boolean canJump = true;
	
	private Node createObject(int x, int y, int w, int h, Color color) {
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		
		blockContainer.getChildren().addAll(object);
		return object;
	}
	
	
	private void MainPage() {
		Rectangle bg = new Rectangle(1280, 720, Color.LIGHTGOLDENRODYELLOW);
		
		levelWidth = ObjectData.LEVEL1[0].length() * 10;
		
		
		for (int i = 0; i < ObjectData.LEVEL1.length; i++) {
			String line = ObjectData.LEVEL1[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
					case '0':
						break;
					case '1':
						Node block = createObject(j*10, i*10, 10, 10, Color.LIGHTGREEN);
						blocks.add(block);
						break;
					case '2':
						Node energy = createObject(j*10, i*10, 5, 5, Color.ORANGE);
						energys.add(energy);
						break;
					case '3':
						Node door = createObject(j*10, i*10, 10, 10, Color.BLACK);
						doors.add(door);
						break;
//						private ArrayList<Node> platforms = new ArrayList<Node>();						
				}
			}
		}
		player = createObject(0, 600, 20, 20, Color.DODGERBLUE);
		
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if (offset > 640 && offset < levelWidth - 640) {
				blockContainer.setLayoutX(-(offset - 640));
			}
		});
		
		mainContainer.getChildren().addAll(bg, blockContainer);
	}
	
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
//		keys객체를 만드는 HashMap클래스에 쓰이는 메소드 getOrDefault = key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환
	}
	
	private void sceneUpdate() { // AnimationTimer로 1프레임마다 업데이트
		
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() > 0) { // LEFT키를 누르고 player객체의 x값이 0보다 크거나 같다면 movePlayerX의 매개변수로 -2를 입력
			movePlayerX(-6);	
			}
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 40 <= levelWidth -  5) { // RIGHT키를 누르고 player객체의 x값+20(player객체크기)이 맵의 WIDTH보다 작거나 같다면 movePlayerX의 매개변수로 2를 입력
			movePlayerX(6);		
			}
		//
		if (isPressed(KeyCode.SPACE) && player.getTranslateY() > -100) {
			jumpPlayer();
		}
//		//
		if (playerVelocity.getY() < 10) { 
			playerVelocity = playerVelocity.add(0, 2);	//playerVelocity의 y값이 10보다 작으면 1프레임당 y값 1씩추가
			}
		movePlayerY((int)playerVelocity.getY()); //movePlayerY(int value)에 playerVelocity값 할당 (낙하담당값이라서 연속적으로 실행되야함)
	}
	
	private void movePlayerX(int value) {
		boolean movingRight = value > 0; // LEFT=false, RIGHT=true
		
			for (int i = 0; i < Math.abs(value); i++) {
				for (Node block : blocks) { //만들어서 objects라는 객체로 만들어놓은 ArrayList에 넣어둔 block를 하나씩 player와 비교 -> block노드를 사용가능
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
							if (player.getTranslateX() + 20 == block.getTranslateX()) { // 우측방향 이동시 player의 x값은 block+20 block의 x값과 같다면
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
								}
								return;
							}
						} else  {	//LEFT
							if (player.getTranslateX() == block.getTranslateX() + 10) { // 좌측방향 이동시 player의 x값과 block의 x값+10이 같다면 
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
								}
								return;
							}
						}
					}
				}
				player.setTranslateX(player.getTranslateX()+(movingRight ? 1 : -1)); // RIGHT버튼을 누르면 player객체의 x위치를 +1만큼씩, LEFT버튼을 누르면 x위치를 -1만큼씩이
			}
			for (Node door : energys) {
				if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
					System.out.println("energy touch");
				}
			}
			for (Node door : doors) {
				if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
					System.out.println("door touch");
				}
			}
		}
	private void movePlayerY(int value) {
		boolean movingDown = value > 0;
				
				for (int i = 0; i < Math.abs(value); i++) {
					for (Node block : blocks) {
						if (player.getBoundsInParent().intersects(block.getBoundsInParent())) { //player와 block비교
							if (movingDown) {
								if (player.getTranslateY() + 20 == block.getTranslateY()) { // player객체 y값 +20(객체높이) = block객체 y값 이라면
									player.setTranslateY(player.getTranslateY() - 1);		// player객체의 y값을 -1해주고 canJump메소드 활성화
									canJump=true;
									return;
								}
							} else {
								if (player.getTranslateY() == block.getTranslateY() + 9) { // 점프시 윗벽이 막혀있으면 더 안올라가짐
									canJump=false;
									return;
								}
							}
						}
					}
					player.setTranslateY(player.getTranslateY()+(movingDown ? 1 : -1)); //player객체의 위치 = player객체의 이동좌표+ movingDown이 참이라면 +1 거짓이면 -1
				}
				for (Node door : energys) {
					if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
						System.out.println("energy touch");
					}
				}
				for (Node door : doors) {
					if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
						System.out.println("door touch");
					}
				}
			}
	//
	private void jumpPlayer() {
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -35);
			canJump = false;
		}
	}
	
	//
	@Override
	public void start(Stage primaryStage) {
		
		MainPage();
		
		Scene scene = new Scene(mainContainer);
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		primaryStage.setTitle("JAVADOT");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
			}
		};
		timer.start();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
