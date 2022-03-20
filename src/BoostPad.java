import javafx.scene.image.Image;

public class BoostPad extends Brick{
	
	public BoostPad(Image img) {
		super(img);
	}
	
	
	@Override
	public void act(long now) {
		boostTouching();
	}
	
	public void boostTouching() {
		if(this.getOneIntersectingObject(player.class) != null) {
			player contact = this.getOneIntersectingObject(player.class);
			contact.setTopVelocity(10);
			contact.setyVelocity(-10);
		}
	}

}
