
public class Messages {

	public static final String HELP_MESSAGE = "\nCommands available:\n - search [search term]\n - position [x/reset] [y/reset]\n - scale [x] [y]\n - window [show/hide]\n - exit";
	
	public static final String WINDOW_CMD_HELP = "--> Usage: window [show/close]";
	public static final String SEARCH_CMD_HELP = "--> Usage: search [term]";
	public static final String POS_CMD_HELP = "--> Usage: position [x] [y] (or use [reset] instead of a number)";
	public static final String SCALE_CMD_HELP = "--> Usage: scale [x] [y]";
	
	public static final String SCALE_BELOW_ZERO_MESSAGE = "--> Scale cannot be lower than 0!";
	public static final String NAN_MESSAGE = "--> Input was not a number.";
	public static final String NO_RESULTS_MESSAGE = "--> No results available for your input";
}
