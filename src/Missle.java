import javafx.scene.image.Image;

public class Missle extends Actor{

	private int missleSpeed = 2;
	
	public Missle (Image img) {
		setImage(img);
	}
	
	@Override
	public void act(long now) {
		double userX = Game.sprite.getX() - this.getX();
		double userY = Game.sprite.getY() - this.getY();
	    
		double radians = Math.atan2(userY, userX);
		
		move(Math.cos(radians)*missleSpeed, Math.sin(radians)*missleSpeed);
		if(this.getOneIntersectingObject(player.class) != null) {
			System.out.println(this + "HIT PLAYER");
			((testWorld) getWorld()).setCoinValue((((testWorld) getWorld()).getCoinAmount() - 1));
			getWorld().getChildren().remove(this);
		}
	}

}
