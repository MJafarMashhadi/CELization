import java.util.ArrayList;


public class Game {
	Map map;
	ArrayList<GameObject> objects;
	public Game(Map m) {
		map = m;
		objects = new ArrayList<GameObject>();
	}
	
	public void addObject(GameObject o){
		objects.add(o);
	}
	
	public Map getMap(){
		return map;
	}
	
	public void nextTurn(){
		for (GameObject o : objects) {
			o.update();
		}
	}
}
