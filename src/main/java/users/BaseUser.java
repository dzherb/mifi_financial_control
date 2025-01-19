package users;

import finances.Wallet;
import storage.UsersStorage;
import storage.WalletsStorage;
import utils.UUIDGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class BaseUser implements User {
    private final String username;
    private final String uuid;
    private final String passwordHashed;

    private BaseUser(String username, String password) {
        this.username = username;
        this.uuid = UUIDGenerator.generate();
        this.passwordHashed = hashPassword(password);
    }

    public static User create(String username, String password) {
        User user = new BaseUser(username, password);
        UsersStorage.getInstance().add(user);

        Wallet wallet = new Wallet(user);
        WalletsStorage.getInstance().add(wallet);

        return user;
    }

    private String hashPassword(String password) {
        // В учебных целях просто хэшируем пароль через md5.
        // В реальных условиях, конечно, нужно куда более безопасное хэширование.
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest(password.getBytes());
        return new String(hash);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public boolean checkPassword(String password) {
        return passwordHashed.equals(hashPassword(password));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUser baseUser)) return false;
        return Objects.equals(uuid, baseUser.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
