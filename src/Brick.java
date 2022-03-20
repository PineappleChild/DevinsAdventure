import javafx.scene.image.Image;

public class Brick extends Actor {
	public Brick(Image img) {
		setImage(img);
	}

	@Override
	public void act(long now) {
		// TODO Auto-generated method stub

	}

	// checks what surface an intersecting class is on
	public int getSurface(Actor actor) {
		// add bound so that there is only a 5px gap for contact
		if (actor.getY() + actor.getHeight() >= this.getY() && actor.getY() + actor.getHeight() <= this.getY() + 10) {
			return 1; // north
		} else if (actor.getY() <= this.getY() + this.getHeight()
				&& actor.getY() >= this.getY() + this.getHeight() - 10) {
			return 2; // south
		} else if (actor.getX() + actor.getWidth() >= this.getX()
				&& actor.getX() + actor.getWidth() <= this.getX() + 10) {
			return 3; // west
		} else if (actor.getX() <= this.getX() + this.getWidth()
				&& actor.getX() >= this.getX() + this.getWidth() - 10) {
			return 4; // east
		}
		return 0;
	}
}
