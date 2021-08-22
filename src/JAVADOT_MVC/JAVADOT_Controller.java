package JAVADOT_MVC;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JAVADOT_Controller {
	// 변수선언
	public Stage stage;
	public Scene scene;
	public Node player;
	public Pane mainContainer = new Pane();
	public HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	public Point2D playerVelocity = new Point2D(0, 0);
	public Rectangle bg;
	public RotateTransition rotate = new RotateTransition();
	public boolean canJump = true;
	public boolean BGMPlay = true;

	// 클래스 참조 변수
	LevelData level = new LevelData(); // LevelData클래스 사용하기 위한 level객체 생성
	JAVADOT_Main mainClass = new JAVADOT_Main();


	/// 점프관련 변수, 메서드
	int jumpNumber;
	public Text jumpCount = new Text();
	public Button jumpCountButton = new Button();

	public void jumpPlayer() {
		
		if (canJump) {
			playerVelocity = playerVelocity.add(0, -36); 
			AudioClip jumpSound = new AudioClip( //점프시 효과음 출력
					Paths.get("/Users/coqoa/eclipse-workspace/JAVADOT_project/src/JAVADOT_MVC/source/pong.mp3").toUri()
					.toString());
			jumpSound.play();
			canJump = false;
		}
	}
	
	public void jumpData() {

		jumpNumber = 1; // 시작시 가지는 점프횟수 기본값
		jumpCount.setText("" + jumpNumber); // Text객체 화면에 출력
		jumpCount.setTranslateX(630); // 출력위치
		jumpCount.setTranslateY(150);
		jumpCount.setFont(Font.font("Brush Script MT", 50));
		jumpCount.setFill(Color.WHITE);
		jumpCount.setStroke(Color.DARKBLUE);
		jumpCount.setStrokeWidth(2);
		jumpCountButton.setTranslateX(10);
		jumpCountButton.setOpacity(0);
		jumpCountButton.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.SPACE && jumpNumber > 0 && canJump && player.getTranslateX() > -20) {
				jumpNumber--;
				jumpCount.setText("" + jumpNumber);
				jumpPlayer();
				if (!canJump) { // player객체를 회전시켜준다 
					rotate.setAxis(Rotate.Z_AXIS);
					rotate.setByAngle(180);
					rotate.setCycleCount(1);
					rotate.setDuration(Duration.millis(300));
					rotate.setAutoReverse(true);
					rotate.setNode(player);
					rotate.play();
				}
			}
		});
	}

	// 재시작 메서드
	public void restartGame() {
		player.setTranslateX(15);
		player.setTranslateY(550);
		level.blockContainer.setTranslateX(0);
		jumpData();
		for (Node energy : level.energys) { // player와 energy 충돌시 이동시킨 energy객체를 원위치
			if (energy.getTranslateY() > 3980) {
				energy.setTranslateY(energy.getTranslateY() - 4000);
			}
		}
	}
	


	// scene에 포함하는 mainContainer를 구성하는 메서드
	public Node mainPage() {
		// 맨 위에 위치해야 player객체가 door보다 앞에 표시됨
		level.levelData();

		// 게임프레임, 배경화면 색깔
		bg = new Rectangle(1280, 720, Color.LIGHTSKYBLUE);

		// jumpCount, jumpCountButton
		jumpData();

		// createObject (blockContainer)
		player = level.createObject(20, 600, 20, 20, Color.DARKVIOLET);

		mainContainer.getChildren().addAll(bg, level.blockContainer, jumpCount, jumpCountButton);
//		mainContainer.getChildren().addAll(bg, level.blockContainer);
		return mainContainer;
	}

	public boolean isPressed(KeyCode key) {
		// key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환
		return keys.getOrDefault(key, false);
	}
	
	public void moveBlock() {
		
	// player와 energy 충돌시 jumpNumber을 1증가시켜서 jumpCount로 출력하고, energy의 위치를 이동시켜서 화면에서 제외시킨다
		for (Node energy : level.energys) {
			if (player.getBoundsInParent().intersects(energy.getBoundsInParent())) {
				jumpNumber = jumpNumber + 1;
				jumpCount.setText("" + jumpNumber);
				energy.setTranslateY(energy.getTranslateY() + 4000);
			}
		}
		
	// restart메서드 실행 관련 객체
		for (Node reset : level.resets) { // 7 : reset블럭 닿으면 재시작
			if (player.getBoundsInParent().intersects(reset.getBoundsInParent())) {
				restartGame();
			}
		}
		for (Node resetDownBlock : level.resetDownBlocks) {
			// player와 block비교
			if (player.getBoundsInParent().intersects(resetDownBlock.getBoundsInParent())) {
				restartGame();
			}
			if (resetDownBlock.getTranslateY() < 730) {
				resetDownBlock.setTranslateY(resetDownBlock.getTranslateY() + 2); // 위에서 아래로 떨어지는 블럭
			} else {
				resetDownBlock.setTranslateY(resetDownBlock.getTranslateY() - 750); // 화면벗어나면 위치 변경
			}
		}
		for (Node resetDownBlockx2 : level.resetDownBlockx2s) {
			// player와 block비교
			if (player.getBoundsInParent().intersects(resetDownBlockx2.getBoundsInParent())) {
				restartGame();
			}
			if (resetDownBlockx2.getTranslateY() < 730) {
				resetDownBlockx2.setTranslateY(resetDownBlockx2.getTranslateY() + 3);
			} else {
				resetDownBlockx2.setTranslateY(resetDownBlockx2.getTranslateY() - 750);
			}
		}
		for (Node thunder : level.thunders) {
			if (player.getBoundsInParent().intersects(thunder.getBoundsInParent())) {
				restartGame();
			}
			if (thunder.getTranslateY() < 730) {
				thunder.setTranslateY(thunder.getTranslateY() + 3);
			} else {
				thunder.setTranslateY(thunder.getTranslateY() - 750);
			}
		}
		for (Node gasForm : level.gasForms) {
			if (gasForm.getTranslateY() > 0) {
				gasForm.setTranslateY(gasForm.getTranslateY() - 2);
			} else {
				gasForm.setTranslateY(gasForm.getTranslateY() + 730);
			}
		}
		for (Node gasFormx2 : level.gasFormx2s) {
			if (gasFormx2.getTranslateY() > 0) {
				gasFormx2.setTranslateY(gasFormx2.getTranslateY() - 3);
			} else {
				gasFormx2.setTranslateY(gasFormx2.getTranslateY() + 730);
			}
		}
	}

	// AnimationTimer로 매번 업데이트하는 메서드
	public void sceneUpdate() {
		
		// LEFT키를 누르고 player객체의 x값이 0보다 크다면 movePlayerX의 매개변수로 -14을 입력
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() > 0) {
			movePlayerX(-14);
		}
		
		// RIGHT키를 누르고 player객체의 위치 조건을 만족하면 movePlayerX의 매개변수로 14을 입력
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() > -20) {
			movePlayerX(14);
		}
		
		//player객체가 아래로 낙하해서 범위르 벗어나면 재시작
		if(player.getTranslateY() > 1000) {
			restartGame();
		}
		
		// esc버튼으로 재시작
		if (isPressed(KeyCode.ESCAPE)) {
			if (player.getTranslateX() > 1) {
				restartGame();
			}
		}

