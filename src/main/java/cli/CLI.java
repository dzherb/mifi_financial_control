package cli;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class CLI {
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

    protected void printCommandDescription(String command, String description) {
        print(command, TextColor.BLUE);
        print(" - " + description + "\n");
    }

    protected String getInput() {
        print("\n>>> ");
        return scanner.nextLine().trim();
    }

    protected int getIntInput() {
        boolean repeat;
        int input = 0;

        do {
            repeat = false;
            try {
                input = Integer.parseInt(getInput());
            }
            catch (NumberFormatException e) {
                print("\nНа вход ожидается число, введите еще раз\n", TextColor.RED);
                repeat = true;
            }

        } while (repeat);

        return input;
    }

    protected boolean yesNoInput(String prompt) {
        while (true) {
            print(prompt);
            String input = getInput();
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
