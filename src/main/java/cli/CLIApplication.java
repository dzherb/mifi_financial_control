package cli;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class CLIApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out = System.out;

    private final String[] YES_VALUES = {"y", "yes", "д", "да"};
    private final String[] NO_VALUES = {"n", "no", "н", "нет"};

    abstract void run();

    protected void print(String message) {
        out.print(message);
    }

    protected void print(String message, TextColor color) {
        print(color + message + TextColor.RESET);
    }

    protected String getInput(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    protected String getInput() {
        return getInput("\n>>> ");
    }

    protected boolean yesNoInput(String prompt) {
        while (true) {
            print(prompt);
            String input = getInput().trim();
            if (isOneOfIgnoreCase(input, YES_VALUES)) {
                return true;
            }
            if (isOneOfIgnoreCase(input, NO_VALUES)) {
                return false;
            }
        }
    }

    private boolean isOneOfIgnoreCase(String value, String[] values) {
        for (String v : values) {
            if (v.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
