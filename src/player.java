import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class player extends Actor {
	private double xVelocity = 5;
	private double yVelocity = 0;
	private double gravity = 0.15;
	// use this to make the boost pad
	private int topVelocity = 10;

	private int jumpBoostAmount = 6;

	private boolean isAandSpacePressed = false;
	private boolean isDandSpacePressed = false;

	private boolean isGrounded = false;
	private boolean inAir = false;

	private BooleanProperty isBottomSend = new SimpleBooleanProperty(false);
	private BooleanProperty isTopSend = new SimpleBooleanProperty(false);

	private List<Brick> direction;

	public player(Image img) {
		setImage(img);
	}

	@Override
	public void act(long now) {
		Point2D coords = localToScene(getX(), getY());
		boolean canA = true;
		boolean canD = true;

		boolean rightSide = coords.getX() >= getScene().getWidth() - getWidth();
		boolean leftSide = coords.getX() <= 0;

		boolean bottomSide = coords.getY() >= getScene().getHeight() - getHeight() - yVelocity;
		boolean topSide = coords.getY() <= 0;

		// gravity
		move(0, yVelocity);

		//
		if (rightSide) {
			canD = false;
		} else if (leftSide) {
			canA = false;
		} else if (topSide) {
			setIsTopSend(true);
		} else if (bottomSide) {
			setIsBottomSend(true);

		} else {
			setIsBottomSend(false);
			setIsTopSend(false);
		}

		// gravity acceleration bounds
		if (yVelocity <= -topVelocity) {
			yVelocity = -topVelocity;
		} else if (yVelocity >= topVelocity) {
			yVelocity = topVelocity;
		}

		// adds values to gravity to simulate acceleration
		if (!isGrounded) {
			yVelocity += gravity;
		}

		// brick collision with the player
		if (getOneIntersectingObject(Brick.class) != null) {
			direction = getIntersectingObjects(Brick.class);
			for (Brick elem : direction) {
				if (elem.getSurface(this) == 1) {
					isGrounded = true;
					yVelocity = 0;
					inAir = false;
				} else if (elem.getSurface(this) == 2) {
					isGrounded = false;
				} else if (elem.getSurface(this) == 3) {
					canD = false;

					isGrounded = false;
				} else if (elem.getSurface(this) == 4) {
					canA = false;

					isGrounded = false;
				}
			}
			isGrounded = false;
		} else {
			if (direction != null) {
				direction.clear();
			}
		}

		if (getOneIntersectingObject(Coin.class) != null) {
			Coin coin = getOneIntersectingObject(Coin.class);
			((testWorld) getWorld()).remove(coin);
			((testWorld) getWorld()).setCoinValue((((testWorld) getWorld()).getCoinAmount() + 1));
			Game.newMapLayout.getCoinFromLevel(Game.newMapLayout.getMapLevel()).remove(coin);
		}

		// Handles the movement for pressing two keys(space and horizontal movement)
		if (isAandSpacePressed && canA) {
			if (!inAir) {
				yVelocity += -jumpBoostAmount;
				inAir = true;
			}
			this.setX(getX() - xVelocity);
		} else if (isDandSpacePressed && canD) {
			if (!inAir) {
				yVelocity += -jumpBoostAmount;
				inAir = true;
			}
			this.setX(getX() + xVelocity);
		}

		// handles regular movement
		if (!(isAandSpacePressed || isDandSpacePressed)) {
			if (canA && getWorld().isInKeyDownTracker(KeyCode.A)) {
				move(-xVelocity, 0);
			} else if (canD && getWorld().isInKeyDownTracker(KeyCode.D)) {
				move(xVelocity, 0);
			} else if (yVelocity == 0 && getWorld().isInKeyDownTracker(KeyCode.SPACE)) {
				yVelocity += -jumpBoostAmount;
			}
			inAir = true;
		}

//        System.out.println(yVelocity);
	}

	public int getTopVelocity() {
		return topVelocity;
	}

	public void setTopVelocity(int topVelocity) {
		this.topVelocity = topVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public void setIsAandSpacePressed(boolean value) {
		isAandSpacePressed = value;
	}

	public void setIsDandSpacePressed(boolean value) {
		isDandSpacePressed = value;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getJumpBoostAmount() {
		return jumpBoostAmount;
	}

	public void setJumpBoostAmount(int jumpBoostAmount) {
		this.jumpBoostAmount = jumpBoostAmount;
	}

	public BooleanProperty getIsBottomSend() {
		return isBottomSend;
	}

	public void setIsBottomSend(Boolean value) {
		isBottomSend.setValue(value);
	}

	public BooleanProperty getIsTopSend() {
		return isTopSend;
	}

	public void setIsTopSend(Boolean value) {
		isTopSend.setValue(value);
	}

}
