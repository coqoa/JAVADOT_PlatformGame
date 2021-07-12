//1. fxml파일로 화면전환구현, door충돌, 바닥충돌도 구현하기 door에서 위로버튼 눌러서 장면전환?
// 7월 10일 1시 : nextStage버튼구현, 맨아래에 level2클래스구현 -> 어떻게 적용할까?버튼을 눌렀을때 level2화면을 가진 scene를 할당해야한다? 적어가면서 풀어보기
// 메인화면꾸미고 버튼을 통해서 챕터넘기기 구현? -> 코드좀만더보면 할 수 있을것 같기도
// 7월11일 메인화면, 스타트메시지, bgm넣을 코드 구현완료, ObjectData클래스에 level2 level3 맵만들면 단순 코드만 변경해서 사용할 수 있도록 구현완료
package JAVADOT_MVC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class JAVADOT_Controller3 {
	public Stage stage;
	public Scene scene;
	public Pane root = new Pane();
	
	public HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	public ArrayList<Node> blocks = new ArrayList<Node>();
	public ArrayList<Node> energys = new ArrayList<Node>();
	public ArrayList<Node> doors = new ArrayList<Node>();
	public Pane mainContainer = new Pane();
	public Pane blockContainer = new Pane();
	public Node player;
	
	public int levelWidth;
	public Point2D playerVelocity = new Point2D(0, 0);
	public boolean canJump = true;
	
	int jumpNumber;
	Label jumpCount = new Label();
	Button jumpCountButton = new Button();
//	Button nextStage = new Button();
	
	public Node createObject(int x, int y, int w, int h, Color color) {
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		
		blockContainer.getChildren().addAll(object);
		return object;
	}
	  // Object 생성 메소드
	public void levelData() {
		levelWidth = ObjectData1.LEVEL1[0].length() * 10; //이것만 바꿔주면 됨..아마? + 씬초기화메소드
		
		for (int i = 0; i < ObjectData1.LEVEL1.length; i++) {
			String line = ObjectData1.LEVEL1[i];
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
					case '4':
						Node sunR = createObject(j*10, i*10, 10, 10, Color.TOMATO);
						doors.add(sunR);
						break;
					case '5':
						Node sunY = createObject(j*10, i*10, 10, 10, Color.YELLOW);
						doors.add(sunY);
						break;
					case '6':
						Node cloud = createObject(j*10, i*10, 10, 10, Color.WHITE);
						doors.add(cloud);
						break;	
				}
			}
		}
	}
	  // 각 block생성
	public void jumpData() {
		//점프관련
				jumpNumber = 1; //최초생성값
				jumpCount.setText("JUMP COUNT "+jumpNumber);
				jumpCountButton.setLayoutX(10);
				jumpCountButton.setOpacity(0);
				jumpCountButton.setOnKeyPressed(e->{
					if (e.getCode() == KeyCode.SPACE && player.getTranslateY() > -100 && jumpNumber > 0 ) {
						jumpNumber--;
						jumpCount.setText("JUMP COUNT "+jumpNumber);
						jumpPlayer();
						}
				});
				//
	}
	  // 점프 구현 메소드
//	public void nextStageBtn() {
//		nextStage.setText("다음스테이지");
//		nextStage.setLayoutX(50);
//		nextStage.setLayoutY(50);
//		nextStage.setOnKeyPressed(e->{
//			if (e.getCode() == KeyCode.UP) {
//				System.out.println("후");
//				}
//		});
//	}
	public void MainPage() {
		Rectangle bg = new Rectangle(1280, 720, Color.LIGHTSKYBLUE);

		jumpData(); 
		  // jumpCount, jumpCountButton
		
		levelData(); 
		  // createObject (blockContainer)
		
//		nextStageBtn();
		
		player = createObject(0, 600, 20, 20, Color.DODGERBLUE); // createObject (blockContainer)
		
		// 화면자동이동
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if (offset > 640 && offset < levelWidth - 640) {
				blockContainer.setLayoutX(-(offset - 640));
			}
		});
		//
		mainContainer.getChildren().addAll(bg, blockContainer, jumpCount, jumpCountButton);
	}
	
	public boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