// 임시작성코드
		//player의 위치확인
		if (isPressed(KeyCode.C)) {
			if (player.getTranslateX() > 1) {
				System.out.println("player X 위치 : " + player.getTranslateX());
				System.out.println("player Y 위치 : " + player.getTranslateY());
				System.out.println("levelBlock위치 :" + level.blockContainer.getTranslateX());
			}
		}
		if (isPressed(KeyCode.L)) {
			if (player.getTranslateX() > 1) {
				level.blockContainer.setTranslateX(level.blockContainer.getTranslateX()-10);
			}
		}
		if (isPressed(KeyCode.K)) {
			if (player.getTranslateX() > 1) {
				level.blockContainer.setTranslateX(level.blockContainer.getTranslateX()+10);
			}
		}
		//player 이동
		if (isPressed(KeyCode.TAB)) {
			if (player.getTranslateX() > 1) {
				player.setTranslateX(17447);
				player.setTranslateY(200);
			}
		}
		
		// BGM끄고켜기 종료
		if (isPressed(KeyCode.F12)) { 
			if (BGMPlay) {
				mainClass.bgm.stop();
				BGMPlay = false;
			}
		}
		if (isPressed(KeyCode.F11)) { 
			if (BGMPlay==false) {
				mainClass.bgm.play();
				BGMPlay = true;
			}
		}
	// Stage이동단축키
		if (isPressed(KeyCode.DIGIT2)) { // 2 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(4575);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT3)) {  // 3 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(9070);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT4)) {  // 4 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(13570);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT5)) {  // 5 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(18750);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT6)) {  // 6 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(22500);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT7)) {  // 7 Stage
			if (player.getTranslateX() > -10) {
				player.setTranslateX(27050);
				player.setTranslateY(600);
				jumpData();
			}
		} else if (isPressed(KeyCode.DIGIT8)) {  // 임시저장(클리어화면)
			if (player.getTranslateX() > -10) {
				player.setTranslateX(31750);
				level.blockContainer.setTranslateX(-31104);
				player.setTranslateY(0);
			}
		//  시연을 위한 스테이지이동 단축키
		} else if (isPressed(KeyCode.F1)) {  
			if (player.getTranslateX() > -10) {
				player.setTranslateX(3010);
				player.setTranslateY(550);
			}
		} else if (isPressed(KeyCode.F2)) {  
			if (player.getTranslateX() > -10) {
				player.setTranslateX(8047);
				player.setTranslateY(0);
			}
		} else if (isPressed(KeyCode.F3)) {  // 
			if (player.getTranslateX() > -10) {
				player.setTranslateX(9760);
				player.setTranslateY(100);
			}
		} else if (isPressed(KeyCode.F4)) {  // 
			if (player.getTranslateX() > -10) {
				player.setTranslateX(16600);
				player.setTranslateY(150);
			}
		} else if (isPressed(KeyCode.F5)) {  // 
			if (player.getTranslateX() > -10) {
				player.setTranslateX(21714);
				player.setTranslateY(50);
			}
		} else if (isPressed(KeyCode.F6)) {  // 
			if (player.getTranslateX() > -10) {
				player.setTranslateX(24500);
				player.setTranslateY(0);
			}
		} else if (isPressed(KeyCode.F7)) {  // 
			if (player.getTranslateX() > -10) {
				player.setTranslateX(29800);
				player.setTranslateY(300);
			}
		}

		// playerVelocity의 y값이 10보다 작으면 y값 2씩추가(체공시간, 점프높이 조절)
		if (playerVelocity.getY() < 11) {
			playerVelocity = playerVelocity.add(0, 2);
		}
		// movePlayerY(int value)에 playerVelocity값을 매개변수로 전달
		movePlayerY((int) playerVelocity.getY());

		for (Node door : level.doors) { // door 충돌 체크
			if (player.getBoundsInParent().intersects(door.getBoundsInParent())) {
				if (player.getTranslateX() > 0 && player.getTranslateX() < 3870) { // 1번맵 범위내에서 충돌 
					player.setTranslateX(4575); // 1 -> 2
					player.setTranslateY(600);
					jumpData(); // 점프데이터 초기화 (횟수1로)

				} else if (player.getTranslateX() > 4490 && player.getTranslateX() < 8400) { // 2번맵 범위내에서 충돌
					player.setTranslateX(9070); // 2 -> 3
					player.setTranslateY(600);
					jumpData();

				} else if (player.getTranslateX() > 9000 && player.getTranslateX() < 12930) { // 3번맵 범위내에서 충돌
					player.setTranslateX(13570); // 3 -> 4
					player.setTranslateY(600);
					jumpData();

				} else if (player.getTranslateX() > 13550 && player.getTranslateX() < 18100) { // 4번맵 범위내에서 충돌
					player.setTranslateX(18750); // 4 -> 5
					player.setTranslateY(600);
					jumpData();

				} else if (player.getTranslateX() > 18700 && player.getTranslateX() < 22470) { // 5번맵 범위내에서 충돌
					player.setTranslateX(22500); // 5 -> 6
					player.setTranslateY(650);
					jumpData();

				} else if (player.getTranslateX() > 22480 && player.getTranslateX() < 26370) {// 6번맵 범위내에서 충돌
					player.setTranslateX(27050); // 6 -> 7
					player.setTranslateY(600);
					jumpData();
					
				//7번맵은 door대신 sunR객체와 충돌체크함
				}
			}
		}
		// 7번맵에서 player객체와 충돌시 클리어화면 출력하도록 구현
		for (Node sunR : level.sunRs) { 
			if (player.getBoundsInParent().intersects(sunR.getBoundsInParent())) {
				if (player.getTranslateX() > -10) {
					player.setTranslateX(31750);
					level.blockContainer.setTranslateX(-31104);
					player.setTranslateY(0);
				}
			}
		}
		
		//player객체를 움직이게하는 Block
		for (Node moveYDownBlock : level.moveYDownBlocks) { //내려가는 블럭
			if (moveYDownBlock.getTranslateY() < 730) {
				moveYDownBlock.setTranslateY(moveYDownBlock.getTranslateY() + 2);
			} else {
				moveYDownBlock.setTranslateY(moveYDownBlock.getTranslateY() - 750);
			}
		}
		for (Node moveYUpBlock : level.moveYUpBlocks) { //올라가는 블럭
			if (moveYUpBlock.getTranslateY() > 0) {
				moveYUpBlock.setTranslateY(moveYUpBlock.getTranslateY() - 2);
			} else {
				moveYUpBlock.setTranslateY(moveYUpBlock.getTranslateY() + 730);
			}
		}
		// player객체를 좌우로 움직이게하는 Block(무빙워크)
		for (Node moveRightBlock : level.moveRightBlocks) {
			if (player.getBoundsInParent().intersects(moveRightBlock.getBoundsInParent())) {
				player.setTranslateX(player.getTranslateX() + 2);
			}
		}
		for (Node moveLeftBlock : level.moveLeftBlocks) {
			if (player.getBoundsInParent().intersects(moveLeftBlock.getBoundsInParent())) {
				player.setTranslateX(player.getTranslateX() - 3);
			}
		}
		//player객체가 밟을수 있는 구름(아래에서 통과가능, 점프가능)
		for (Node darkCloud : level.darkClouds) {
			if (player.getBoundsInParent().intersects(darkCloud.getBoundsInParent())) {
				player.setTranslateY(player.getTranslateY() - 5); // 구름위에서 player객체가 벌벌떨게 구현
				canJump = true;
			}
		}
		for (Node lightCloud : level.lightClouds) {
			if (player.getBoundsInParent().intersects(lightCloud.getBoundsInParent())) {
				player.setTranslateY(player.getTranslateY() - 5); // 구름위에서 player객체가 벌벌떨게 구현
					canJump = true;
			}
		}
		// player객체가 밟으면 낙하속도가 느려지고 점프를 할 수 도록 하는 구름
		for (Node lineCloud : level.lineClouds) {
			if (player.getBoundsInParent().intersects(lineCloud.getBoundsInParent())) {
				player.setTranslateY(player.getTranslateY() - 2);
				canJump = true;
			}
		}
		// player객체의 낙하속도만 줄여주고 점프는 불가능한 공기
		for (Node air : level.airs) {
			if (player.getBoundsInParent().intersects(air.getBoundsInParent())) {
				player.setTranslateY(player.getTranslateY() - 3);
			}
		}
	}

	public void moveCamera() {
		// 카메라이동 player의 위치 : 640 ~ (전체화면 - 640)사이일때
		// blockContainer의 X값 이동(-(player의X값-640만))
		if (player.getTranslateX() > 640 && player.getTranslateX() < level.levelWidth - 640) {
			level.blockContainer.setTranslateX(-(player.getTranslateX() - 640));
		}
	}

	public void movePlayerX(int value) {

		// LEFT=false, RIGHT=true , player객체를 좌,우로 움직일때 객체들과 상호작용을 구현
		boolean movingRight = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {

			// LevelData클래스의 blocks ArrayList에 넣어둔 block를 하나씩 실행
			for (Node block : level.blocks) { // block : 초록색 벽
				if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
					if (movingRight) {// RIGHT
						// RIGHT입력시 player의 오른쪽경계와 block의 왼쪽경계가 맞닿았을때
						if (player.getTranslateX() + 20 == block.getTranslateX()) {
							// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 block은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < block.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
							} else{
//								// 붙었을때 player객체의 투명도를 변화시키는 메서드
								player.setOpacity(0.3);
							}
						//충돌시 멈추도록 하는 메서드
						player.setTranslateY(player.getTranslateY() - 1);
						return;
						}
					} else { // LEFT
					// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
						if (player.getTranslateX() == block.getTranslateX() + 10) {
							// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 block은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < block.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
							} else {
//								// 붙었을때 player객체의 투명도를 변화시키는 메서드
								player.setOpacity(0.3);
							}
						player.setTranslateY(player.getTranslateY() - 1);
						return;
						}
					}
				}
				//벽에서 떨어졌을때 다시 원래의 투명도로 돌려준다
				player.setOpacity(1.0);
			}

			for (Node slipBlock : level.slipBlocks) { // slipBlock : 붙지않는 벽
				if (player.getBoundsInParent().intersects(slipBlock.getBoundsInParent())) {
					if (movingRight) { // RIGHT
						// RIGHT입력시 player의 오른쪽경계와 slipBlock의 왼쪽경계가 맞닿았을때
						if (player.getTranslateX() + 20 == slipBlock.getTranslateX()) {
							// player의 y값+10이 slipBlock의 y값보다 작다면 y값을 -10해준다 (한칸짜리 slipBlock은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < slipBlock.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
						} else {
							player.setTranslateX(player.getTranslateX() - 3);// 벽에 안달라붙고 미끄러지게 하는코드
						}
						return;
						}
					} else { // LEFT
						// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
						if (player.getTranslateX() == slipBlock.getTranslateX() + 10) {
							// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 slipBlock은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < slipBlock.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
							} else {
								player.setTranslateX(player.getTranslateX() + 3);// 벽에 안달라붙고 미끄러지게 하는코드
							}
							return;
						}
					}
				}
			}
			for (Node sandBlock : level.sandBlocks) { // sandBlock : 모래블럭
				if (player.getBoundsInParent().intersects(sandBlock.getBoundsInParent())) {
					if (movingRight) { // RIGHT
						// RIGHT입력시 player의 오른쪽경계와 sandBlock의 왼쪽경계가 맞닿았을때
						if (player.getTranslateX() + 20 == sandBlock.getTranslateX()) {
							// player의 y값+10이 sandBlock의 y값보다 작다면 y값을 -10해준다 (한칸짜리 sandBlock은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < sandBlock.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
							} else {
								player.setTranslateX(player.getTranslateX() - 3);// 벽에 안달라붙고 미끄러지게 하는코드
							}
							return;
						}
					} else { // LEFT
						// LEFT입력시 player의 왼쪽경계와 block의 오른쪽 경계가 맞닿았을때
						if (player.getTranslateX() == sandBlock.getTranslateX() + 10) {
							// player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다 (한칸짜리 sandBlock은 뛰어넘게 해줌)
							if (player.getTranslateY() + 10 < sandBlock.getTranslateY()) {
								player.setTranslateY(player.getTranslateY() - 10);
							} else {
								player.setTranslateX(player.getTranslateX() + 3);// 벽에 안달라붙고 미끄러지게 하는코드
							}
							return;
						}
					}
				}
			}
			for (Node moveYDownBlock : level.moveYDownBlocks) { // 아래로 움직이는 벽
				if (player.getBoundsInParent().intersects(moveYDownBlock.getBoundsInParent())) {
					if (movingRight) {
						// RIGHT입력시 player의 오른쪽경계와 moveYDownBlock의 왼쪽경계가 맞닿았을때 멈추게함
						if (player.getTranslateX() + 20 == moveYDownBlock.getTranslateX()) {
							player.setTranslateX(player.getTranslateX() - 1);
							return;
						}
					} else {
						// LEFT입력시 player의 왼쪽경계와 moveYDownBlock의 오른쪽 경계가 맞닿았을때 멈추게 함
						if (player.getTranslateX() == moveYDownBlock.getTranslateX() + 10) {
							player.setTranslateX(player.getTranslateX() + 1);
							return;
						}
					}
				}
			}
			for (Node moveYUpBlock : level.moveYUpBlocks) { // moveYUpBlock : 위로 움직이는 벽
				if (player.getBoundsInParent().intersects(moveYUpBlock.getBoundsInParent())) {
					if (movingRight) {
						// RIGHT입력시 player의 오른쪽경계와 moveYUpBlock의 왼쪽경계가 맞닿았을때 멈추게함
						if (player.getTranslateX() + 20 == moveYUpBlock.getTranslateX()) {
							player.setTranslateX(player.getTranslateX() - 1);
							return;
						}
					} else {
						// LEFT입력시 player의 왼쪽경계와 moveYUpBlock의 오른쪽 경계가 맞닿았을때 멈추게 함
						if (player.getTranslateX() == moveYUpBlock.getTranslateX() + 10) {
							player.setTranslateX(player.getTranslateX() + 1);
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
							player.setTranslateX(player.getTranslateX() - 1);
							return;
						}
					} else {
						// LEFT입력시 player의 왼쪽경계와 layout의 오른쪽 경계가 맞닿았을때 멈추게 함
						if (player.getTranslateX() == layout.getTranslateX() + 10) {
							player.setTranslateX(player.getTranslateX() + 1);
							return;
						}
					}
				}
			}
			// RIGHT버튼을 누르면 player객체의 x위치를 +0.5만큼씩, LEFT버튼을 누르면 x위치를 -0.5만큼씩이동
			player.setTranslateX(player.getTranslateX() + (movingRight ? 0.5 : -0.5));
		}
	}

	public void movePlayerY(int value) { // player객체가 상,하로 움직일때 객체들과 상호작용을 구현
		boolean movingDown = value > 0;

		for (int i = 0; i < Math.abs(value); i++) {

			for (Node block : level.blocks) {
				// player와 block비교
				if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥면과 block의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == block.getTranslateY()) {
							// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 1); 
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == block.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 2);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}

			for (Node slipBlock : level.slipBlocks) {
				// player와 slipBlock비교
				if (player.getBoundsInParent().intersects(slipBlock.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥변과 slipBlock의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == slipBlock.getTranslateY()) {
							// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 1);
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == slipBlock.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 2);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}
			for (Node layout : level.layouts) {
				// player와 slipBlock비교
				if (player.getBoundsInParent().intersects(layout.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥변과 slipBlock의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == layout.getTranslateY()) {
							// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 1);
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == layout.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 2);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}

			for (Node sandBlock : level.sandBlocks) {
				// player와 sandBlock비교
				if (player.getBoundsInParent().intersects(sandBlock.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥변과 sandBlock의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == sandBlock.getTranslateY()) {
							// player객체의 y값을 -1해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 1);
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == sandBlock.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 2);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}

			for (Node longJump : level.longJumps) {
				// player와 longJump비교
				if (player.getBoundsInParent().intersects(longJump.getBoundsInParent())) {

					if (movingDown) {
						// player의 바닥변과 longJump의 윗면이 충돌하면
						if (player.getTranslateY() + 19 == longJump.getTranslateY()) {
							// 충돌시 스프링을 밟은효과
							playerVelocity = playerVelocity.add(0, -50);
							// 효과음
							AudioClip longJumpSound = new AudioClip(Paths.get(
									"/Users/coqoa/eclipse-workspace/JAVADOT_project/src/JAVADOT_MVC/source/longJump.mp3")
									.toUri().toString());
							longJumpSound.play();
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == longJump.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 1);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}

			for (Node moveYDownBlock : level.moveYDownBlocks) {
				// player와 moveYDownBlock비교
				if (player.getBoundsInParent().intersects(moveYDownBlock.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥변과 moveYDownBlock의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == moveYDownBlock.getTranslateY()) {
							// player객체의 y값을 -4 해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 4); 
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == moveYDownBlock.getTranslateY() + 4) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 2);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}

			for (Node moveYUpBlock : level.moveYUpBlocks) {
				// player와 moveYUpBlock비교
				if (player.getBoundsInParent().intersects(moveYUpBlock.getBoundsInParent())) {
					if (movingDown) {
						// player의 바닥변과 moveYUpBlock의 윗면이 충돌하면
						if (player.getTranslateY() + 20 == moveYUpBlock.getTranslateY()) {
							// player객체의 y값을 -8해주고 점프버튼이 작동하도록 해줌
							player.setTranslateY(player.getTranslateY() - 3);
							canJump = true;
							return;
						}
					} else {
						// 점프시 윗벽이 막혀있으면 더 안올라가짐
						if (player.getTranslateY() == moveYUpBlock.getTranslateY() + 10) {
							// 윗벽에 막힌 순간 playerVelocity변수의 Y값을 구해서 그 값을 다시 돌려주도록 구현 -> 벽에서 바로 떨어질 수 있게
							playerVelocity = playerVelocity.add(0, -playerVelocity.getY() + 1);
							// 윗벽에 붙었을때 점프버튼 작동x
							canJump = false;
							return;
						}
					}
				}
			}
			// player객체의 위치 = player객체의 이동좌표 + movingDown이 참이라면 +1 거짓이면 -1
			player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
		}
	}

	public void gameStartButton(ActionEvent event) throws IOException, InterruptedException {

		Thread mainPageThread = new Thread() { 
			@Override
			public void run() {
				Platform.runLater(() -> {
					mainPage();
				});
			}
		};
		mainPageThread.setPriority(10);
		mainPageThread.setDaemon(true);
		mainPageThread.start();
		
		AnimationTimer sceneUpdateTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				sceneUpdate();
			}
		};
		sceneUpdateTimer.start();
		
		Thread cameraThread = new Thread() {
			@Override
			public void run() {
				AnimationTimer moveCameraTimer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						moveCamera();
					}
				};
				moveCameraTimer.start();
			}
		};
		Platform.runLater(cameraThread);
		mainPageThread.setPriority(7);
		cameraThread.setDaemon(true);
		cameraThread.start();
		
		Thread moveBlockThread = new Thread() {
			@Override
			public void run() {
				AnimationTimer moveBlockTimer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						moveBlock();
					}
				};
				moveBlockTimer.start();
			}
		};
		Platform.runLater(moveBlockThread);
		moveBlockThread.setDaemon(true);
		moveBlockThread.start();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(mainContainer, 1280, 720);
		stage.setScene(scene);
		stage.show();

		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
	}
}

