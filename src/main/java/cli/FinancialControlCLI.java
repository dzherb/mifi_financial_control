package cli;

import users.Authentication;
import users.User;

import java.util.Optional;

public class FinancialControlCLI extends CLIApplication {
    private User currentUser;
    private final Authentication authentication = new Authentication();

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
        print("\nДля продолжения работы требуется аутентификация\n", TextColor.YELLOW);

        while (true) {
            if (yesNoInput("\nХотите создать новый аккаунт? (y/n)\n")) {
                if (createNewAccount()) {
                    break;
                }
            } else {
                if (login()) {
                    break;
                }
            }
        }
    }

    private boolean createNewAccount() {
        print("\nВведите имя пользователя:\n");
        String username = getInput();
        print("\nВведите пароль:\n");
        String password = getInput();

        User user;
        try {
            user = authentication.register(username, password);
        }
        catch (Authentication.ValidationException e) {
            print("\n" + e.getMessage() + "\n", TextColor.RED);
            return false;
        }

        print("\nПользователь был успешно создан!\n", TextColor.GREEN);
        currentUser = user;
        return true;
    }

    private boolean login() {
        print("\nВход в существующий аккаунт.\nВведите имя пользователя:\n");
        String username = getInput();
        print("\nВведите пароль:\n");
        String password = getInput();

        Optional<User> user = authentication.login(username, password);
        if (user.isEmpty()) {
            print("\nВход не удался :с\n", TextColor.RED);
            return false;
        }
        print("\nВы успешно вошли как " + currentUser.getUsername() + "!\n", TextColor.GREEN);
        currentUser = user.get();
        return true;
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
        print("\nИмя пользователя: ");
        print(currentUser.getUsername(), TextColor.YELLOW);
        print("\nUUID: ");
        print(currentUser.getUUID() + "\n", TextColor.YELLOW);
    }

    private void onLogoutCommand() {
        if (yesNoInput("\nВы точно хотите выйти из аккаунта? (y/n)\n")) {
            currentUser = null;
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
