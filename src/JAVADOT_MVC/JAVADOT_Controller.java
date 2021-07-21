package JAVADOT_MVC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class JAVADOT_Controller {
	public Stage stage;
	public Scene scene;
	public Node player;
	public Pane mainContainer = new Pane();
	public HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	public Point2D playerVelocity = new Point2D(0, 0);
	public boolean canJump = true;
	public boolean pressESC;
	
	LevelData level = new LevelData(); //LevelData클래스 사용하기 위한 level객체 생성
	JAVADOT_Main mainClass = new JAVADOT_Main();
	
	///점프관련 변수, 메서드
	int jumpNumber;
	public Label jumpCount = new Label();
	public Button jumpCountButton = new Button();

	public void jumpData() {
		
		jumpNumber = 1; //시작시 가지는 점프횟수 기본값
		jumpCount.setText(""+jumpNumber); //화면에 출력
		jumpCount.setTranslateX(630); //출력위치
		jumpCount.setTranslateY(150);
		jumpCount.setFont(Font.font("verdana", FontWeight.BOLD, 20));
		jumpCount.setTextFill(Color.LIGHTGOLDENRODYELLOW);
		jumpCountButton.setTranslateX(10);
		jumpCountButton.setOpacity(0);
		jumpCountButton.setOnKeyPressed(e->{
			if (e.getCode() == KeyCode.SPACE && jumpNumber > 0 && canJump) {
				jumpNumber--;
				jumpCount.setText(""+jumpNumber);
				jumpPlayer();
			}
		});
	}
    
	public void jumpPlayer() {
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -35);
			canJump = false;
		}
	}
	
	public Node mainPage() {
		// 맨 위에 위치해야 player객체가 door보다 앞에 표시됨, 매개변수를 매개변수로 넣음
		level.levelData(ObjectData1.LEVEL1); 
    	
		//게임프레임, 배경화면 색깔
		Rectangle bg = new Rectangle(1280, 720, Color.LIGHTSKYBLUE);
		
        // jumpCount, jumpCountButton
		jumpData(); 
			
		// createObject (blockContainer)
		player = level.createObject(0, 600, 20, 20, Color.DODGERBLUE); 
		 
		mainContainer.getChildren().addAll(bg, level.blockContainer, jumpCount, jumpCountButton);
		return mainContainer;
	}
	
		public boolean isPressed(KeyCode key) {
		//	key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환		
        return keys.getOrDefault(key, false);
	}
		
		
	// AnimationTimer로 매번 업데이트
	public void sceneUpdate() { 
		
		
		//	LEFT키를 누르고 player객체의 x값이 0보다 크거나 같다면 movePlayerX의 매개변수로 -6를 입력		
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() > 0) { 
			movePlayerX(-6);
		}
        //	RIGHT키를 누르고 player객체의 오른쪽 경계가 맵의 넓이보다 작다면 movePlayerX의 매개변수로 6를 입력
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 20 < level.levelWidth) { 
			movePlayerX(6);	
		}
		if (isPressed(KeyCode.ESCAPE)) {
			if(player.getTranslateX() > 0) {
			try {
				player.setTranslateX(player.getTranslateX()-5000);
				mainClass.start(stage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
        //	playerVelocity의 y값이 10보다 작으면 y값 2씩추가(체공시간, 점프높이 담당)
		if (playerVelocity.getY() < 10) { 
			playerVelocity = playerVelocity.add(0, 2);	
		}
        //	movePlayerY(int value)에 playerVelocity값 할당
		movePlayerY((int)playerVelocity.getY()); 
		
        //  카메라이동 player의 위치 : 640 ~ (전체화면 - 640)사이일때
		//	blockContainer의 X값 이동(-(player의X값-640만))
		if (player.getTranslateX() > 640 && player.getTranslateX() < level.levelWidth-640 ) {
			level.blockContainer.setLayoutX(-(player.getTranslateX()-640));
		}
		
		for (Node reset : level.resets) { 
			if (player.getBoundsInParent().intersects(reset.getBoundsInParent())) {
				// 7번 박스와 충돌하면 재시작
//				mainContainer.getChildren().clear(); 
				try {
					mainClass.start(stage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			player.setTranslateX(0); //반복적인 rese을 피하기위해 player값의 위치이동
			player.setTranslateY(0); 
			}
		}
		
		for (Node door : level.doors) { // 
			if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
//				if(isPressed(KeyCode.UP)) {
					if (player.getTranslateY() > 0 && player.getTranslateY() < 720) { //1렙 낙하구
						level.blockContainer.setLayoutX(0);
						level.blockContainer.setLayoutY(-810);
						player.setTranslateX(0); //기존의 player객체를 없애는 메소드
						player.setTranslateY(1200); //기존의 player객체를 없애는 메소드
						jumpData();
					}else if (player.getTranslateY() > 750 && player.getTranslateY() < 1500) { //1렙 낙하구
						level.blockContainer.setLayoutX(0);
						level.blockContainer.setLayoutY(-1620);
						player.setTranslateX(0); //기존의 player객체를 없애는 메소드
						player.setTranslateY(1920); //기존의 player객체를 없애는 메소드
						jumpData();
//					}
				}
			}	
		}
		}
	public void movePlayerX(int value) {
    	// LEFT=false, RIGHT=true
		boolean movingRight = value > 0; 
		
			for (int i = 0; i < Math.abs(value); i++) {
            	// LevelData클래스의 blocks ArrayList에 넣어둔 block를 하나씩 실행
				for (Node block : level.blocks) { 
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
                        	// RIGHT입력시 player의 오른쪽경계와 block의 왼쪽경계가 맞닿았을때
							if (player.getTranslateX() + 20 == block.getTranslateX()) { 
                            	// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
								if(player.getTranslateY()+10 < block.getTranslateY()) { 
									player.setTranslateY(player.getTranslateY()-10);
								}
								return;
							}
						} else  {	//LEFT
                        	// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
							if (player.getTranslateX() == block.getTranslateX() + 10) { 
								// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
                                if(player.getTranslateY()+10 < block.getTranslateY()) {
									player.setTranslateY(player.getTranslateY()-10);
								}
								return;
							}
						}
					}
				}
                // RIGHT버튼을 누르면 player객체의 x위치를 +1만큼씩, LEFT버튼을 누르면 x위치를 -1만큼씩이
				player.setTranslateX(player.getTranslateX()+(movingRight ? 1 : -1)); 
			}
            
            // energy를 먹으면 jumpNumber를 1 증가시키고 jumpCount에 출력한
			// energy는 충돌시 Y값을 +500만큼 증가시켜서 화면에서 제외시킨다 
			for (Node energy : level.energys) { // player와 energy 충돌구현
				if (player.getBoundsInParent().intersects(energy.getBoundsInParent())) {
					jumpNumber = jumpNumber+1;
					jumpCount.setText(""+jumpNumber);
					energy.setTranslateY(energy.getTranslateX()+4000);
				}
			}
		}
	
	public void movePlayerY(int value) {
		boolean movingDown = value > 0;
		
				for (int i = 0; i < Math.abs(value); i++) {
					for (Node block : level.blocks) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(block.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥변과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 20 == block.getTranslateY()) { 
									// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
                                    player.setTranslateY(player.getTranslateY() - 1);		
									canJump=true; 
									return;
								}
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == block.getTranslateY() + 10) { 
									//윗벽에 막혔을 시 Y값을 +1해준다(붙어있으면 안움직임) 
                                    player.setTranslateY(player.getTranslateY() + 1); 
									// 윗벽에 붙었을때 점프버튼 작동x
                                    canJump = false; 
									return;
								}
							}
						}
					}
                    //player객체의 위치 = player객체의 이동좌표 + movingDown이 참이라면 +1 거짓이면 -1
					player.setTranslateY(player.getTranslateY()+(movingDown ? 1 : -1)); 
				}
