import java.util.Scanner;

public class Main {

	public static ImageFrame f = new ImageFrame(1000, 1000, 0.6f);
	public static GoogleImagesScraper g = new GoogleImagesScraper();

	public static void main(String[] args) throws Exception {

		try (Scanner s = new Scanner(System.in)) {
			while (true) {
				handleCommand(s.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void handleCommand(String input) {
		String[] cmdParts = input.split(" ", 2);

		switch (cmdParts[0].toLowerCase()) {
		// Help Command
		case "help":
			System.out.println(Messages.HELP_MESSAGE);
			break;
		// Exit Command
		case "exit":
			System.exit(0);
			// Window Commands
		case "window":
			// Integrity check
			if (cmdParts.length == 1) {
				System.out.println(Messages.WINDOW_CMD_HELP);
				return;
			}
			switch (cmdParts[1]) {
			// Show Window
			case "show":
				f.openWindow();
				break;
			// Hide Window
			case "hide":
				f.hideWindow();
				break;
			}
			break;
		// Search Command
		case "s":
		case "show":
		case "search":
			// Integrity check
			if (cmdParts.length == 1) {
				System.out.println(Messages.SEARCH_CMD_HELP);
				return;
			}
			// Search on Google and display it
			g.setSearch(cmdParts[1], 0);
			if (!displaySearch()) {
				// No results available
				System.out.println(Messages.NO_RESULTS_MESSAGE);
			}
			break;
		// Position Commands
		case "pos":
		case "position":
			// Integrity Check
			if (cmdParts.length == 1) {
				System.out.println(Messages.POS_CMD_HELP);
				return;
			}
			// Split and check for integrity
			String[] pointInput = cmdParts[1].split(" ");
			if (pointInput.length < 2) {
				System.out.println(Messages.POS_CMD_HELP);
			}

			// Parse the values entered by the user
			try {
				// X Position
				int x = -1;
				System.out.println(pointInput[0]);
				if (!pointInput[0].equals("reset"))
					x = Integer.parseInt(pointInput[0]);

				// Y Position
				int y = -1;
				if (!pointInput[1].equals("reset")) {
					y = Integer.parseInt(pointInput[1]);
				}

				// Call the relocator
				f.setWindowLocation(x, y);

			} catch (Exception e) {
				System.out.println("--> Input was not a number.");
			}
			break;
		}
	}

	// Tries a hundred times to display an image. Returns false when no possible
	// images were found
	public static boolean displaySearch() {
		try {
			for (int i = 0; i < 100; i++) {
				String url = g.getImageUrl(i);
				System.out.println(url);

				if (f.setCurrentImageFromURL(url)) {
					f.closeWindow();
					f.openWindow();
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
