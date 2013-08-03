import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import judge.Judge;
import celization.CElization;

/**
 * 
 */

/**
 * @author mjafar
 * 
 */
public final class Main {
	public static CElization game;
	public static Map<String, Action> commands = new HashMap<String, Action>();

	/**
	 * @param args
	 */
	private static Scanner consoleIn = new Scanner(System.in);
	
	public static void main(String[] args) {
		String command = new String();
		initializeActions();

		/** Initialize game */
		System.err.println("Starting game!");
		game = new CElization();
		Judge.injectGameInstance(game);
		/** get and parse and do commands */
		while (true) {
			System.out.print("$ ");
			command = consoleIn.nextLine();
			command = command.trim();
			if (command.isEmpty()) {
				continue;
			}
			if (!commands.containsKey((command.split(" ")[0]))) {
				System.err
						.printf("Invalid command \"%s\".\nUse 'help' to get a list of availabble commands.\n",
								command.split(" ")[0]);

				continue;
			}
			try {
				parseCommand(command);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // end of while

	} // end of main

	public static void close() {
		consoleIn.close();
		SimpleGraphic.close();
		System.exit(0);
	}

	private static boolean parseCommand(String c) {
		String[] cParts = c.split(" ");
		String commandName = cParts[0];
		String[] args = new String[cParts.length - 1];
		for (int i = 0; i < cParts.length - 1; i++) {
			args[i] = cParts[i + 1];
		}

		return commands.get(commandName).Do(args);
	}

	private static void initializeActions() {
		ExitGame exitGame = new ExitGame();
		commands.put("exit", exitGame);
		commands.put("bye", exitGame);
		commands.put("quit", exitGame);

		ShowGraphics show = new ShowGraphics();
		commands.put("show", show);

		AllCommands help = new AllCommands();
		commands.put("help", help);

		Info info = new Info();
		commands.put("info", info);

		doAction action = new doAction();
		commands.put("action", action);

		NextTurn nextTurn = new NextTurn();
		commands.put("next", nextTurn);
	}

}

abstract class Action {
	protected String helpString;

	public abstract boolean Do(String[] args);

	public void ShowHelp() {
		System.err.println(helpString);
	}

	public String getHelpString() {
		return helpString;
	}
}

final class ExitGame extends Action {
	public ExitGame() {
		helpString = new String("Exits Game. Progress will not be saved.");
	}

	@Override
	public boolean Do(String[] args) {
		Main.close();
		return true;
	}
}

final class ShowGraphics extends Action {
	public ShowGraphics() {
		helpString = new String("Displays a window of game map.");
	}

	@Override
	public boolean Do(String[] args) {
		System.err.println("Initializing Graphics");
		SimpleGraphic.main(Main.game);
		return true;
	}
}

final class AllCommands extends Action {
	public AllCommands() {
		helpString = new String("Shows all available commands.");
	}

	@Override
	public boolean Do(String[] args) {
		for (String c : Main.commands.keySet()) {
			System.out.printf("%-15s - %s \n", c, Main.commands.get(c)
					.getHelpString());
		}
		return true;
	}
}

final class Info extends Action {
	public Info() {
		helpString = new String("Get information about a game object.");
	}

	@Override
	public boolean Do(String[] args) {
		Map<String, String> gotInfo;
		StringBuilder argsStr = new StringBuilder(5);

		if (args.length == 0) {
			gotInfo = Judge.info(null);
		} else {
			for (int i = 0; i < args.length; i++) {
				argsStr.append(args[i]);
				argsStr.append(' ');
			}
			gotInfo = Judge.info(argsStr.toString().trim());
		}
		if (args.length > 0) {
			System.out.println("Information got from `" + args[0] + "' :");
		}
		for (String key : gotInfo.keySet()) {
			System.out.printf(" %-10s : %s\n", key, gotInfo.get(key));
		}

		return true;
	}

}

final class doAction extends Action {
	public doAction() {
		helpString = new String("Order game to perform an action.");
	}

	@Override
	public boolean Do(String[] args) {
		String objectID = args[0];
		StringBuilder command = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			command.append(args[i] + " ");
		}

		System.out.println(Judge.action(objectID, command.toString().trim()));
		return true;
	}
}

final class NextTurn extends Action {
	public NextTurn() {
		helpString = new String("Go to next turn");
	}

	@Override
	public boolean Do(String[] args) {
		String[] events = Judge.nextTurn();
		if (events.length == 0) {
			// System.out.println(">> nothing special happened");
		} else {
			for (int i = 0; i < events.length; i++) {
				System.out.printf(">> %s\n", events[i]);
			}
		}
		return true;
	}

}