//				for (Node door : doors) { x충돌구현에 해놔서 주석처리해놓음
//					if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
//						System.out.println("door touch");
//					}
//				}
			}
	
	public void gameStartButton(ActionEvent event) throws IOException {
		
		mainPage();
		// if (블럭과 플레이어가 겹쳤고 UP버튼을 눌렀을때)
		// if (changeMap == mainPage(ObjectData1.LEVEL1)) {
		// 	changeMap = mainPage(ObjectData1.LEVEL2) 
        // }else if((changeMap == mainPage(ObjectData1.LEVEL2)){
		// 	changeMap = mainPage(ObjectData1.LEVEL3)
		// } 이런식으로 스테이지넘기기? 이게구현되면 죽는것도구현할 수 있음
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainContainer);
		stage.setTitle("JAVADOT");
		stage.setScene(scene);
		stage.show();
		
		
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		
//		button.setOnKeyPressed(e -> {
//			if (e.getCode() == KeyCode.N) {
//				System.out.println("IT'S N");
//			}
//		}); 버튼눌러서 다음스테이지로 가게하려면 버튼이 스페이스바에 반응하지 않도록 구현해야함
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
			}
		};
		timer.start();
	}
}

class LevelData { //객체생성관련 코드모음 (player, block, energy, door, 등등)
	public Pane blockContainer = new Pane();
	public Pane blockContainer1 = new Pane();
	public int levelWidth;
	public ArrayList<Node> blocks = new ArrayList<Node>();
	public ArrayList<Node> energys = new ArrayList<Node>();
	public ArrayList<Node> doors = new ArrayList<Node>();
	public ArrayList<Node> resets = new ArrayList<Node>();
	public ArrayList<Node> bgObject = new ArrayList<Node>(); 
	
	public Node createObject(int x, int y, int w, int h, Color color) {
		
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		blockContainer.getChildren().addAll(object);
		return object;
	}
	public Node levelData(String[]levelNumber) {
		
//		level.levelData(ObjectData1.LEVEL1); 이렇게 사용가능
		
		levelWidth = levelNumber[0].length() * 10; 
		
		for (int i = 0; i < levelNumber.length; i++) {
			String line = levelNumber[i];
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
						bgObject.add(sunR);
						break;
					case '5':
						Node sunY = createObject(j*10, i*10, 10, 10, Color.YELLOW);
						bgObject.add(sunY);
						break;
					case '6':
						Node cloud = createObject(j*10, i*10, 10, 10, Color.WHITE);
						bgObject.add(cloud);
						break;
					case '7':
						Node reset = createObject(j*10, i*10, 10, 10, Color.WHITE);
						resets.add(reset);
						break;
				}
			}
		}
		return blockContainer;
	}
}


