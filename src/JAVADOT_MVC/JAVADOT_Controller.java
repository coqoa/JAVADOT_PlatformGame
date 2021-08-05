package JAVADOT_MVC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.image.impl.ByteBgr;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JAVADOT_Controller {
	//변수선언
	public Stage stage;
	public Scene scene;
	public Node player;
	public Pane mainContainer = new Pane();
	public HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	public Point2D playerVelocity = new Point2D(0, 0);
	public Rectangle bg;
		
	// 클래스 참조 변수
	LevelData level = new LevelData(); //LevelData클래스 사용하기 위한 level객체 생성
	JAVADOT_Main mainClass = new JAVADOT_Main();
	//재시작 메서드
	public void restartGame() {
		player.setTranslateX(10);
		player.setTranslateY(550);
		level.blockContainer.setTranslateX(0);
		jumpData();
		for (Node energy : level.energys) { // player와 energy 충돌구현 (충돌시 jumpNumber값 +1, 점프횟수 시각화, energy객체는 이동시켜서 화면에 안보이게함
			if (energy.getTranslateY() > 4000) {
				energy.setTranslateY(energy.getTranslateY()-4000);
			}
		}
	}
	
	///점프관련 변수, 메서드
	int jumpNumber;
	public Text jumpCount = new Text();
	public Button jumpCountButton = new Button();
	public void jumpData() {
		
		jumpNumber = 1; //시작시 가지는 점프횟수 기본값
		jumpCount.setText(""+jumpNumber); //화면에 출력
		jumpCount.setTranslateX(630); //출력위치
		jumpCount.setTranslateY(150);
		jumpCount.setFont(Font.font("Brush Script MT", 50));
		jumpCount.setFill(Color.WHITE);
		jumpCount.setStroke(Color.DARKBLUE);
		jumpCount.setStrokeWidth(2);
		jumpCountButton.setTranslateX(10);
		jumpCountButton.setOpacity(0);
		jumpCountButton.setOnKeyPressed(e->{
			if (e.getCode() == KeyCode.SPACE && jumpNumber > 0 && canJump) {
				jumpNumber--;
				jumpCount.setText(""+jumpNumber);
				jumpPlayer();
				System.out.println(playerVelocity + "점프높이");
			}
		});
	}
    
	public boolean canJump = true;
	public void jumpPlayer() {
		
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -34);
			canJump = false;
		}
	}
	
	// scene에 포함하는 mainContainer 구성하는 메서드
	public Node mainPage() {
		// 맨 위에 위치해야 player객체가 door보다 앞에 표시됨, 매개변수를 매개변수로 넣음
		level.levelData(); 
    	
		//게임프레임, 배경화면 색깔
		bg = new Rectangle(1280, 720, Color.LIGHTSKYBLUE);
		
        // jumpCount, jumpCountButton
		jumpData(); 
			
		// createObject (blockContainer)
		player = level.createObject(10, 550, 20, 20, Color.MEDIUMVIOLETRED);
		
		mainContainer.getChildren().addAll(bg, level.blockContainer, jumpCount, jumpCountButton);
		return mainContainer;
	}
	

		public boolean isPressed(KeyCode key) {
		//	key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환		
        return keys.getOrDefault(key, false);
	}
		
		
	// AnimationTimer로 매번 업데이트하는 메서드
	public void sceneUpdate() { 
		//	LEFT키를 누르고 player객체의 x값이 0보다 크거나 같다면 movePlayerX의 매개변수로 -14을 입력		
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() > 0) { 
			movePlayerX(-14);
		}
        //	RIGHT키를 누르고 player객체의 오른쪽 경가 맵의 넓이보다 작다면 movePlayerX의 매개변수로 14을 입력
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 20 < level.levelWidth) { 
			movePlayerX(14);	
		}
		// esc버튼으로 재시작
		if (isPressed(KeyCode.ESCAPE)) {
			if(player.getTranslateX() > 1) {
				restartGame();
			}
		}
		//임시작성코드
		if (isPressed(KeyCode.C)) {
			if(player.getTranslateX() > 1) {
//				System.out.println(player.getLayoutX());
				System.out.println("player위치 : " + player.getTranslateX());
				System.out.println("levelBlock위치 :" + level.blockContainer.getTranslateX());
			}
		}
		if (isPressed(KeyCode.X)) { //종료단축키
			if(player.getTranslateX() > -1) {
				System.exit(0);
			}
		}
		//
        //	playerVelocity의 y값이 10보다 작으면 y값 2씩추가(체공시간, 점프높이 조절)
		if (playerVelocity.getY() < 10) { 
			playerVelocity = playerVelocity.add(0, 2);	
		}
        //	movePlayerY(int value)에 playerVelocity값 할당
		movePlayerY((int)playerVelocity.getY()); 
		
		
		for (Node door : level.doors) { // 3 : door 충돌체크 메서드
			if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
					if (player.getTranslateX() > 0 && player.getTranslateX() < 4490) { // 1번맵 내에서 충돌시 2번맵으로 이동
						level.blockContainer.setTranslateX(4700); //카메라 위치 지정
						level.blockContainer.setTranslateY(0);
						
						System.out.println(player.getTranslateX()+" 1번도어"); //임시저장코드
						
						player.setTranslateX(5250); // 2번맵 생성위치
						player.setTranslateY(600); 
						jumpData(); //점프데이터 초기화 (횟수1로)
						
					}else if (player.getTranslateX() > 5170 && player.getTranslateX() < 9020) { // 2번맵 내에서 충돌시 번맵으로 이동
						level.blockContainer.setTranslateX(9800); //카메라 위치 지정
						level.blockContainer.setTranslateY(0);
						
						System.out.println(player.getTranslateX()+" 2번도어"); //임시저장코드
						
						player.setTranslateX(9800); // 3번맵 생성위치
						player.setTranslateY(600); 
						jumpData();
					
					}else if (player.getTranslateX() > 9700 && player.getTranslateX() < 13550 ) { // 3번맵 내에서 충돌시 4번맵으로 이동
						level.blockContainer.setTranslateX(14400); //카메라 위치 지정
						level.blockContainer.setTranslateY(0);
						
						System.out.println(player.getTranslateX()+" 3번도어"); //임시저장코드
						
						player.setTranslateX(14400); // 4번맵 생성위치
						player.setTranslateY(600); 
						jumpData();
					
					}else if (player.getTranslateX() > 14230 && player.getTranslateX() < 18080) { // 4번맵 내에서 충돌시 5번맵으로 이동
						level.blockContainer.setTranslateX(18900); //카메라 위치 지정
						level.blockContainer.setTranslateY(0);
						
						System.out.println(player.getTranslateX()+" 4번도어");//임시저장코드
						
						player.setTranslateX(18900); // 5번맵 생성위치
						player.setTranslateY(600); 
						jumpData();
						
					}else if (player.getTranslateX() > 18760 && player.getTranslateX() < 22610) { // 5번맵 내에서 충돌시 6번맵으로 이동
						level.blockContainer.setTranslateX(23450); //카메라 위치 지정
						level.blockContainer.setTranslateY(0);
						
						System.out.println(player.getTranslateX()+" 5번도어");//임시저장코드
						
						player.setTranslateX(23450); // 6번맵 생성위치
						player.setTranslateY(600); 
						jumpData();
						
					}else if (player.getTranslateX() > 23300 && player.getTranslateX() < 27140) { // 6번맵 내에서 충돌시 7번맵으로 이동
//						level.blockContainer.setTranslateX(); //카메라 위치 지정
//						level.blockContainer.setTranslateY();
						
						System.out.println(player.getTranslateX()+" 6번도어");//임시저장코드
						
//						player.setTranslateX(); // player ?번맵에서 ?번맵으로 이동
//						player.setTranslateY(); 
						jumpData();
						
//					}else if (player.getTranslateX() > __ && player.getTranslateX() < __) { // __번맵
////						level.blockContainer.setTranslateX(); //카메라 위치 지정
////						level.blockContainer.setTranslateY();
////						
////						System.out.println(player.getTranslateX()+" ?번도어");
////						
////						player.setTranslateX(); // player ?번맵에서 ?번맵으로 이동
////						player.setTranslateY(); 
////						jumpData();
					}
			}
			 // energy를 먹으면 jumpNumber를 1 증가시키고 jumpCount에 출력하고
			 // energy의 Y값을 +4000만큼 증가시켜서 화면에서 제외시킨다 (재시작시 -4000을 줘서 다시 돌려놓음)
			for (Node energy : level.energys) { 
				if (player.getBoundsInParent().intersects(energy.getBoundsInParent())) {
					jumpNumber = jumpNumber+1;
					jumpCount.setText(""+jumpNumber);
					energy.setTranslateY(energy.getTranslateY()+4000);
				}
			}
		}
		
		for (Node reset : level.resets) { // 7 : reset블럭 닿으면 재시작
			if (player.getBoundsInParent().intersects(reset.getBoundsInParent())) {
				restartGame();
			}
		}
		
		for (Node moveYDownBlock : level.moveYDownBlocks) {
			//player와 block비교
				if (moveYDownBlock.getTranslateY() < 730 ) {
					moveYDownBlock.setTranslateY(moveYDownBlock.getTranslateY()+2);
				}else {
					moveYDownBlock.setTranslateY(moveYDownBlock.getTranslateY()-750);
				}
			}
		
	
		for (Node moveYUpBlock : level.moveYUpBlocks) {
			//player와 block비교
				if (moveYUpBlock.getTranslateY() > 0 ) {
					moveYUpBlock.setTranslateY(moveYUpBlock.getTranslateY()-2);
				}else {
					moveYUpBlock.setTranslateY(moveYUpBlock.getTranslateY()+730);
				}
		}
	}



	public void moveCamera() {
		 //  카메라이동 player의 위치 : 640 ~ (전체화면 - 640)사이일때
		//	blockContainer의 X값 이동(-(player의X값-640만))
		if (player.getTranslateX() > 640 && player.getTranslateX() < level.levelWidth-640 ) {
			level.blockContainer.setTranslateX(-(player.getTranslateX()-640));
		}
	}

	public void movePlayerX(int value) {
    	// LEFT=false, RIGHT=true
		boolean movingRight = value > 0; 
		
			for (int i = 0; i < Math.abs(value); i++) {

				// LevelData클래스의 blocks ArrayList에 넣어둔 block를 하나씩 실행
				for (Node block : level.blocks) { //  block : 초록색 벽
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
                        	// RIGHT입력시 player의 오른쪽경계와 block의 왼쪽경계가 맞닿았을때
							if (player.getTranslateX() + 20 == block.getTranslateX()) { 
                            	// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 block은 뛰어넘게 해줌)
								if(player.getTranslateY()+10 < block.getTranslateY()) { 
									player.setTranslateY(player.getTranslateY()-10);
								}else {
									// 붙었을때 player객체의 변화를 주는메서드
									player.setOpacity(0.5);
								}
								return;
							}
						} else  {	//LEFT
                        	// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
							if (player.getTranslateX() == block.getTranslateX() + 10) { 
								// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 block은 뛰어넘게 해줌)
                                if(player.getTranslateY()+10 < block.getTranslateY()) {
									player.setTranslateY(player.getTranslateY()-10);
                                }else {
									// 붙었을때 player객체의 변화를 주는메서드
									player.setOpacity(0.5);
                                }
                                return;
							}
						}
					}
					player.setOpacity(1.0);
				}
				
				for (Node slipBlock : level.slipBlocks) { // slipBlock : 남색 벽
					if (player.getBoundsInParent().intersects(slipBlock.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
                        	// RIGHT입력시 player의 오른쪽경계와 slipBlock의 왼쪽경계가 맞닿았을때
							if (player.getTranslateX() + 20 == slipBlock.getTranslateX()) { 
                            	// player의 y값+10이 slipBlock의 y값보다 작다면 y값을 -10해준다 (한칸짜리 slipBlock은 뛰어넘게 해줌)
								if(player.getTranslateY()+10 < slipBlock.getTranslateY()) { 
									player.setTranslateY(player.getTranslateY()-10);
								}else {
								player.setTranslateX(player.getTranslateX()-1);// 벽에 안달라붙고 미끄러지게 하는코드
								}
								return;
							}
						} else  {	//LEFT
                        	// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
							if (player.getTranslateX() == slipBlock.getTranslateX() + 10) { 
								// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 slipBlock은 뛰어넘게 해줌)
                                if(player.getTranslateY()+10 < slipBlock.getTranslateY()) {
									player.setTranslateY(player.getTranslateY()-10);
								}else {
                                player.setTranslateX(player.getTranslateX()+1);// 벽에 안달라붙고 미끄러지게 하는코드
								}
                                return;
							}
						}
					}
				}
				for (Node layout : level.layouts) { // layout : 투명 벽
					if (player.getBoundsInParent().intersects(layout.getBoundsInParent())) {
						if (movingRight) {
                        	// RIGHT입력시 player의 오른쪽경계와 layout의 왼쪽경계가 맞닿았을때 멈추게함
							if (player.getTranslateX() + 20 == layout.getTranslateX()) { 
								player.setTranslateX(player.getTranslateX()-1);
								return;
							}
						} else  {
                        	// LEFT입력시 player의 왼쪽경계와 layout의 오른쪽 경계가 맞닿았을때 멈추게 함
							if (player.getTranslateX() == layout.getTranslateX() + 10) { 
								player.setTranslateX(player.getTranslateX()+1);
								return;
							}
						}
					}
				}
				
                // RIGHT버튼을 누르면 player객체의 x위치를 +0.5만큼씩, LEFT버튼을 누르면 x위치를 -0.5만큼씩이동
				player.setTranslateX(player.getTranslateX()+(movingRight ? 0.5 : -0.5)); 
			}
