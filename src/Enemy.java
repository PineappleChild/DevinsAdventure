
import java.util.concurrent.TimeUnit;

import javafx.scene.image.Image;

public class Enemy extends Actor {

	private String bubbleImg = getClass().getClassLoader().getResource("resources/bubble.png").toString();

	private Image missleImg = new Image(bubbleImg, 25, 25, true, true);

	private Missle missle = new Missle(missleImg);

	private int distanceTotal = 0;
	private int lastX = (int) getX();
	private int lastY = (int) getY();

	public Enemy(Image img) {
		setImage(img);
	}

	@Override
	public void act(long now) {
		if (distance(getX(), getY(), Game.getSprite().getX(), Game.getSprite().getY()) <= 150) {
			if (!getWorld().getChildren().contains(missle)) {
				missle.setX(this.getX() + missle.getWidth());
				missle.setY(this.getY());
				getWorld().add(missle);
			}
		}

		distanceTotal += (int) Math.abs(distance(missle.getX(), missle.getY(), lastX, lastY));

		if (distanceTotal >= 500 && getWorld().getChildren().contains(missle)) {
			System.out.println(missle + "Popped");
			distanceTotal = 0;
			lastX = (int) getX();
			lastY = (int) getY();
			getWorld().remove(missle);
		}
		lastX = (int) missle.getX();
		lastY = (int) missle.getY();
	}

	public double distance(double xPointOne, double yPointOne, double xPointTwo, double yPointTwo) {
		return Math.sqrt(
				(yPointTwo - yPointOne) * (yPointTwo - yPointOne) + (xPointTwo - xPointOne) * (xPointTwo - xPointOne));
	}

}
