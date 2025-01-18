package cli;

public class FinancialControlCLI extends CLIApplication {

    public void run() {
        greet();
        authenticate();
        while (true) {
            availableCommands();
            handleNextCommand();
        }
    }

    private void greet() {
        print("""
        \nДобро пожаловать в приложение для управления личными финансами!
        """, TextColor.PURPLE);
    }

    private void authenticate() {
        print("\nДля продолжения работы требуется авторизация\n");

        while (true) {
            if (yesNoInput("\nХотите создать новый аккаунт? (y/n)\n")) {
                createNewAccount();
                break;
            } else {
                if (login()) {
                    break;
                }
            }
        }
    }

    private void createNewAccount() {
        print("\nnew\n");
    }

    private boolean login() {
        print("\nlogin\n");
        print("\nВход не удался :с\n", TextColor.RED);
        return false;
    }

    private void availableCommands() {
        print("\nДоступные команды:\n");
        printCommand("me", "вывести данные своего профиля");
        printCommand("logout", "выход из аккаунта");
        printCommand("exit", "выход из приложения");
    }

    private void printCommand(String command, String description) {
        print(command, TextColor.BLUE);
        print(" - " + description + "\n");
    }

    private void handleNextCommand() {
        switch (getInput()) {
            case "me":
                onMeCommand();
                break;
            case "logout":
                onLogoutCommand();
                break;
            case "exit":
                onExitCommand();
                break;
            default:
                onUnknownCommand();
        }

    }

    private void onMeCommand() {
        print("\nme command\n", TextColor.CYAN);
    }

    private void onLogoutCommand() {
        if (yesNoInput("\nВы точно хотите выйти из аккаунта? (y/n)\n")) {
            // todo логика выхода
            print("\nВы вышли\n", TextColor.RED);
            authenticate();
        }
    }

    private void onExitCommand() {
        System.exit(0);
    }

    private void onUnknownCommand() {
        print("\nКоманда нераспознана :с\n", TextColor.RED);
    }
}