class LevelData { // 객체생성관련 코드모음 (player, block, energy, door, 등등)
	public Pane blockContainer = new Pane();
	public int levelWidth;
	//상호작용노드
	public ArrayList<Node> blocks = new ArrayList<Node>();
	public ArrayList<Node> energys = new ArrayList<Node>();
	public ArrayList<Node> doors = new ArrayList<Node>();
	public ArrayList<Node> slipBlocks = new ArrayList<Node>();
	public ArrayList<Node> longJumps = new ArrayList<Node>();
	public ArrayList<Node> resets = new ArrayList<Node>();
	public ArrayList<Node> moveYDownBlocks = new ArrayList<Node>();
	public ArrayList<Node> moveYUpBlocks = new ArrayList<Node>();
	public ArrayList<Node> layouts = new ArrayList<Node>();
	public ArrayList<Node> resetDownBlocks = new ArrayList<Node>();
	public ArrayList<Node> resetDownBlockx2s = new ArrayList<Node>();
	public ArrayList<Node> moveRightBlocks = new ArrayList<Node>();
	public ArrayList<Node> moveLeftBlocks = new ArrayList<Node>();
	public ArrayList<Node> darkClouds = new ArrayList<Node>();
	public ArrayList<Node> lightClouds = new ArrayList<Node>();
	public ArrayList<Node> lineClouds = new ArrayList<Node>();
	public ArrayList<Node> thunders = new ArrayList<Node>();
	public ArrayList<Node> airs = new ArrayList<Node>();
	public ArrayList<Node> sandBlocks = new ArrayList<Node>();
	public ArrayList<Node> sunRs = new ArrayList<Node>();
	