//			for (Node reset : level.resets) { // 7 : reset블럭 닿으면 재시작
//				if (player.getBoundsInParent().intersects(reset.getBoundsInParent())) {
//					restartGame();
//					
////					System.out.println("TouchReset");
//				}
//			}
		}
	
	public void movePlayerY(int value) {
		boolean movingDown = value > 0;
		
				for (int i = 0; i < Math.abs(value); i++) {
					
					for (Node block : level.blocks) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(block.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥면과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 20 == block.getTranslateY()) { 
									// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
                                    player.setTranslateY(player.getTranslateY() - 1);		
									canJump=true; 
									return;
								}
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == block.getTranslateY() + 10) { 
									// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게 
                                    playerVelocity = playerVelocity.add(0, -playerVelocity.getY()+1);
									// 윗벽에 붙었을때 점프버튼 작동x
                                    canJump = false; 
									return;
								}
							}
						}
					}
					
					for (Node slipBlock : level.slipBlocks) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(slipBlock.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥변과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 20 == slipBlock.getTranslateY()) { 
									// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
                                    player.setTranslateY(player.getTranslateY() - 1);		
									canJump=true; 
									return;
								}
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == slipBlock.getTranslateY() + 10) { 
									// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게 
                                    playerVelocity = playerVelocity.add(0, -playerVelocity.getY()+1);
									// 윗벽에 붙었을때 점프버튼 작동x
                                    canJump = false; 
									return;
								}
							}
						}
					}
					
					for (Node longJump : level.longJumps) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(longJump.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥변과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 19 == longJump.getTranslateY()) { 
									// 충돌시 스프링을 밟은효과
                                	playerVelocity = playerVelocity.add(0, -50);
//									canJump=true; 
									return;
								}
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == longJump.getTranslateY() + 10) { 
									// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게 
                                    playerVelocity = playerVelocity.add(0, -playerVelocity.getY()+1);
									// 윗벽에 붙었을때 점프버튼 작동x
                                    canJump = false; 
									return;
								}
							}
						}
					}
					
					for (Node moveYDownBlock : level.moveYDownBlocks) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(moveYDownBlock.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥변과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 20 == moveYDownBlock.getTranslateY()) { 
									// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
                                    player.setTranslateY(player.getTranslateY() - 1);		
									canJump=true; 
									return;
								}
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == moveYDownBlock.getTranslateY() + 10) { 
									// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게 
                                    playerVelocity = playerVelocity.add(0, -playerVelocity.getY()+2);
									// 윗벽에 붙었을때 점프버튼 작동x
                                    canJump = false; 
									return;
								}
							}
						}
					}
					
					for (Node moveYUpBlock : level.moveYUpBlocks) {
						//player와 block비교
                        if (player.getBoundsInParent().intersects(moveYUpBlock.getBoundsInParent())) { 
							if (movingDown) {
								// player의 바닥변과 block의 윗면이 충돌하면
                                if (player.getTranslateY() + 20 == moveYUpBlock.getTranslateY()) { 
									// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
                                    player.setTranslateY(player.getTranslateY() - 1);		
									canJump=true; 
									return;
                                }
							} else {
                            	// 점프시 윗벽이 막혀있으면 더 안올라가짐
								if (player.getTranslateY() == moveYUpBlock.getTranslateY() + 10) { 
									// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게 
                                    playerVelocity = playerVelocity.add(0, -playerVelocity.getY()+1);
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
//				for (Node reset : level.resets) { // 7 : reset블럭 닿으면 재시작
//					if (player.getBoundsInParent().intersects(reset.getBoundsInParent())) {
//						restartGame();
//						
////						System.out.println("TouchReset");
//					}
//				}
			}
	
	public void gameStartButton(ActionEvent event) throws IOException {
		
		mainPage();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(mainContainer, 1280, 720);
		stage.setScene(scene);
		stage.show();
		
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
		
//		쓰레드 미사용 버전
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
//				System.out.println("sceneUpdate-AnimationTimer");
			}
		};
		timer.start();
		
//		//쓰레드사용버전 ->삭제예정 210803
//		Thread updateThread = new Thread() {
//			@Override
//			public void run() {
//				AnimationTimer timer = new AnimationTimer() {
//					@Override
//					public void handle(long now) {
//						sceneUpdate();
//					}
//				};
//				timer.start();
//			}
//		};
//		Platform.runLater(updateThread);
//		updateThread.setDaemon(true);
//		updateThread.start();
//		System.out.println("updateThread 상태 event " + updateThread.getState());

//		Thread cameraThread = new Thread() { -> 삭제예정 210803
//				@Override
//				public void run() {
//				EventHandler<ActionEvent> sceneMove = new EventHandler<ActionEvent>() {
//					public void handle(ActionEvent e) {
//						moveCamera();
//						
//						}
//					};
//				Timeline sceneAnimation = new Timeline(new KeyFrame(Duration.millis(1), sceneMove));
//				sceneAnimation.setCycleCount(Timeline.INDEFINITE);
//				sceneAnimation.play();
//				}
//		};
//		Platform.runLater(cameraThread);
//		cameraThread.setDaemon(true);
//		cameraThread.start();
//		
				
		//화면이동을 animationTimer로 관리
		Thread cameraThread = new Thread() {
			@Override
			public void run() {
				AnimationTimer timer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						moveCamera();
//						System.out.println("CAMERA-animationTimer");
					}
				};
				timer.start();
				}
		};
		Platform.runLater(cameraThread);
		cameraThread.setDaemon(true);
		cameraThread.start();
