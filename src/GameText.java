import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameText extends Text {
	private Font font;

	public GameText(String text, Color color) {
		font = new Font(50);
		super.setFont(font);
		super.setFill(color);
		super.setText(text);
	}
}