	//디자인관련노드
	public ArrayList<Node> gasForms = new ArrayList<Node>();
	public ArrayList<Node> gasFormx2s = new ArrayList<Node>();
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
		for (int i = 0; i < ObjectData1.LEVEL1.length; i++) {
			String line = ObjectData1.LEVEL1[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {

					case '0':
						break;
					case '1':
						Node block = createObject(j * 10, i * 10, 10, 10, Color.LIGHTGREEN);
						blocks.add(block);
						break;
					case '2':
						Node energy = createObject(j * 10, i * 10, 6, 9, Color.DARKVIOLET);
						energys.add(energy);
						break;
					case '3':
						Node door = createObject(j * 10, i * 10, 10, 10, Color.BLACK);
						doors.add(door);
						break;
					case '4':
						Node slipBlock = createObject(j * 10, i * 10, 10, 10, Color.SLATEBLUE);
						slipBlocks.add(slipBlock);
						break;
					case '5':
						Node longJump = createObject(j * 10, i * 10, 10, 10, Color.CRIMSON);
						longJumps.add(longJump);
						break;
					case '7':
						Node reset = createObject(j * 10, i * 10, 10, 10, Color.LIGHTCYAN);
						resets.add(reset);
						break;
					case '8':
						Node moveYDownBlock = createObject(j * 10, i * 10, 10, 10, Color.TEAL);
						moveYDownBlocks.add(moveYDownBlock);
						break;
					case '9':
						Node moveYUpBlock = createObject(j * 10, i * 10, 10, 10, Color.TEAL);
						moveYUpBlocks.add(moveYUpBlock);
						break;
					case 'L':
						Node layout = createObject(j * 10, i * 10, 10, 10, Color.TRANSPARENT);
						layouts.add(layout);
						break;
					case 'M':
						Node resetDownBlock = createObject(j * 10, i * 10, 10, 10, Color.CYAN);
						resetDownBlocks.add(resetDownBlock);
						break;
					case 'H':
						Node resetDownBlockx2 = createObject(j*10, i*10, 10, 10, Color.CYAN);
						resetDownBlockx2s.add(resetDownBlockx2);
						break;
					case 'N':
						Node moveRightBlock = createObject(j * 10, i * 10, 10, 10, Color.LIGHTSALMON);
						moveRightBlocks.add(moveRightBlock);
						break;
					case 'n':
						Node moveLeftBlock = createObject(j * 10, i * 10, 10, 10, Color.SALMON);
						moveLeftBlocks.add(moveLeftBlock);
						break;
	
					case 'C':
						Node darkCloud = createObject(j * 10, i * 10, 10, 10, Color.DARKGRAY);
						darkClouds.add(darkCloud);
						break;
					case 'c':
						Node lightCloud = createObject(j * 10, i * 10, 10, 10, Color.GAINSBORO);
						lightClouds.add(lightCloud);
						break;
					case 'T':
						Node thunder = createObject(j * 10, i * 10, 10, 10, Color.YELLOW);
						thunders.add(thunder);
						break;
						
					case 'E':
						Node lineCloud = createObject(j * 10, i * 10, 10, 10, Color.DODGERBLUE);
						lineClouds.add(lineCloud);
						break;
					case 'e':
						Node air = createObject(j * 10, i * 10, 10, 10, Color.POWDERBLUE);
						airs.add(air);
						break;
					case 'X':
						Node sandBlock = createObject(j * 10, i * 10, 10, 10, Color.WHEAT);
						sandBlocks.add(sandBlock);
						break;
					case 'R':
						Node sunR = createObject(j * 10, i * 10, 10, 10, Color.RED);
						sunRs.add(sunR);
						break;
	
					
					
					case 'm':
						Node gasForm = createObject(j * 8, i * 8, 10, 10, Color.WHITE);
						gasForms.add(gasForm);
						break;
					case 'h':
						Node gasFormx2 = createObject(j * 10, i * 10, 10, 10, Color.WHITE);
						gasFormx2s.add(gasFormx2);
						break;
						
					case 'Y':
						Node sunY = createObject(j * 10, i * 10, 10, 10, Color.KHAKI);
						bgObject.add(sunY);
						break;
					case 'W':
						Node cloud = createObject(j * 10, i * 10, 10, 10, Color.WHITE);
						bgObject.add(cloud);
						break;
					case 'G':
						Node darkGreen = createObject(j * 10, i * 10, 10, 10, Color.DARKGREEN);
						bgObject.add(darkGreen);
						break;
					case 'g':
						Node lightGreen = createObject(j * 10, i * 10, 10, 10, Color.MEDIUMSEAGREEN);
						bgObject.add(lightGreen);
						break;
					case 'B':
						Node darkBrown = createObject(j * 10, i * 10, 10, 10, Color.SADDLEBROWN);
						bgObject.add(darkBrown);
						break;
					case 'b':
						Node lightBrown = createObject(j * 10, i * 10, 10, 10, Color.DARKGOLDENROD);
						bgObject.add(lightBrown);
						break;
					case 'K':
						Node black = createObject(j * 10, i * 10, 10, 10, Color.BLACK);
						bgObject.add(black);
						break;
					case 't':
						Node TOMATO = createObject(j * 10, i * 10, 10, 10, Color.TOMATO);
						bgObject.add(TOMATO);
						break;
					case 'S':
						Node salmon = createObject(j * 10, i * 10, 10, 10, Color.SALMON);
						bgObject.add(salmon);
						break;
					case 's':
						Node lightSalmon = createObject(j * 10, i * 10, 10, 10, Color.LIGHTSALMON);
						bgObject.add(lightSalmon);
						break;
					case 'y':
						Node navajowhite = createObject(j * 10, i * 10, 10, 10, Color.NAVAJOWHITE);
						bgObject.add(navajowhite);
						break;
					case 'u':
						Node THISTLE = createObject(j * 10, i * 10, 10, 10, Color.ORCHID);
						bgObject.add(THISTLE);
						break;
					case 'O':
						Node gold = createObject(j * 10, i * 10, 10, 10, Color.CORNFLOWERBLUE);
						bgObject.add(gold);
						break;
					case 'q':
						Node sand = createObject(j * 10, i * 10, 10, 10, Color.TAN);
						bgObject.add(sand);
						break;	
				}
			}
		}
		return blockContainer;
	}

}