//		System.out.println("cameraThread 상태 event " + cameraThread.getState()); // cameraThread의 상태확인메서드
	}
}

class LevelData { //객체생성관련 코드모음 (player, block, energy, door, 등등)
	public Pane blockContainer = new Pane();
	public int levelWidth;
	public ArrayList<Node> blocks = new ArrayList<Node>();
	public ArrayList<Node> energys = new ArrayList<Node>();
	public ArrayList<Node> doors = new ArrayList<Node>();
	public ArrayList<Node> longJumps = new ArrayList<Node>();
	public ArrayList<Node> leftLongJumps = new ArrayList<Node>();
	public ArrayList<Node> rightLongJumps = new ArrayList<Node>();
	public ArrayList<Node> moveYDownBlocks = new ArrayList<Node>();
	public ArrayList<Node> moveYUpBlocks = new ArrayList<Node>();
	public ArrayList<Node> resets = new ArrayList<Node>();
	public ArrayList<Node> layouts = new ArrayList<Node>();
	public ArrayList<Node> slipBlocks = new ArrayList<Node>();
	public ArrayList<Node> bgObject = new ArrayList<Node>(); 
	
	public Node createObject(int x, int y, int w, int h, Color color) {
		
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		blockContainer.getChildren().addAll(object);
		return object;
	}
	public Node levelData() {
		levelWidth = ObjectData1.LEVEL1[0].length() * 10; 
		blockContainer.setCache(true);
		blockContainer.setCacheShape(true);
		blockContainer.setCacheHint(CacheHint.SPEED);
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
						Node energy = createObject(j*10, i*10, 7, 7, Color.GOLD);
						energys.add(energy);
						break;
					case '3':
						Node door = createObject(j*10, i*10, 10, 10, Color.BLACK);
						doors.add(door);
						break;
					case '4':
						Node slipBlock = createObject(j*10, i*10, 10, 10, Color.NAVY);
						slipBlocks.add(slipBlock);
						break;
					case '5':
						Node longJump = createObject(j*10, i*10, 10, 10, Color.RED);
						longJumps.add(longJump);
						break;
					case '8':
						Node moveYDownBlock = createObject(j*10, i*10, 10, 10, Color.DARKGREEN);
						moveYDownBlocks.add(moveYDownBlock);
						break;
					case '9':
						Node moveYUpBlock = createObject(j*10, i*10, 10, 10, Color.DARKGREEN);
						moveYUpBlocks.add(moveYUpBlock);
						break;
					case 'R':
						Node sunR = createObject(j*10, i*10, 10, 10, Color.TOMATO);
						bgObject.add(sunR);
						break;
					case 'Y':
						Node sunY = createObject(j*10, i*10, 10, 10, Color.YELLOW);
						bgObject.add(sunY);
						break;
					case 'W':
						Node cloud = createObject(j*10, i*10, 10, 10, Color.WHITE);
						bgObject.add(cloud);
						break;
					case 'G':
						Node darkGreen = createObject(j*10, i*10, 10, 10, Color.DARKGREEN);
						bgObject.add(darkGreen);
						break;
					case 'g':
						Node lightGreen = createObject(j*10, i*10, 10, 10, Color.LIGHTGREEN);
						bgObject.add(lightGreen);
						break;
					case 'B':
						Node darkBrown = createObject(j*10, i*10, 10, 10, Color.SADDLEBROWN);
						bgObject.add(darkBrown);
						break;
					case 'b':
						Node lightBrown = createObject(j*10, i*10, 10, 10, Color.DARKGOLDENROD);
						bgObject.add(lightBrown);
						break;
					case 'K':
						Node black = createObject(j*10, i*10, 10, 10, Color.BLACK);
						bgObject.add(black);
						break;
					case '7':
						Node reset = createObject(j*10, i*10, 10, 10, Color.WHITE);
						resets.add(reset);
						break;
					case 'L':
						Node layout = createObject(j*10, i*10, 10, 10, Color.TRANSPARENT);
						layouts.add(layout);
						break;
				}
			}
		}
		return blockContainer;
	}
	
}

