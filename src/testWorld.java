import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class testWorld extends World {

	private int coinsToWinGame = 50;
	private int score = 0;

	private boolean gameWon = false;
	private CoinCounter val;
	private GameText gameWonText;
	private GameText gameWonTextUnder;

	public testWorld() {
		val = new CoinCounter();

		gameWonText = new GameText("Game Won!", Color.LIME);
		gameWonTextUnder = new GameText("Press Space To Continue!", Color.LIME);

		getChildren().add(val);

	}

	@Override
	public void act(long now) {
		System.out.println(val.getCoinCounter());
		val.setX(25);
		val.setY(50);

		gameWonText.setX(Game.SceneWidth / 2 - gameWonText.getBoundsInLocal().getWidth() / 2);
		gameWonText.setY(Game.SceneHeight / 2 - gameWonText.getBoundsInLocal().getHeight() / 2);

		gameWonTextUnder.setX(Game.SceneWidth / 2 - gameWonTextUnder.getBoundsInLocal().getWidth() / 2);
		gameWonTextUnder.setY(Game.SceneHeight / 2 - gameWonTextUnder.getBoundsInLocal().getHeight() / 2
				+ (gameWonText.getBoundsInLocal().getHeight() + 20));

		if (val.getCoinCounter() == coinsToWinGame) {
			getChildren().add(gameWonText);
			getChildren().add(gameWonTextUnder);
			this.stop();
			gameWon = true;
		}

	}

	public GameText getGameWonText() {
		return gameWonText;
	}

	public GameText getGameWonTextUnder() {
		return gameWonTextUnder;
	}

	public int getCoinsToWinGame() {
		return coinsToWinGame;
	}

	public void setCoinsToWinGame(int coinsToWinGame) {
		this.coinsToWinGame = coinsToWinGame;
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}

	public int getCoinAmount() {
		return score;
	}

	public void setCoinValue(int score) {
		this.score = score;
		val.toFront();
		val.setCoinCounter(score);
		val.updateDisplay();
	}

}
