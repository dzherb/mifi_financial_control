package cli;

import storage.StoragesSerializer;
import users.Authentication;
import users.User;

import java.io.IOException;
import java.util.Optional;

public class FinancialControlApplicationCLI extends CLI {
    private User currentUser;
    private final Authentication authentication = new Authentication();

    public void run() {
        try {
            // Пробуем загрузить последнее сохранение
            StoragesSerializer.deserialize();
        } catch (IOException | ClassNotFoundException _) {}

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
        currentUser = user.get();
        print("\nВы успешно вошли как " + currentUser.getUsername() + "!\n", TextColor.GREEN);
        return true;
    }

    private void availableCommands() {
        print("\nГЛАВНОЕ МЕНЮ\n", TextColor.CYAN);
        print("Доступные команды:\n");
        printCommandDescription("finances", "управление финансами");
        printCommandDescription("me", "вывести данные своего профиля");
        printCommandDescription("logout", "выход из аккаунта");
        printCommandDescription("exit", "выход из приложения");
    }

    private void handleNextCommand() {
        switch (getInput()) {
            case "finances":
                onFinancesCommand();
                break;
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

    private void onFinancesCommand() {
        new FinancesLayerCommandsHandler(currentUser).run();
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
        try {
            // Пробуем сохранить состояние приложения
            StoragesSerializer.serialize();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    private void onUnknownCommand() {
        print("\nКоманда не распознана :с\n", TextColor.RED);
    }
}
