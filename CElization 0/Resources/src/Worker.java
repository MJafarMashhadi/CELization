import java.util.LinkedList;


public class Worker extends GameObject {
	Point target;
	Point location;
	Game game;
	public Worker(Game game, int row, int col) {
		this.game = game;
		location = new Point(row,col);
	}
	public static String  findPath(boolean[][] map, int sRow, int sCol, int dRow,
			int dCol) {
		
		class Point{
			public int row;
			public int col;
			public Point(int row, int col){
				this.row = row;
				this.col = col;
			}
		}
		
		int rowCount = map.length;
		int colCount = map[0].length;
		
		String[][] directions = new String[rowCount][colCount];
		Point[][] parents = new Point[rowCount][colCount];
		boolean[][] visited = new boolean[rowCount][colCount];

		boolean found = false;

		LinkedList<Point> queue = new LinkedList<Point>();

		queue.add(new Point(sRow,sCol));
		while (!found && queue.size() > 0) {
			Point point = queue.poll();

			if (point.row == dRow && point.col == dCol)
				found = true;
			
			String[] dirList = {"up", "right", "down", "left"};
			int[] deltaRow   = {-1, 0, 1, 0};
			int[] deltaCol   = { 0, 1, 0,-1};
			
			for (int i = 0; i < 4; i++){
				int cRow = point.row + deltaRow[i];
				int cCol = point.col + deltaCol[i];
				if(cRow < 0 || cRow >= rowCount || cCol < 0 || cCol >= colCount)
					continue;
				if (visited[cRow][cCol] || !map[cRow][cCol])
					continue;
				queue.add(new Point(cRow, cCol));
				directions[cRow][cCol] = dirList[i];
				parents[cRow][cCol] = point;
				visited[cRow][cCol] = true;

				if (cRow == dRow && cCol == dCol)
					found = true;
			}
		}

		if (!found)
			return "none";
		Point point = new Point(dRow, dCol);
		Point parent = parents[dRow][dCol];
		while (parent != null && !(parent.row == sRow && parent.col == sCol)){
			point = parent;
			parent = parents[point.row][point.col];
		}
		
		String dir = directions[point.row][point.col];
		if(dir == null)
			return "none";
		else
			return dir;
	}
	
	
    public void update() {
		if(target != null){
			Map map = game.getMap();
			boolean[][] bmap  =new boolean[map.getRowCount()][map.getColCount()];
			for (int i = 0; i < bmap.length; i++) {
				for (int j = 0; j < bmap[0].length; j++) {
					if(map.getBlock(i, j).getType() == BlockType.Plain)
						bmap[i][j] = true;
					else
						bmap[i][j] = false;
				}
			}
			
			String dir = findPath(bmap,location.row, location.col, target.row,target.col);
			if(dir.equals("up"))
				location.row--;
			else if(dir.equals("down"))
				location.row++;
			else if (dir.equals("left"))
				location.col--;
			else if (dir.equals("right"))
				location.col++;
			
		}
	}

	public void move(int row, int col){
		target = new Point(row, col);
	}
	
	public void build(){
		
	}
}
