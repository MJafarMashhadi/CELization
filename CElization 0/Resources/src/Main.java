import java.util.Scanner;


public class Main {
	
	private static void printMap(Map map, Worker worker){
		for (int i = 0; i < map.getRowCount(); i++) {
			for (int j = 0; j < map.getColCount(); j++) {
				if(worker != null && i == worker.location.row && j == worker.location.col)
				{
					System.out.print("W");
					continue;
				}
				Block b = map.getBlock(i,j);
				switch ( b.getType()) {
				case Plain:
					System.out.print('P');
					break;
				}
			}
			System.out.println();
		}
	}
	
	private static void nextturn(Worker worker){
		worker.update();
	}
	
	public static void main(String[] args) {
		Block[][] blocks = new Block[5][5];
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				blocks[i][j] = new Block(BlockType.Plain,i,j);
			}
		}
		Map map = new Map(blocks);
		
		Game game = new Game(map);
		Worker  worker = new Worker(game, 0,0);
		game.addObject(worker);
		printMap(map, worker);
		Scanner scanner = new Scanner(System.in);
		worker.move(scanner.nextInt(), scanner.nextInt());
		while(true){
			printMap(map, worker);
			scanner.next();
			game.nextTurn();
		}
		
		
	}
}
