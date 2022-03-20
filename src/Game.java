import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application {
	private BooleanProperty isAPressed = new SimpleBooleanProperty(false);
	private BooleanProperty isDPressed = new SimpleBooleanProperty(false);
	private BooleanProperty isSpacePressed = new SimpleBooleanProperty(false);
	private BooleanBinding spaceAndA = isSpacePressed.and(isAPressed);
	private BooleanBinding spaceAndD = isSpacePressed.and(isDPressed);
	static MapLayout newMapLayout;
	static player sprite;
	static double SceneWidth;
	static double SceneHeight;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Terrain gen test");

		String playerImg = getClass().getClassLoader().getResource("resources/sprite.png").toString();
		String groundImg = getClass().getClassLoader().getResource("resources/groundTexture.png").toString();
		String boostImg = getClass().getClassLoader().getResource("resources/boostTexture.png").toString();
		String coinImg = getClass().getClassLoader().getResource("resources/devincoin.png").toString();
		String enemyImg = getClass().getClassLoader().getResource("resources/enemyBucket.png").toString();

		Image paddleOneImg = new Image(playerImg, 25, 25, true, true);
		Image groundTextureImg = new Image(groundImg, 50, 50, true, true);
		Image boostTextureImg = new Image(boostImg, 50, 50, true, true);
		Image coinTextureImg = new Image(coinImg, 25, 25, true, true);
		Image enemyTextureImg = new Image(enemyImg, 50, 50, true, true);

		BorderPane root = new BorderPane();
		Pane layerPane = new Pane();
		layerPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		layerPane.setPrefSize(100, 100);

		Scene scene = new Scene(root, 1000, 1000);
		SceneWidth = scene.getWidth();
		SceneHeight = scene.getHeight();

		newMapLayout = new MapLayout();
		Random rand = new Random();

		testWorld newWorld = new testWorld();
		root.getChildren().add(newWorld);

		newWorld.start();

		primaryStage.setScene(scene);
		primaryStage.setFullScreenExitHint("");
		primaryStage.show();

		newWorld.requestFocus();

		sprite = new player(paddleOneImg);
		newWorld.add(sprite);

		// creates level 0
		newMapLayout.addToLevel(storeMapInfo(scene, newWorld, groundTextureImg));
		// adds level 0 boosts
		newMapLayout
				.setBoostSpots(placeBoosts(getBoostSpots(newMapLayout.getValidBoostSpot(newMapLayout.getLevelMap(0))),
						newMapLayout.getLevel(0), newWorld, boostTextureImg));
		// adds level 0 coins
		newMapLayout.addToCoinList(placeCoins(newWorld, coinTextureImg, scene, newMapLayout.getLevelMap(0)));
		// adds level 0 enemies
		placeEnemy(newWorld, enemyTextureImg, scene, newMapLayout.getLevelMap(0));

		// gets level 0 spawn
		ArrayList<int[]> firstSpawn = getSpawnPositions(scene, newMapLayout.getLevelMap(0));
		int[] randomCoordForSprite = firstSpawn.get(rand.nextInt(firstSpawn.size() - 1));
		sprite.setX(randomCoordForSprite[0] + sprite.getWidth() / 2);
		sprite.setY(randomCoordForSprite[1]);

		// handles key events
		newWorld.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.A) {
					newWorld.addKeyToKeyDownTracker(KeyCode.A);
					isAPressed.set(true);
				} else if (ke.getCode() == KeyCode.D) {
					newWorld.addKeyToKeyDownTracker(KeyCode.D);
					isDPressed.set(true);
				} else if (ke.getCode() == KeyCode.SPACE) {
					if (newWorld.isGameWon()) {
						newWorld.getChildren().remove(newWorld.getGameWonText());
						newWorld.getChildren().remove(newWorld.getGameWonTextUnder());
						newWorld.start();
						newWorld.setGameWon(false);
						newWorld.setCoinsToWinGame(0);
					}
					newWorld.addKeyToKeyDownTracker(KeyCode.SPACE);
					isSpacePressed.set(true);
				}
			}
		});

		newWorld.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.A) {
					newWorld.removeKeyFromKeyDownTracker(KeyCode.A);
					isAPressed.set(false);
				} else if (ke.getCode() == KeyCode.D) {
					newWorld.removeKeyFromKeyDownTracker(KeyCode.D);
					isDPressed.set(false);
				} else if (ke.getCode() == KeyCode.SPACE) {
					newWorld.removeKeyFromKeyDownTracker(KeyCode.SPACE);
					isSpacePressed.set(false);
				}
			}
		});

		// Handles when both a space and a horizontal movement key a pressed at the same
		// time
		spaceAndA.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				sprite.setIsAandSpacePressed(observable.getValue());
			}
		});

		spaceAndD.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				sprite.setIsDandSpacePressed(observable.getValue());
			}
		});

		sprite.getIsTopSend().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				System.out.println("Level: " + newMapLayout.getMapLevel());
				// removes the old bricks from the level below and increases the level count
				removeBrickFromLevel(newMapLayout.getLevel(newMapLayout.getMapLevel()), newWorld);
				newMapLayout.incrementMapLevel();

				// checks if the level exists if not creates a new level
				if (newMapLayout.getMapLevel() > newMapLayout.getLevelsList().size() - 1) {
					newMapLayout.addToLevel(storeMapInfo(scene, newWorld, groundTextureImg));
				} else {
					placeOldLevel(newMapLayout.getLevel(newMapLayout.getMapLevel()), newWorld);
				}

				// spawns the player on a block in the bottom 3/4th of the map
				ArrayList<int[]> firstSpawn = getSpawnPositions(scene,
						newMapLayout.getLevelMap(newMapLayout.getMapLevel()));
				int[] randomCoordForSprite = firstSpawn.get(rand.nextInt(firstSpawn.size() - 1));
				sprite.setX(randomCoordForSprite[0] + sprite.getWidth() / 2);
				sprite.setY(randomCoordForSprite[1]);

				// removes the old boost pads(could be other boosts in the future also) and adds
				// new boots that fit the level
				removePowerups(newWorld);
				newMapLayout.setBoostSpots(placeBoosts(
						getBoostSpots(
								newMapLayout.getValidBoostSpot(newMapLayout.getLevelMap(newMapLayout.getMapLevel()))),
						newMapLayout.getLevel(newMapLayout.getMapLevel()), newWorld, boostTextureImg));

				if (newMapLayout.getMapLevel() > newMapLayout.getCoinsList().size() - 1) {
					newMapLayout.addToCoinList(placeCoins(newWorld, coinTextureImg, scene,
							newMapLayout.getLevelMap(newMapLayout.getMapLevel())));
				} else {
					placeOldCoins(newMapLayout.getCoinFromLevel(newMapLayout.getMapLevel()), newWorld);
				}
				placeEnemy(newWorld, enemyTextureImg, scene, newMapLayout.getLevelMap(newMapLayout.getMapLevel()));

			}

		});

		sprite.getIsBottomSend().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newMapLayout.getMapLevel() != 0) {
					// decreases the level count
					newMapLayout.decrementMapLevel();

					// removes the previous levels bricks and places the corresponding levels bricks
					removeBrickFromLevel(newMapLayout.getLevel(newMapLayout.getMapLevel() + 1), newWorld);
					placeOldLevel(newMapLayout.getLevel(newMapLayout.getMapLevel()), newWorld);

					// spawns the player at the top of the screen
					sprite.setX(sprite.getX());
					sprite.setY(10);

					// removes the old boost pads(could be other boosts in the future also) and adds
					// new boots that fit the level
					removePowerups(newWorld);
					newMapLayout.setBoostSpots(placeBoosts(
							getBoostSpots(newMapLayout
									.getValidBoostSpot(newMapLayout.getLevelMap(newMapLayout.getMapLevel()))),
							newMapLayout.getLevel(newMapLayout.getMapLevel()), newWorld, boostTextureImg));
					placeOldCoins(newMapLayout.getCoinFromLevel(newMapLayout.getMapLevel()), newWorld);
					placeEnemy(newWorld, enemyTextureImg, scene, newMapLayout.getLevelMap(newMapLayout.getMapLevel()));

				} else {
					newWorld.stop();
					GameText gameOverText = new GameText("Game Over", Color.RED);
					GameText gameOverTextUnder = new GameText("You Fell To The Very Bottom", Color.RED);
					gameOverTextUnder.setX(scene.getWidth() / 2 - gameOverTextUnder.getBoundsInLocal().getWidth() / 2);
					gameOverTextUnder.setY(scene.getHeight() / 2 - gameOverTextUnder.getBoundsInLocal().getHeight() / 2
							+ (gameOverText.getBoundsInLocal().getHeight() + 20));
					gameOverText.setX(scene.getWidth() / 2 - gameOverText.getBoundsInLocal().getWidth() / 2);
					gameOverText.setY(scene.getHeight() / 2 - gameOverText.getBoundsInLocal().getHeight() / 2);
					newWorld.getChildren().add(gameOverText);
					newWorld.getChildren().add(gameOverTextUnder);
					System.out.println("Hit the very bottom");
				}
			}

		});

	}

	public static player getSprite() {
		return sprite;
	}

	// creates the map and stores map info
	public ArrayList<Brick> storeMapInfo(Scene scene, testWorld worldTarget, Image texture) {
		ArrayList<Brick> brickStoreArr = new ArrayList<Brick>();
		ArrayList<ArrayList<Integer>> mainSendArr = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempArr;
		for (int i = 0; i < scene.getHeight() / 50; i++) {
			tempArr = new ArrayList<Integer>();
			for (int j = 0; j < scene.getWidth() / 50; j++) {
				Boolean isFilled = false;
				Random rand = new Random();
				if (rand.nextInt(7) == 4) {
					tempArr.add(1);
					Brick newBrick = new Brick(texture);
					newBrick.setX(newBrick.getWidth() * j);
					newBrick.setY(newBrick.getHeight() * i);
					worldTarget.add(newBrick);
					brickStoreArr.add(newBrick);
					isFilled = true;
				}
				if (!isFilled) {
					tempArr.add(0);
				}

			}
			mainSendArr.add(tempArr);
		}
		newMapLayout.addToLevelMap(mainSendArr);
		return brickStoreArr;
	}

	// adds the old level back using existing bricks from the map layout class
	public void placeOldLevel(ArrayList<Brick> bricks, testWorld worldTarget) {
		for (Brick elem : bricks) {
			if (!worldTarget.getChildren().contains(elem)) {
				worldTarget.add(elem);
			}
		}
	}

	public void placeOldCoins(ArrayList<Coin> coins, testWorld worldTarget) {
		for (Coin elem : coins) {
			if (!worldTarget.getChildren().contains(elem)) {
				worldTarget.add(elem);
			}
		}
	}

	// converts grid points into tangible x and y coords
	public int[] plotToCoord(int[] plot) {
		int[] sendVal = new int[2];
		sendVal[0] = plot[0] * 50;
		sendVal[1] = plot[1] * 50;
		return sendVal;
	}

	public void removeBrickFromLevel(ArrayList<Brick> bricks, testWorld world) {
		for (Brick elem : bricks) {
			world.remove(elem);
		}
	}

	// gets player spawn points
	public ArrayList<int[]> getSpawnPositions(Scene scene, ArrayList<ArrayList<Integer>> currLevel) {
		ArrayList<int[]> spawnCoordList = new ArrayList<int[]>();
		for (int[] elem : newMapLayout.getValidSpots(currLevel)) {
			int[] realCoordVals = plotToCoord(elem);
			if (realCoordVals[1] > scene.getHeight() * 3 / 4) {
				spawnCoordList.add(realCoordVals);
			}
		}

		return spawnCoordList;
	}

	// gets points which are empty but above a block
	public ArrayList<int[]> getFreeCoordPositions(Scene scene, ArrayList<ArrayList<Integer>> currLevel) {
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		for (int[] elem : newMapLayout.getValidSpots(currLevel)) {
			int[] realCoordVals = plotToCoord(elem);
			coordList.add(realCoordVals);

		}
		return coordList;
	}

	// gets spots where a boost can be placed based on the criteria of having 4
	// empty blocks above the boost spot
	public ArrayList<int[]> getBoostSpots(ArrayList<int[]> validSpots) {
		ArrayList<int[]> storeArr = new ArrayList<int[]>();
		Random rand = new Random();
		boolean notFinished = true;
		int counter = 0;
		while (notFinished) {
			int[] coord = validSpots.get(rand.nextInt(validSpots.size() - 1));
			storeArr.add(coord);
			counter++;
			if (counter == 3) {
				notFinished = false;
			}
		}
		return storeArr;
	}

	public ArrayList<BoostPad> placeBoosts(ArrayList<int[]> validSpots, ArrayList<Brick> currentMap,
			testWorld worldTarget, Image boostImage) {
		ArrayList<BoostPad> padSpots = new ArrayList<BoostPad>();
		for (Brick elem : currentMap) {
			for (int i = 0; i < validSpots.size(); i++) {
				int[] coordPlot = plotToCoord(validSpots.get(i));
				if (elem.getX() == coordPlot[0] && elem.getY() == coordPlot[1]) {
					worldTarget.getChildren().remove(elem);
					BoostPad newPad = new BoostPad(boostImage);
					newPad.setX(elem.getX());
					newPad.setY(elem.getY());
					worldTarget.remove(elem);
					padSpots.add(newPad);
					worldTarget.add(newPad);
				}
			}
		}
		return padSpots;
	}

	public void removePowerups(testWorld worldTarget) {
		ArrayList<Object> objToRemove = new ArrayList<Object>();
		for (Object elem : worldTarget.getChildren()) {
			if (elem.getClass() == BoostPad.class || elem.getClass() == Coin.class || elem.getClass() == Enemy.class
					|| elem.getClass() == Missle.class) {
				objToRemove.add(elem);
			}
		}
		for (Object elem : objToRemove) {
			worldTarget.getChildren().remove(elem);
		}
	}

	public ArrayList<Coin> placeCoins(testWorld worldTarget, Image img, Scene scene,
			ArrayList<ArrayList<Integer>> currLevel) {
		ArrayList<Coin> coinArr = new ArrayList<Coin>();
		Random rand = new Random();
		ArrayList<int[]> coords = getFreeCoordPositions(scene, currLevel);
		for (int[] elem : coords) {
			if (rand.nextInt(10) == 9) {
				Coin coin = new Coin(img);
				coin.setX(elem[0] + coin.getWidth() / 2);
				coin.setY(elem[1] + coin.getHeight());
				coinArr.add(coin);
				worldTarget.getChildren().add(coin);
			}
		}
		return coinArr;
	}

	public void placeEnemy(testWorld worldTarget, Image img, Scene scene, ArrayList<ArrayList<Integer>> currLevel) {
		Random rand = new Random();
		ArrayList<int[]> coords = getFreeCoordPositions(scene, currLevel);
		ArrayList<int[]> coordsWithOutBoosts = new ArrayList<int[]>();
		ArrayList<int[]> coordsWithOutBoostsAndCoins = new ArrayList<int[]>();
		int spawnAmount = 0;
		for (int[] elem : coords) {
			if (elem[0] != newMapLayout.getBoostSpots().get(0).getX()
					&& elem[1] != newMapLayout.getBoostSpots().get(0).getY()
					&& elem[0] != newMapLayout.getBoostSpots().get(1).getX()
					&& elem[1] != newMapLayout.getBoostSpots().get(1).getY()
					&& elem[0] != newMapLayout.getBoostSpots().get(2).getX()
					&& elem[1] != newMapLayout.getBoostSpots().get(2).getY()) {
				coordsWithOutBoosts.add(elem);
			}
		}
		for (int i = 0; i < coordsWithOutBoosts.size(); i++) {
			for (int j = 0; j < newMapLayout.getCoinFromLevel(newMapLayout.getMapLevel()).size(); j++) {
				if ((coordsWithOutBoosts.get(i)[0] != newMapLayout.getCoinFromLevel(newMapLayout.getMapLevel()).get(j)
						.getX()
						&& coordsWithOutBoosts.get(i)[1] != newMapLayout.getCoinFromLevel(newMapLayout.getMapLevel())
								.get(j).getY())
						&& i < coordsWithOutBoosts.size() - 1
						&& coordsWithOutBoosts.get(i)[1] <= (scene.getHeight() * 3 / 4)) {
					coordsWithOutBoostsAndCoins.add(coordsWithOutBoosts.get(i));
					i++;
				}
			}
		}

		if (newMapLayout.getMapLevel() <= 10) {
			spawnAmount = 1;
		} else if (newMapLayout.getMapLevel() > 10 && newMapLayout.getMapLevel() < 20) {
			spawnAmount = 2;
		} else {
			spawnAmount = (newMapLayout.getMapLevel() / 10) + 1;
		}

		for (int k = 0; k < spawnAmount; k++) {
			if (!coordsWithOutBoostsAndCoins.isEmpty()) {
				int[] coord = coordsWithOutBoostsAndCoins.get(rand.nextInt(coordsWithOutBoostsAndCoins.size()));

				Enemy enemy = new Enemy(img);
				enemy.setX(coord[0]);
				enemy.setY(coord[1]);
				worldTarget.getChildren().add(enemy);
			}
		}
	}
}
