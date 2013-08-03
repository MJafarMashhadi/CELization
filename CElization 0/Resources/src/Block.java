
public class Block {
	private BlockType type;
	private int row, col;
	public Block(BlockType t, int r, int c) {
		type = t;
		row = r;
		col = c;
	}
	public BlockType getType(){
		return type;
	}
}
