package users;

import storage.Storage;
import storage.UsersStorage;
import storage.WalletsStorage;

import java.util.Optional;

public class Authentication {
    private final Storage<User> usersStorage = UsersStorage.getInstance();

    private final int USERNAME_MIN_LENGTH = 3;
    private final int PASSWORD_MIN_LENGTH = 6;

    public static class ValidationException extends Exception {
        private ValidationException(String message) {
            super(message);
        }
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = usersStorage.find(u -> u.getUsername().equals(username));
        if (user.isPresent() && user.get().checkPassword(password)) {
            return user;
        }

        return Optional.empty();
    }

    public User register(String username, String password) throws ValidationException {
        Optional<User> user = usersStorage.find(u -> u.getUsername().equals(username));
        if (user.isPresent()) {
            throw new ValidationException("Пользователь с таким именем уже существует");
        }
        return BaseUser.create(
            validateUsername(username),
            validatePassword(password)
        );
    }

    private String validateUsername(String username) throws ValidationException {
        if (isTooShort(username, USERNAME_MIN_LENGTH)) {
            throw new ValidationException("Имя должно быть не меньше " + USERNAME_MIN_LENGTH + " символов");
        }
        if (containsWhitespace(username)) {
            throw new ValidationException("Имя не должно содержать пробелов");
        }
        return username;
    }

    private String validatePassword(String password) throws ValidationException {
        if (isTooShort(password, PASSWORD_MIN_LENGTH)) {
            throw new ValidationException("Пароль должен быть не меньше " + PASSWORD_MIN_LENGTH + " символов");
        }
        if (containsWhitespace(password)) {
            throw new ValidationException("Пароль не должен содержать пробелов");
        }
        // Для пароля можно еще добавить проверки
        return password;
    }

    private boolean isTooShort(String value, int minLength) {
        return value == null || value.length() < minLength;
    }

    private boolean containsWhitespace(String value) {
        return value != null && value.contains(" ");
    }
}
