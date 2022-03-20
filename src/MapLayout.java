import java.util.ArrayList;

public class MapLayout {
	private ArrayList<ArrayList<Brick>> levels = new ArrayList<ArrayList<Brick>>();
	private ArrayList<ArrayList<Coin>> coins = new ArrayList<ArrayList<Coin>>();
	private ArrayList<BoostPad> boostSpots = new ArrayList<BoostPad>();
	private ArrayList<ArrayList<ArrayList<Integer>>> levelMap = new ArrayList<ArrayList<ArrayList<Integer>>>();
	private ArrayList<Brick> brickStorage = new ArrayList<Brick>();
	private ArrayList<ArrayList<Integer>> xArr = new ArrayList<ArrayList<Integer>>();

	private int mapLevel = 0;

	public MapLayout() {

	}

	public ArrayList<BoostPad> getBoostSpots() {
		return boostSpots;
	}

	public void setBoostSpots(ArrayList<BoostPad> arr) {
		boostSpots = arr;
	}

	public ArrayList<ArrayList<Brick>> getLevelsList() {
		return levels;
	}

	public void addToLevelMap(ArrayList<ArrayList<Integer>> level) {
		levelMap.add(level);
	}

	public ArrayList<Coin> getCoinFromLevel(int level) {
		return coins.get(level);
	}

	public ArrayList<ArrayList<Coin>> getCoinsList() {
		return coins;
	}

	public void addToCoinList(ArrayList<Coin> coinsFromLevel) {
		coins.add(coinsFromLevel);
	}

	public ArrayList<ArrayList<Integer>> getLevelMap(int levelNum) {
		return levelMap.get(levelNum);
	}

	public void addToLevel(ArrayList<Brick> level) {
		levels.add(level);
	}

	public ArrayList<Brick> getLevel(int levelNumber) {
		return levels.get(levelNumber);
	}

	public void incrementMapLevel() {
		mapLevel++;
	}

	public void decrementMapLevel() {
		mapLevel--;
	}

	public int getMapLevel() {
		return mapLevel;
	}

	public ArrayList<ArrayList<Integer>> getxArr() {
		return xArr;
	}

	public void setxArr(ArrayList<ArrayList<Integer>> xArr) {
		this.xArr = xArr;
	}

	public ArrayList<Brick> getBrickStorage() {
		return brickStorage;
	}

	public void setBrickStorage(ArrayList<Brick> brickStorage) {
		this.brickStorage = brickStorage;
	}

	public void printCurrentMap(ArrayList<ArrayList<Integer>> currMap) {
		for (ArrayList<Integer> e : currMap) {
			for (Integer elem : e) {
				System.out.print(elem + " ");
			}
			System.out.println(" ");
		}
	}

	// gets spots which are empty above a brick
	public ArrayList<int[]> getValidSpots(ArrayList<ArrayList<Integer>> currLevel) {
		ArrayList<int[]> tempArr = new ArrayList<int[]>();

		for (int row = 0; row < currLevel.size(); row++) {
			for (int col = 0; col < currLevel.get(row).size(); col++) {
				if (row > 0 && currLevel.get(row).get(col) == 1 && currLevel.get(row - 1).get(col) == 0) {
					int[] coord = new int[2];
					coord[0] = col;
					coord[1] = row - 1;
					tempArr.add(coord);
				}
			}
		}
		return tempArr;
	}

	// gets spots which have 4 empty spaces above a brick
	public ArrayList<int[]> getValidBoostSpot(ArrayList<ArrayList<Integer>> currLevel) {
		ArrayList<int[]> tempArr = new ArrayList<int[]>();
		for (int row = 0; row < currLevel.size(); row++) {
			for (int col = 0; col < currLevel.get(row).size(); col++) {
				if (row > 3 && currLevel.get(row).get(col) == 1 && currLevel.get(row - 1).get(col) == 0
						&& currLevel.get(row - 2).get(col) == 0 && currLevel.get(row - 3).get(col) == 0
						&& currLevel.get(row - 4).get(col) == 0) {
					int[] coord = new int[2];
					coord[0] = col;
					// just use row so the block placement is better
					coord[1] = row;
					tempArr.add(coord);
				}
			}
		}
		return tempArr;
	}
}
