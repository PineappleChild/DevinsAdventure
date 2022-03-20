import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CoinCounter extends Text {
	private int score;
	private Font font;

	public CoinCounter() {
		score = 0;
		font = new Font(50);
		super.setFont(font);
		super.setFill(Color.RED);
		super.setTextAlignment(TextAlignment.CENTER);
		updateDisplay();
	}

	public void updateDisplay() {
		
		super.setText("" + score);
	}

	public int getCoinCounter() {
		return score;
	}

	public void setCoinCounter(int score) {
		if (score < 0) {
			score = 0;
		}
		this.score = score;
	}
}