//		keys객체를 만드는 HashMap클래스에 쓰이는 메소드 getOrDefault = key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환
	}
	
	public void sceneUpdate() { // AnimationTimer로 1프레임마다 업데이트
		
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() > 0) { // LEFT키를 누르고 player객체의 x값이 0보다 크거나 같다면 movePlayerX의 매개변수로 -2를 입력
			movePlayerX(-6);	
			}
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 20 <= levelWidth) { // RIGHT키를 누르고 player객체의 x값+20(player객체크기)이 맵의 WIDTH보다 작거나 같다면 movePlayerX의 매개변수로 2를 입력
			movePlayerX(6);		
			}
		if (playerVelocity.getY() < 10) { 
			playerVelocity = playerVelocity.add(0, 2);	//playerVelocity의 y값이 10보다 작으면 1프레임당 y값 1씩추가
			}
		movePlayerY((int)playerVelocity.getY()); //movePlayerY(int value)에 playerVelocity값 할당 (낙하담당값이라서 연속적으로 실행되야함)
	
		if (player.getTranslateY()>720)  {
			
//			System.out.println("TOUCH");
		}
	}
	
	public void movePlayerX(int value) {
		boolean movingRight = value > 0; // LEFT=false, RIGHT=true
		
			for (int i = 0; i < Math.abs(value); i++) {
				for (Node block : blocks) { //만들어서 objects라는 객체로 만들어놓은 ArrayList에 넣어둔 block를 하나씩 player와 비교 -> block노드를 사용가능
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
							if (player.getTranslateX() + 20 == block.getTranslateX()) { // 우측방향 이동시 player의 x값은 block+20 block의 x값과 같다면
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
//								}else {
//									player.setTranslateX(player.getTranslateX() - 1); // 오른쪽벽에 붙으면 미끄러지게하는 코드
								}
								
								return;
							}
						} else  {	//LEFT
							if (player.getTranslateX() == block.getTranslateX() + 10) { // 좌측방향 이동시 player의 x값과 block의 x값+10이 같다면 
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
//								}else {
//									player.setTranslateX(player.getTranslateX() + 1); // 왼쪽벽에 붙으면 미끄러지게하는 코드
								}
								return;
							}
						}
					}
				}
				player.setTranslateX(player.getTranslateX()+(movingRight ? 1 : -1)); // RIGHT버튼을 누르면 player객체의 x위치를 +1만큼씩, LEFT버튼을 누르면 x위치를 -1만큼씩이
			}
			for (Node energy : energys) {
				if (player.getBoundsInParent().intersects(energy.getBoundsInParent())) {
					jumpNumber = jumpNumber+1;
					jumpCount.setText("JUMP COUNT "+jumpNumber);
					energy.setTranslateY(energy.getTranslateY()+500);//energy 먹으면 jumpNumber+1, energy 사라지
				}
			}
			for (Node door : doors) {
				if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
					System.out.println("door touch");
				}
			}
		}
	public void movePlayerY(int value) {
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
								if (player.getTranslateY() == block.getTranslateY() + 10) { // 점프시 윗벽이 막혀있으면 더 안올라가짐
									player.setTranslateY(player.getTranslateY() + 1); 
									canJump=false;
									return;
								}
							}
						}
					}
					player.setTranslateY(player.getTranslateY()+(movingDown ? 1 : -1)); //player객체의 위치 = player객체의 이동좌표+ movingDown이 참이라면 +1 거짓이면 -1
				}
				for (Node door : doors) {
					if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
						System.out.println("door touch");
					}
				}
			}
	//
	public void jumpPlayer() {
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -35);
			canJump = false;
		}
	}
	
	public void gameStartButton(ActionEvent event) throws IOException {
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainContainer);
		MainPage();
		stage.setTitle("JAVADOT");
		stage.setScene(scene);
		
		Button btn2 = new Button();
		btn2.setText("2차로");
		btn2.setLayoutX(100);
		btn2.setLayoutY(100);
		stage.show();
		
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
			}
		};
		timer.start();
	}

}
class Level2_ extends JAVADOT_Controller2{
	
	public void levelData() {

		levelWidth = ObjectData1.LEVEL2[0].length() * 10; //이것만 바꿔주면 됨..아마? + 씬초기화메소드
		
		for (int i = 0; i < ObjectData1.LEVEL2.length; i++) {
			String line = ObjectData1.LEVEL2[i];
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
					case '4':
						Node sunR = createObject(j*10, i*10, 10, 10, Color.TOMATO);
						doors.add(sunR);
						break;
					case '5':
						Node sunY = createObject(j*10, i*10, 10, 10, Color.YELLOW);
						doors.add(sunY);
						break;
					case '6':
						Node cloud = createObject(j*10, i*10, 10, 10, Color.WHITE);
						doors.add(cloud);
						break;	
				}
			}
		}
	}
	public void mainPage() {
		Rectangle bg = new Rectangle(1280, 720, Color.LIGHTSKYBLUE);

		jumpData(); 
		  // jumpCount, jumpCountButton
		
		levelData(); 
		  // createObject (blockContainer)
		
		player = createObject(0, 600, 20, 20, Color.DODGERBLUE); // createObject (blockContainer)
		
		// 화면자동이동
		player.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if (offset > 640 && offset < levelWidth - 640) {
				blockContainer.setLayoutX(-(offset - 640));
			}
		});
		//
		mainContainer.getChildren().addAll(bg, blockContainer, jumpCount, jumpCountButton);
	}
	public void gameStartButton(ActionEvent event) throws IOException {
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainContainer);
		mainPage();
		stage.setTitle("JAVADOT");
		stage.setScene(scene);
		stage.show();
		
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
			}
		};
		timer.start();
	}
	
}


