
public class Map {
	private Block[][] blocks;
	
	public Map(Block[][] b) {
		blocks = b;
	}
	
	public Block getBlock(int row, int col){
		return blocks[row][col];
	}
	
	public int getRowCount(){
		return blocks.length;
	}
	
	public int getColCount(){
		return blocks[0].length;
	}
}
