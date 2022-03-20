import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashSet;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import javafx.animation.AnimationTimer;

public abstract class World extends javafx.scene.layout.Pane {
	private javafx.animation.AnimationTimer timer;

	private boolean isTimerRunning = false;;

	HashSet<KeyCode> keyDownTracker = new HashSet<KeyCode>();

	public World() {
		timer = new Timer();
	}

	private class Timer extends AnimationTimer {
		@Override
		public void handle(long sec) {
			act(sec);
			for (int i = 0; i < getChildren().size(); i++) {
				if (getChildren().get(i).getClass() != CoinCounter.class
						&& getChildren().get(i).getClass() != GameText.class) {
					((Actor) getChildren().get(i)).act(sec);
				}
			}
		}

	}

	public void addKeyToKeyDownTracker(KeyCode key) {
		keyDownTracker.add(key);
	}

	public void removeKeyFromKeyDownTracker(KeyCode key) {
		keyDownTracker.remove(key);
	}

	public boolean isInKeyDownTracker(KeyCode key) {
		if (keyDownTracker.contains(key))
			return true;
		return false;
	}

	public void start() {
		timer.start();
		isTimerRunning = true;
	}

	public void stop() {
		timer.stop();
		isTimerRunning = false;
	}

	public boolean isTimerOn() {
		return isTimerRunning;
	}

	public void add(Actor actor) {
		getChildren().add(actor);
	}

	public void remove(Actor actor) {
		getChildren().remove(actor);
	}

	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls) {
		ArrayList<A> newList = new ArrayList<A>();
		for (int i = 0; i < getChildren().size(); i++) {
			if (cls.isInstance(getChildren().get(i))) {
				newList.add((A) getChildren().get(i));
			}
		}
		return newList;
	}

	public abstract void act(long now);
}
